//
//  HomeObserver.swift
//  iosApp
//
//  Created by OmAr on 16/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Combine
import shared

@MainActor
class HomeObserve : ObservableObject {
    
    @MainActor
    private var scope = Scope()
    
    @MainActor
    private var back: HomeObserveBack? = nil
    
    @MainActor
    @Published var state = State()
    
    init() {
        @Inject
        var project: Project
        scope.launchMain {
            self.back = await HomeObserveBack(project: project)
        }
    }
    
    func loadData(userPref: UserPref, isDarkMode: Bool, failed: @escaping @Sendable @MainActor () -> Unit) {
        //setIsProcess(true)
        scope.launchMain {
            self.back!.loadData(userPref: userPref, isDarkMode: isDarkMode) { mers in
                self.state = self.state.copy(metrics: mers, isProcess: false)
            } failed: {
                self.setMainProcess(false)
                failed()
            }

        }
    }
    
    func setIsLiveVisible(it: Bool) {
        self.state = self.state.copy(isProcess: it)
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
        
        var metrics: [FitnessMetric] = []
        var exercises: [Exercise] = []
        var messages: [Message] = []
        var chatText: String = ""
        var isLiveVisible: Bool = false
        var isPickerVisible: Bool = false
        var isProcess: Bool = true
        
        @MainActor
        mutating func copy(updates: @MainActor (inout Self) -> Self) -> Self { // Only helpful For struct or class with nil values
            self = updates(&self)
            return self
        }
        
        @MainActor
        mutating func copy(
            metrics: [FitnessMetric]? = nil,
            exercises: [Exercise]? = nil,
            messages: [Message]? = nil,
            chatText: String? = nil,
            isLiveVisible: Bool? = nil,
            isPickerVisible: Bool? = nil,
            isProcess: Bool? = nil
        ) -> Self {
            self.metrics = metrics ?? self.metrics
            self.exercises = exercises ?? self.exercises
            self.messages = messages ?? self.messages
            self.chatText = chatText ?? self.chatText
            self.isLiveVisible = isLiveVisible ?? self.isLiveVisible
            self.isPickerVisible = isPickerVisible ?? self.isPickerVisible
            self.isProcess = isProcess ?? self.isProcess
            return self
        }
    }

    deinit {
        back = nil
    }
}

@BackgroundActor
class HomeObserveBack {
    
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
    func loadData(userPref: UserPref, isDarkMode: Bool, metrics: @escaping @Sendable @MainActor ([FitnessMetric]) -> Unit, failed: @escaping @Sendable @MainActor () -> Unit) {
        scope.launchBack {
            self.healthKit.requestPermissions {
                TaskBackSwitcher {
                    self.healthKit.stepCountHKHealth(days: 3) { steps in
                        TaskBackSwitcher {
                            self.healthKit.distanceWalkingRunningHKHealth(days: 3) { distance in
                                TaskBackSwitcher {
                                    self.healthKit.activeEnergyBurnedHKHealth(days: 3) { calBurned in
                                        TaskBackSwitcher {
                                            self.healthKit.basalEnergyBurnedHKHealth(days: 3) { metabolicRate in
                                                TaskBackSwitcher {
                                                    self.healthKit.heartRateHKHealth(days: 3) { heartRate in
                                                        TaskBackSwitcher {
                                                            self.healthKit.sleepAnalysisHKHealth(days: 3) { sleep in
                                                                let stepsInt = Int64(steps ?? 0)
                                                                let distanceInt = Int64(distance ?? 0)
                                                                let calBurnedInt = Int64(calBurned ?? 0)
                                                                let metabolicRateInt = Int64(metabolicRate ?? 0)
                                                                let heartRateInt = Int64(heartRate ?? 0)
                                                                let sleepInt = Int64(sleep ?? 0)
                                                                    let mers = [
                                                                        FitnessMetric(
                                                                            id: ConstKt.STEPS,
                                                                            title: "Steps",
                                                                            iconColor: isDarkMode ? UIColor(29, 108, 245).argb : UIColor(29, 108, 245).darken(by: 0.3).argb,
                                                                            value: stepsInt,
                                                                            valueUnit: ""
                                                                        ),
                                                                        FitnessMetric(
                                                                            id: ConstKt.DISTANCE,
                                                                            title: "Distance",
                                                                            iconColor: isDarkMode ? UIColor(0, 255, 0).argb : UIColor(0, 255, 0).darken(by: 0.3).argb,
                                                                            value: distanceInt,
                                                                            valueUnit: " m"
                                                                        ),
                                                                        FitnessMetric(
                                                                            id: ConstKt.CALORIES_BURNED,
                                                                            title: "Calories",
                                                                            iconColor: isDarkMode ? UIColor(255, 255, 0).argb : UIColor(255, 255, 0).darken(by: 0.3).argb,
                                                                            value: calBurnedInt,
                                                                            valueUnit: " kcal"
                                                                        ),
                                                                        FitnessMetric(
                                                                            id: ConstKt.METABOLIC_RATE,
                                                                            title: "Metabolic Rate",
                                                                            iconColor: isDarkMode ? UIColor(245, 73, 29).argb : UIColor(245, 73, 29).darken(by: 0.3).argb,
                                                                            value: metabolicRateInt,
                                                                            valueUnit: " kcal/day"
                                                                        ),
                                                                        FitnessMetric(
                                                                            id: ConstKt.HEART_RATE,
                                                                            title: "Heart Rate",
                                                                            iconColor: isDarkMode ? UIColor(255, 0, 0).argb : UIColor(255, 0, 0).darken(by: 0.3).argb,
                                                                            value: heartRateInt,
                                                                            valueUnit: " bpm"
                                                                        ),
                                                                        FitnessMetric(
                                                                            id: ConstKt.SLEEP,
                                                                            title: "Sleep",
                                                                            iconColor: isDarkMode ? UIColor(28, 199, 139).argb : UIColor(28, 199, 139).darken(by: 0.3).argb,
                                                                            value: sleepInt,
                                                                            valueUnit: ""
                                                                        )
                                                                    ]
                                                                TaskMainSwitcher {
                                                                    metrics(mers)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } failed: {
                TaskMainSwitcher {
                    failed()
                }
            }

        }
    }
}
