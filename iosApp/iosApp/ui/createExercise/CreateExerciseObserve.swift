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
 
    
    struct State {
        
        var exercise: Exercise = Exercise()
        var videoUrl : (URL, String)? = nil
        var slide: Int = 0
        var isProcess: Bool = false
        
        @MainActor
        mutating func copy(updates: @MainActor (inout Self) -> Self) -> Self { // Only helpful For struct or class with nil values
            self = updates(&self)
            return self
        }

    }

    deinit {
        back = nil
    }
    
}

@BackgroundActor
struct CreateExerciseObserveBack {

    private let project: Project
        
    @MainActor
    private var scope = Scope()
    
    @BackgroundActor
    private var scopeBack = Scope()

    init(project: Project) {
        self.project = project
    }
}
