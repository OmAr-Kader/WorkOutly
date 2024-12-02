//
//  CreateExerciseObserve.swift
//  iosApp
//
//  Created by OmAr on 01/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Combine
import shared

@MainActor
class CreateExerciseObserve : ObservableObject {
    
    @MainActor
    private var scope = Scope()
    
    @MainActor
    private var back: CreateExerciseObserveBack? = nil
    
    @MainActor
    @Published var state = State()
    
    init() {
        @Inject
        var project: Project
        scope.launchMain {
            self.back = await CreateExerciseObserveBack(project: project)
        }
    }
    
    @MainActor
    func upload(invoke: @Sendable @escaping @MainActor () -> Unit, failed: @Sendable @escaping @MainActor () -> Unit) {
        guard let videoUrl = self.state.videoUrl else {
            failed()
            return
        }
        setIsProcess(true)
        scope.launchMain {
            self.back!.upload(exercise: self.state.exercise, videoUrl: videoUrl) {
                //self.setIsProcess(false); invoke()
                invoke()
            } failed: {
                self.setIsProcess(false); failed()
            }
        }
    }
    
    func setTitle(_ it: String) {
        scope.launchMain {
            self.state = self.state.copy(exercise: self.state.exercise.copy(title: it.replacingOccurrences(of: "\n", with: " ")))
            
        }
    }
    
    func setDescription(_ it: String) {
        scope.launchMain {
            self.state = self.state.copy(exercise: self.state.exercise.copy(desc: it))
        }
    }
    
    func setCato(_ it: String) {
        scope.launchMain {
            self.state = self.state.copy(exercise: self.state.exercise.copy(cato: it))
        }
    }
 
    func setVideo(_ videoUrl: URL) {
        logger(videoUrl.absoluteString)
        scope.launchMain {
            self.state = self.state.copy(videoUrl: videoUrl)
        }
    }
    
    func setSlide(_ slide: Int) {
        scope.launchMain {
            self.state = self.state.copy(slide: slide)
        }
    }
    
    private func setIsProcess(_ isProcess: Bool) {
        scope.launchMain {
            self.state = self.state.copy(isProcess: isProcess)
        }
    }
       
    @MainActor func setMainProcess(_ isProcess: Bool) {
        self.state = self.state.copy(isProcess: isProcess)
    }
    
    struct State {
        
        var exercise: Exercise = Exercise()
        var videoUrl : URL? = nil
        var slide: Int = 0
        var isProcess: Bool = false
        
        @MainActor
        mutating func copy(updates: @MainActor (inout Self) -> Self) -> Self { // Only helpful For struct or class with nil values
            self = updates(&self)
            return self
        }

        @MainActor
        mutating func copy(
            exercise: Exercise? = nil,
            videoUrl: URL? = nil,
            slide: Int? = nil,
            isProcess: Bool? = nil
        ) -> Self {
            self.exercise = exercise ?? self.exercise
            self.videoUrl = videoUrl ?? self.videoUrl
            self.slide = slide ?? self.slide
            self.isProcess = isProcess ?? self.isProcess
            return self
        }
    }

    deinit {
        back = nil
    }
    
}

@BackgroundActor
class CreateExerciseObserveBack {

    private let project: Project
        
    @MainActor
    private var scope = Scope()
    
    @BackgroundActor
    private var scopeBack = Scope()

    init(project: Project) {
        self.project = project
    }
    
    @MainActor
    func upload(exercise: Exercise, videoUrl: URL, invoke: @Sendable @escaping @MainActor () -> Unit, failed: @Sendable @escaping @MainActor () -> Unit) {
        scope.launchBack {
            let generateUniqueId = ConverterKt.generateUniqueId
            guard let cloudUrl = await self.uploadExerciseVideo(url: videoUrl, uuid: generateUniqueId) else {
                self.scopeBack.launchMain { failed() } ; return
            }
            guard let length = await getVideoDuration(from: videoUrl) else {
                self.scopeBack.launchMain { failed() } ; return
            }
            let cloudExercise = exercise.copy(
                id: generateUniqueId,
                date: DateKt.dateNow,
                videoUrl: cloudUrl,
                length: length,
                views: 0,
                liker: []
            )
            guard (try? await self.project.exercise.createExercise(exercise: cloudExercise)) != nil else {
                self.scopeBack.launchMain { failed() } ; return
            }
            self.scopeBack.launchMain { invoke() }
        }
    }
    
    @BackgroundActor
    private func uploadExerciseVideo(url: URL, uuid: String) async -> String? {
        guard let it = getByteArraySafe(from: url) else {
            return nil
        }
        let ext = "." + url.pathExtension.lowercased()
        let mediaBytes = convertToKotlinByteArray(from: it)
        return try? await StorageKt.uploadS3VideoExercise(imageBytes: mediaBytes, fileName: uuid + String(DateKt.dateNowMills) + ext)
    }
}
