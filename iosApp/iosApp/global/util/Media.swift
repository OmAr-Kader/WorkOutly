import Foundation
import _PhotosUI_SwiftUI
import PhotosUI
import SwiftUI
import AVFoundation

@BackgroundActor
func getByteArraySafe(from url: URL) -> [UInt8]? {
    guard FileManager.default.fileExists(atPath: url.path) else {
        print("File does not exist at \(url.path)")
        return nil
    }

    do {
        let fileData = try Data(contentsOf: url)
        return [UInt8](fileData)
    } catch {
        print("Error reading file: \(error.localizedDescription)")
        return nil
    }
}

func forChangePhoto(_ image: @escaping @Sendable @MainActor (URL, Bool) -> Void) -> ((PhotosPickerItem?) -> Void) {
    return { newIt in
        if let newIt = newIt {
            newIt.getURL { url, isVideo in
                TaskMainSwitcher {
                    image(url, isVideo)
                }
                logger("imageUri", String(isVideo))
                logger("imageUri", url.absoluteString)
            } failed: {
                
            }
        }
    }
}


func loadThumbnail(videoURL: URL, invoke: @Sendable @escaping @MainActor (UIImage) -> Unit) {
    DispatchQueue.global(qos: .background).async {
        let asset = AVAsset(url: videoURL)
        let imageGenerator = AVAssetImageGenerator(asset: asset)
        imageGenerator.appliesPreferredTrackTransform = true
        
        do {
            let time = CMTime(seconds: 1, preferredTimescale: 60) // Get the thumbnail at 1-second mark
            let cgImage = try imageGenerator.copyCGImage(at: time, actualTime: nil)
            let uiImage = UIImage(cgImage: cgImage)
            DispatchQueue.main.async {
                invoke(uiImage)
            }
        } catch {
            print("Error generating thumbnail: \(error.localizedDescription)")
        }
    }
}

extension PhotosPickerItem {
    
    func loadImageFromUrl(invoke: @Sendable @escaping (Image?) -> Unit) -> Progress {
        return loadTransferable(type: Image.self) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let image?):
                    invoke(image)
                    // Handle the success case with the image.
                case .success(nil):
                    invoke(nil)
                    // Handle the success case with an empty value.
                case .failure(let error):
                    loggerError("loadImageFromUrl", error.localizedDescription)
                    // Handle the failure case with the provided error.
                }
            }
        }
    }
    
    func getURL(
        invoke: @escaping @Sendable @MainActor (URL, _ isVideo: Bool) -> Void,
        failed: @escaping @Sendable @MainActor () -> Void
    ) {
        // Step 1: Load as Data object.
        loadTransferable(type: Data.self) { result in
            switch result {
            case .success(let data):
                if let contentType = supportedContentTypes.first {
                    logger("imageUri", contentType.identifier)
                    // Step 2: make the URL file name and a get a file extention.
                    let url = getDocumentsDirectory().appendingPathComponent("\(UUID().uuidString).\(contentType.preferredFilenameExtension ?? "")")
                    if let data = data {
                        do {
                            // Step 3: write to temp App file directory and return in completionHandler
                            try data.write(to: url)
                            TaskMainSwitcher {
                                invoke(url, contentType == .video)
                            }
                        } catch {
                            logger("getURL",error.localizedDescription)
                            TaskMainSwitcher {
                                failed()
                            }
                        }
                    }
                }
            case .failure(let failure):
                logger("getURL", failure.localizedDescription)
                TaskMainSwitcher {
                    failed()
                }
            }
        }
    }
}

/// from: https://www.hackingwithswift.com/books/ios-swiftui/writing-data-to-the-documents-directory
func getDocumentsDirectory() -> URL {
    // find all possible documents directories for this user
    let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)

    // just send back the first one, which ought to be the only one
    return paths[0]
}

@BackgroundActor
func getVideoDuration(from url: URL) async -> Int64? {
    let asset = AVAsset(url: url)
    guard let duration = try? await asset.load(.duration) else {
        return nil
    }
    let durationInSeconds = CMTimeGetSeconds(duration)
    guard durationInSeconds.isFinite else {
        return nil
    }
    
    // Convert seconds to milliseconds
    let durationInMilliseconds = Int64(durationInSeconds * 1000)
    return durationInMilliseconds
}
