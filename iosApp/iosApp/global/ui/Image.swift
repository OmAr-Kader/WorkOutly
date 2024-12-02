import Foundation
import AVFoundation
import SwiftUI
import Combine
import _PhotosUI_SwiftUI

struct ImageSystem : View {
    
    let systemIcon: String
    let tint: Color
    
    var body: some View {
        Image(
            uiImage: UIImage(
                systemName: systemIcon
            )?.withTintColor(UIColor(tint), renderingMode: .alwaysOriginal) ?? UIImage()
        ).resizable()
            .renderingMode(.template)
            .foregroundColor(tint)
            .tint(tint)
            .background(Color.clear)
            .imageScale(.medium)
            .aspectRatio(contentMode: .fill)
            .scaledToFit()
    }
}

struct ImageAsset : View {
    
    let icon: String
    let tint: Color
    
    var body: some View {
        Image(
            uiImage: UIImage(
                named: icon
            )?.withTintColor(UIColor(tint), renderingMode: .alwaysOriginal) ?? UIImage()
        ).resizable()
            .renderingMode(.template)
            .foregroundColor(tint)
            .tint(tint)
            .background(Color.clear)
            .imageScale(.medium)
            .aspectRatio(contentMode: .fit)
            .scaledToFit()
    }
}


struct ImageCacheView : View {
    
    private let urlString: String
    private let isVideoPreview: Bool
    private let contentMode: ContentMode
    private let errorImage: UIImage?
    @StateObject private var obs: UrlImageModel = UrlImageModel()
    
    init(_ urlString: String, isVideoPreview: Bool = false, contentMode: ContentMode = .fit, errorImage: UIImage? = nil) {
        self.urlString = urlString
        self.isVideoPreview = isVideoPreview
        self.contentMode = contentMode
        self.errorImage = errorImage
    }
    
    var body: some View {
        Image(uiImage: obs.image ?? errorImage ?? UIImage())
            .resizable()
            .renderingMode(.original)
            .background(Color.clear)
            .imageScale(.large)
            .aspectRatio(contentMode: contentMode)
            .task { @BackgroundActor in
                await obs.initial(url: URL(string: urlString), isPreview: isVideoPreview)
            }
            .onChange(urlString) { it in
                if obs.image == nil {
                    let url = URL(string: it)
                    Task { @BackgroundActor in
                        await obs.inti(url: url, isPreview: isVideoPreview)
                    }
                }
            }
    }
    
}


class UrlImageModel: ObservableObject, @unchecked Sendable {
    
    @MainActor
    @Published var image: UIImage? = nil
    private var url: URL? = nil
    private var cancellable: AnyCancellable? = nil

    private var imageCache: ImageCache? = nil
    
    init() {
        
    }
    /*@BackgroundActor
    init(url: URL?, isPreview: Bool) {
        self.url = url
        if isPreview {
            loadVideo()
        } else {
            loadImage()
        }
    }
    
    @BackgroundActor
    func inti(url: URL?, isPreview: Bool) {
        self.url = url
        if isPreview {
            loadVideoFromURL()
        } else {
            loadImage()
        }
    }*/
    
    @BackgroundActor
    func initial(url: URL?, isPreview: Bool) {
        if imageCache == nil {
            imageCache = ImageCache.getImageCache()
        }
        self.url = url
        if isPreview {
            loadVideo()
        } else {
            loadImage()
        }
    }
    
    @BackgroundActor
    func inti(url: URL?, isPreview: Bool) {
        if imageCache == nil {
            imageCache = ImageCache.getImageCache()
        }
        self.url = url
        if isPreview {
            loadVideoFromURL()
        } else {
            loadImage()
        }
    }

    @BackgroundActor
    func loadImage() {
        if loadImageFromCache() {
            logger("UrlImageModel", "Cache hit")
            return
        }
        logger("UrlImageModel", "Cache missing, loading from url")
        loadImageFromUrl()
    }
    
    @BackgroundActor
    func loadVideo() {
        if loadImageFromCache() {
            logger("UrlImageModel", "Cache hit")
            return
        }
        logger("UrlImageModel", "Cache missing, loading from url")
        loadVideoFromURL()
    }

    @BackgroundActor
    func loadImageFromCache() -> Bool {
        guard let url = url else {
            return false
        }
        guard let cacheImage = imageCache?[url] else {
            return false
        }
        TaskMainSwitcher {
            self.image = cacheImage
        }
        return true
    }

    @BackgroundActor
    func loadImageFromUrl() {
        guard let url = url else {
            return
        }
        TaskMainSwitcher {
            self.cancellable = URLSession.shared.dataTaskPublisher(for: url)
                .subscribe(on: DispatchQueue.global(qos: .background))
                .receive(on: DispatchQueue.main)
                .map { UIImage(data: $0.data) }
                .replaceError(with: nil)
                .handleEvents(receiveOutput: { [weak self] image in
                    logger("UrlImageModel", "WWW URL => " + String(image == nil))
                    guard let image = image else { return }
                    self?.imageCache?[url] = image
                    self?.image = image
                })
                .assign(to: \.image, on: self)
        }
    }
    
    /*private func loadImageFromURL() {
     logger("UrlImageModel", "WWW URL => " + (url?.absoluteString ?? "NULL"))
        guard let url = url else {
            return
        }
        logger("UrlImageModel", "WWW URL => " + url.absoluteString)
        URLSession.shared.dataTask(with: url) { data, response, error in
            guard error == nil else {
                logger("UrlImageModel", error ?? "unknown error")
                return
            }

            guard let data = data else {
                logger("UrlImageModel", "No data found")
                return
            }

            DispatchQueue.main.async { [weak self] in
                guard let loadedImage = UIImage(data: data) else { return }
                logger("UrlImageModel", "WWW URL => " + "LOADED")
                self?.image = loadedImage
                self?.imageCache[url] = loadedImage
            }
        }.resume()
    }*/
    
    @BackgroundActor
    private func loadVideoFromURL() {
        guard let url = url else {
            return
        }
        let asset = AVAsset(url: url)
        let assetImgGenerate = AVAssetImageGenerator(asset: asset)
        assetImgGenerate.appliesPreferredTrackTransform = true
        let time = CMTimeMakeWithSeconds(Float64(1), preferredTimescale: 100)
        do {
            let img = try assetImgGenerate.copyCGImage(at: time, actualTime: nil)
            Task { @MainActor [weak self] in
                let thumbnail = UIImage(cgImage: img)
                self?.image = thumbnail
                self?.imageCache?[url] = thumbnail
            }
        } catch {

        }
    }
}

class ImageCache {
    var cache = NSCache<NSURL, UIImage>()

    subscript(_ key: URL) -> UIImage? {
        get { cache.object(forKey: key as NSURL) }
        set { newValue == nil ? cache.removeObject(forKey: key as NSURL) : cache.setObject(newValue!, forKey: key as NSURL) }
    }
}

extension ImageCache {
    @BackgroundActor private static var imageCache = ImageCache()

    @BackgroundActor static func getImageCache() -> ImageCache {
        return imageCache
    }
}

struct PhotoPiceker<Content : View> : View {

    @State private var selectedItem: PhotosPickerItem?

    let pickerType: PHPickerFilter
    @ViewBuilder let content: @Sendable () -> Content
    let imagePicked: @Sendable (URL, _ isVideo: Bool) -> Unit
    var body: some View {

        PhotosPicker(
            selection: $selectedItem,
            matching: pickerType
        ) {
            //ImageAsset(icon: "upload", tint: .white).frame(width: 45, height: 45).padding(5)
            content()
        }.onChange(selectedItem, forChangePhoto(imagePicked))
    }
}

struct DataPicker : View {

    @Inject private var theme: Theme
    
    let isDateTimeError: Bool
    let dataPicked: (Int64) -> ()

    var body: some View {
        VStack {
            DatePicker("Enter Timeline Date", selection: Binding(get: {
                Date.now
            }, set: { it in
                dataPicked(Int64(it.timeIntervalSince1970) * 1000)
            }), displayedComponents: [.date, .hourAndMinute])
            .labelsHidden()
            .frame(maxHeight: 400)
            .foregroundColor(isDateTimeError ? theme.error : theme.textColor
            )
        }.frame(maxHeight: 400).padding(5).background(
            RoundedRectangle(cornerRadius: 5).stroke(isDateTimeError ? theme.error : Color.clear, lineWidth: 1)
        )
    }
}
