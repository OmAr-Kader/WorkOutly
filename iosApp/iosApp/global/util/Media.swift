import Foundation
import _PhotosUI_SwiftUI
import PhotosUI
import SwiftUI

func getURL(
    item: PhotosPickerItem,
    invoke: @escaping @Sendable (URL, _ isVideo: Bool) -> Void,
    failed: @escaping @Sendable () -> Void
) {
    // Step 1: Load as Data object.
    item.loadTransferable(type: Data.self) { result in
        switch result {
        case .success(let data):
            if let contentType = item.supportedContentTypes.first {
                logger("imageUri", contentType.identifier)
                // Step 2: make the URL file name and a get a file extention.
                let url = getDocumentsDirectory().appendingPathComponent("\(UUID().uuidString).\(contentType.preferredFilenameExtension ?? "")")
                if let data = data {
                    do {
                        // Step 3: write to temp App file directory and return in completionHandler
                        try data.write(to: url)
                        invoke(url, contentType == .video)
                    } catch {
                        logger("getURL",error.localizedDescription)
                        failed()
                    }
                }
            }
        case .failure(let failure):
            logger("getURL", failure.localizedDescription)
            failed()
        }
    }
}

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
            getURL(item: newIt) { url, isVideo in
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
}

/// from: https://www.hackingwithswift.com/books/ios-swiftui/writing-data-to-the-documents-directory
func getDocumentsDirectory() -> URL {
    // find all possible documents directories for this user
    let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)

    // just send back the first one, which ought to be the only one
    return paths[0]
}
