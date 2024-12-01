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
    
    func loadData(userPref: UserPref, days: Int, isDarkMode: Bool, failed: @escaping @Sendable @MainActor () -> Unit) {
        scope.launchMain {
            self.back!.loadData(userPref: userPref, days: days, isDarkMode: isDarkMode) { mers, messages, exercises in
                self.state = self.state.copy(metrics: mers, exercises: exercises, messages: messages, currentSession: DateKt.dateNowOnlyHour, days: days, isProcess: false)
                //self.observeSessionChanged(id: userPref.id)
                //self.startMessagesObserving(userId: userPref.id)
            } failed: {
                self.setMainProcess(false)
                failed()
            }
        }
    }
    
    func observeSessionChanged(id: String) {
        scope.launchMain {
            self.back!.observeSessionChanged(id: id) { messages in
                self.state = self.state.copy(
                    messages: messages,
                    currentSession: DateKt.dateNowOnlyHour,
                    isProcess: false
                )
            }
        }
    }
    
    @MainActor
    func startMessagesObserving(userId: String) {
        self.scope.launchMain {
            self.back!.startMessagesObserving(userId: userId) { new in
                TaskMainSwitcher {
                    self.scope.launchMain {
                        let messages = ConverterKt.messagesSort(self.state.messages + [new.copy(isFromCurrentUser: new.userId == userId)])
                        self.state = self.state.copy(messages: messages)
                    }
                }
            } deleted: { delete in
                TaskMainSwitcher {
                    self.scope.launchMain {
                        let refreshed = ConverterKt.messagesSort(self.state.messages.filter { it in it.id != delete })
                        self.state = self.state.copy(messages: refreshed)
                    }
                }
            }
        }
    }
    
    private func reLoadMetrics(userPref: UserPref, days: Int, isDarkMode: Bool, failed: @escaping @Sendable @MainActor () -> Unit) {
        //setIsProcess(true)
        scope.launchMain {
            self.back!.reLoadMetrics(days: days, isDarkMode: isDarkMode) { mers in
                self.state = self.state.copy(metrics: mers, isProcess: false)
            } failed: {
                self.setMainProcess(false)
                failed()
            }
        }
    }
    
    func setFilterDays(userPref: UserPref, isDarkMode: Bool, it: Int) {
        scope.launchMain {
            self.back!.updatePref(key: ConstKt.PREF_DAYS_COUNT, value: String(it))
        }
        reLoadMetrics(userPref: userPref, days: it, isDarkMode: isDarkMode) {
            
        }
        self.state = self.state.copy(days: it)
    }
    
    func setSortBy(it: Int) {
        scope.launchMain {
            self.back!.updatePref(key: ConstKt.PREF_SORT_BY, value: String(it))
        }
        // @OmAr-Kader IMPLEMENTATION
        self.state = self.state.copy(sortBy: it)
    }
    
    @MainActor
    func setFile(url: URL, isVideo: Bool, user: UserPref, failed: @escaping @Sendable @MainActor () -> Unit) {
        setIsProcess(true)
        scope.launchMain {
            self.back!.setFile(url: url, isVideo: isVideo, user: user) { new in
                let messages = ConverterKt.messagesSort(self.state.messages + [new.copy(isFromCurrentUser: true)])
                self.state = self.state.copy(messages: messages, isProcess: false)
            } failed: {
                self.setIsProcess(false)
                failed()
                logger("getByteArrayFromUri", "null")
            }
        }
    }
    
    @MainActor
    func send(message: String, user: UserPref) {
        scope.launchMain {
            self.back!.send(message: message, user: user) { new in
                let messages = ConverterKt.messagesSort(self.state.messages + [new.copy(isFromCurrentUser: true)])
                self.state = self.state.copy(messages: messages, isProcess: false)
            }
        }
    }
    
    @MainActor
    func setChosenCato(cato: String) {
        let exercises = if (cato.isEmpty) {
            self.state.exercises // @OmAr-Kader sortBy
        } else {
            self.state.exercises.filter { it in it.cato == cato } // @OmAr-Kader sortBy
        }
        self.state = self.state.copy(displayExercises: exercises, chosenCato: cato)
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
        var displayExercises: [Exercise] = []
        var messages: [Message] = []
        var chosenCato: String = ""
        var currentSession: String = ""
        var days: Int = 3
        var sortBy: Int = 1
        var chatText: String = ""
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
            displayExercises: [Exercise]? = nil,
            messages: [Message]? = nil,
            chosenCato: String? = nil,
            currentSession: String? = nil,
            chatText: String? = nil,
            days: Int? = nil,
            sortBy: Int? = nil,
            isProcess: Bool? = nil
        ) -> Self {
            self.metrics = metrics ?? self.metrics
            self.exercises = exercises ?? self.exercises
            self.displayExercises = displayExercises ?? self.displayExercises
            self.messages = messages ?? self.messages
            self.chosenCato = chosenCato ?? self.chosenCato
            self.currentSession = currentSession ?? self.currentSession
            self.chatText = chatText ?? self.chatText
            self.days = days ?? self.days
            self.sortBy = sortBy ?? self.sortBy
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
    func loadData(userPref: UserPref, days: Int, isDarkMode: Bool, invoke: @escaping @Sendable @MainActor ([FitnessMetric], [Message], [Exercise]) -> Unit, failed: @escaping @Sendable @MainActor () -> Unit) {
        scope.launchBack {
            self.loadMetrics(days: days, isDarkMode: isDarkMode) { mers in
                self.scopeBack.launchBack {
                    let exercises = TempKt.tempExercises // @OmAr-Kader sortBy
                    let messages = ConverterKt.messagesSort(ConverterKt.messagesFilter(TempKt.messages, userId: userPref.id))
                    //let exercises = try? await self.project.exercise.fetchExercises() // @OmAr-Kader sortBy
                    //let fetched = try? await self.project.message.fetchMessageSession(session: DateKt.dateNowUTCMILLSOnlyTour)
                    //let messages = ConverterKt.messagesSort(ConverterKt.messagesFilter(fetched ?? [], userId: userPref.id))
                    TaskMainSwitcher {
                        invoke(mers, messages, exercises)
                    }
                }
            } failed: {
                failed()
            }
        }
    }
    
    @MainActor
    func reLoadMetrics(days: Int, isDarkMode: Bool, invoke: @escaping @Sendable @MainActor ([FitnessMetric]) -> Void, failed: @escaping @Sendable @MainActor () -> Unit) {
        scope.launchBack {
            self.loadMetrics(days: days, isDarkMode: isDarkMode) { mers in
                TaskMainSwitcher {
                    invoke(mers)
                }
            } failed: {
                failed()
            }
        }
    }
    
    func loadMetrics(days: Int, isDarkMode: Bool, invoke: @escaping @Sendable @BackgroundActor ([FitnessMetric]) -> Void, failed: @escaping @Sendable @MainActor () -> Unit) {
        self.healthKit.requestPermissions {
            TaskBackSwitcher {
                self.healthKit.stepCountHKHealth(days: days) { steps in
                    TaskBackSwitcher {
                        self.healthKit.distanceWalkingRunningHKHealth(days: days) { distance in
                            TaskBackSwitcher {
                                self.healthKit.activeEnergyBurnedHKHealth(days: days) { calBurned in
                                    TaskBackSwitcher {
                                        self.healthKit.basalEnergyBurnedHKHealth(days: days) { metabolicRate in
                                            TaskBackSwitcher {
                                                self.healthKit.heartRateHKHealth(days: days) { heartRate in
                                                    TaskBackSwitcher {
                                                        self.healthKit.sleepAnalysisHKHealth(days: days) { sleep in
                                                            TaskBackSwitcher {
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
                                                                invoke(mers)
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
    
    @MainActor
    func observeSessionChanged(id: String, invoke: @escaping @Sendable @MainActor ([Message]) -> Unit) {
        scope.launchBack {
            nonisolated func onTimeReached() {
                TaskBackSwitcher {
                    self.scopeBack.launchBack {
                        let fetched = try? await self.project.message.fetchMessageSession(session: DateKt.dateNowUTCMILLSOnlyTour)
                        let messages = ConverterKt.messagesSort(ConverterKt.messagesFilter(fetched ?? [], userId: id))
                        TaskMainSwitcher {
                            self.observeSessionChanged(id: id, invoke: invoke)
                            invoke(messages)
                        }
                    }
                }
            }
            try? await DateKt.observeNextHour(onTimeReached: onTimeReached)
        }
    }
    
    @MainActor
    func startMessagesObserving(userId: String, inserted: @escaping @Sendable (Message) -> Unit, deleted: @escaping @Sendable (String) -> Unit) {
        self.scope.launchBack {
            nonisolated func changed(new: Message) {
            }
            try? await self.project.message.fetchNewMessages(inserted: inserted, changed: changed, deleted: deleted)
        }
    }
    
    @MainActor
    func updatePref(key: String, value: String) {
        scope.launchBack {
            let _ = try? await self.project.pref.updatePref(pref: [PreferenceData(id: "", keyString: key, value: value)])
        }
    }
    
    @MainActor
    func setFile(url: URL, isVideo: Bool, user: UserPref, invoke: @escaping @Sendable @MainActor (Message) -> Unit, failed: @escaping @Sendable @MainActor () -> Unit) {
        scope.launchBack {
            guard let it = getByteArraySafe(from: url) else {
                self.scopeBack.launchMain {
                    failed()
                }
                return
            }
            let type = isVideo ? ConstKt.MSG_VID : ConstKt.MSG_IMG
            let ext = url.pathExtension.lowercased()
            let mediaBytes = self.convertToKotlinByteArray(from: it)
            guard let cloudUrl = try? await StorageKt.uploadS3Message(imageBytes: mediaBytes, fileName: user.id + String(DateKt.dateNowMills) + ext, type: type)else {
                self.scopeBack.launchMain {
                    failed()
                }
                return
            }
            logger("uploadS3Message", cloudUrl)
            guard let msg = await self.sendFile(fileUrl: cloudUrl, type: type, user: user) else {
                self.scopeBack.launchMain {
                    failed()
                }
                return
            }
            TaskMainSwitcher {
                invoke(msg)
            }
            //sendFile(fileUrl: url, type: type, userId: userId)
        }
    }
    
    @BackgroundActor
    private func sendFile(fileUrl: String, type: Int32, user: UserPref) async -> Message? {
        return try? await self.project.message.addMessage(
            message: Message(
                id: ConverterKt.generateUniqueId,
                userId: user.id,
                senderName: user.name,
                message: "",
                fileUrl: fileUrl,
                type: type,
                session: DateKt.dateNowUTCMILLSOnlyTour,
                date: DateKt.dateNow
            )
        )
    }
    
    @MainActor
    func send(message: String, user: UserPref, success: @escaping @MainActor (Message) -> Unit) {
        self.scope.launchBack {
            guard let msg = try? await self.project.message.addMessage(
                message: Message(
                    id: ConverterKt.generateUniqueId,
                    userId: user.id,
                    senderName: user.name,
                    message: message,
                    fileUrl: "",
                    type: ConstKt.MSG_TEXT,
                    session: DateKt.dateNowUTCMILLSOnlyTour,
                    date: DateKt.dateNow
                )
            ) else {
                return
            }
            TaskMainSwitcher {
                success(msg)
            }
        }
    }
    
    @BackgroundActor
    private func convertToKotlinByteArray(from byteArray: [UInt8]) -> KotlinByteArray {
        let kotlinByteArray = KotlinByteArray(size: Int32(byteArray.count))
        for (index, byte) in byteArray.enumerated() {
            kotlinByteArray.set(index: Int32(index), value: Int8(bitPattern: byte))
        }
        return kotlinByteArray
    }

}
