//
//  ExerciseObserve.swift
//  iosApp
//
//  Created by OmAr on 21/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
class ExerciseObserve : ObservableObject {
    
    @MainActor
    private var scope = Scope()
    
    @MainActor
    private var back: ExerciseObserveBack? = nil
    
    @MainActor
    @Published var state = State()
    
    init() {
        @Inject
        var project: Project
        scope.launchMain {
            self.back = await ExerciseObserveBack(project: project)
        }
    }
    
    @MainActor
    func loadData(exercise: Exercise) {
        scope.launchMain {
            self.state = self.state.copy(exercise: exercise, isProcess: false)
            /*self.back!.loadData(id: exercise.id) { exercise in
                self.state = self.state.copy(exercise: exercise, isProcess: false)
            }*/
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
        var isProcess: Bool = true
        
        @MainActor
        mutating func copy(updates: @MainActor (inout Self) -> Self) -> Self { // Only helpful For struct or class with nil values
            self = updates(&self)
            return self
        }
        
        @MainActor
        mutating func copy(
            exercise: Exercise? = nil,
            isProcess: Bool? = nil
        ) -> Self {
            self.exercise = exercise ?? self.exercise
            self.isProcess = isProcess ?? self.isProcess
            return self
        }
    }

    deinit {
        back = nil
    }
    
}

@BackgroundActor
class ExerciseObserveBack {
    
    private let project: Project
    
    private let healthKit: HealthKitManager = HealthKitManager()
    
    @MainActor
    private var scope = Scope()
    
    @BackgroundActor
    private var scopeBack = Scope()

    init(project: Project) {
        self.project = project
    }
    
    @MainActor
    func loadData(id: String, invoke: @escaping @MainActor (Exercise) -> Unit) {
        scope.launchBack {
            if let exercise = try? await self.project.exercise.getExercise(id: id) {
                self.scopeBack.launchMain {
                    invoke(exercise)
                }
            }
        }
    }
    
}
