//
//  SessionObserve.swift
//  iosApp
//
//  Created by OmAr on 21/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
class SessionObserve : ObservableObject {
    
    @MainActor
    private var scope = Scope()
    
    @MainActor
    private var back: SessionObserveBack? = nil
    
    @MainActor
    @Published var state = State()
    
    init() {
        @Inject
        var project: Project
        scope.launchMain {
            self.back = await SessionObserveBack(project: project)
        }
    }
    
    func loadData(metric: FitnessMetric, days: Int) {
        //setIsProcess(true)
        scope.launchMain {
            switch metric.id {
            case ConstKt.STEPS:
                self.back!.loadStepsData(days: days) { it in
                    self.state = self.state.copy(session: metric, sessionHistories: it, isProcess: false)
                }
            default:
                break
            }
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
        
        var session: FitnessMetric = FitnessMetric()
        var sessionHistories: [FitnessHistoryMetric] = []
        var isProcess: Bool = true
        
        @MainActor
        mutating func copy(updates: @MainActor (inout Self) -> Self) -> Self { // Only helpful For struct or class with nil values
            self = updates(&self)
            return self
        }
        
        @MainActor
        mutating func copy(
            session: FitnessMetric? = nil,
            sessionHistories: [FitnessHistoryMetric]? = nil,
            isProcess: Bool? = nil
        ) -> Self {
            self.session = session ?? self.session
            self.sessionHistories = sessionHistories ?? self.sessionHistories
            self.isProcess = isProcess ?? self.isProcess
            return self
        }
    }

    deinit {
        back = nil
    }

}

@BackgroundActor
class SessionObserveBack : Scoper, Sendable {
    
    private let project: Project
    
    private let healthKit: HealthKitManager = HealthKitManager()

    init(project: Project) {
        self.project = project
    }
    
    @MainActor
    func loadStepsData(days: Int, invoke: @Sendable @escaping @MainActor ([FitnessHistoryMetric]) -> Unit) {
        self.launchBack {
            self.healthKit.stepHistoryHKHealth(days: days) { list in
                let regenerated = ConverterKt.regenerateHistories(list)
                self.launchMain {
                    invoke(regenerated)
                }
            }
        }
    }
}
