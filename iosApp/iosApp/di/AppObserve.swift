//
//  AppObserve.swift
//  iosApp
//
//  Created by OmAr on 02/10/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Combine
import shared

@MainActor
class AppObserve : ObservableObject {

    @MainActor
    private var scope = Scope()
    
    @MainActor
    private var back: AppObserveBack? = nil

    @MainActor
    @Published var navigationPath = NavigationPath()

    @MainActor
    @Published var state = State()

    init() {
        @Inject
        var project: Project
        scope.launchMain {
            self.back = await AppObserveBack(project: project)
        }
    }
    
    var navigateHome: (Screen) -> Unit {
        return { screen in
            self.state = self.state.copy(homeScreen: screen)
            return ()
        }
    }
    
    func navigateHomeNoAnimation(_ screen: Screen) -> Unit {
        self.state = self.state.copy(homeScreen: screen)
    }
    
    func navigateTo(_ screen: Screen) {
        self.navigationPath.append(screen)
    }
    
    func backPress() {
        if !self.navigationPath.isEmpty {
            self.navigationPath.removeLast()
        }
    }
    
    func fetchUser(invoke: @MainActor @escaping (UserPref?) -> Unit) {
        scope.launchMain {
            self.back!.fetchUser { user in
                self.state = self.state.copy(userPref: user)
                invoke(user)
            }
        }
    }

    func findUser(invoke: @MainActor @escaping (UserPref?) -> Unit) {
        scope.launchMain {
            self.back!.findUser { user in
                self.state = self.state.copy(userPref: user)
                invoke(user)
            }
        }
    }
    
    func updateUserPref(userPref: UserPref) {
        state = state.copy(userPref: userPref)
    }
    
    func findUserLive(invoke: @MainActor @escaping (UserPref?) -> Unit) {
        scope.launchMain {
            self.back!.findUserLive { user in
                if let user {
                    invoke(user)
                    self.state = self.state.copy(userPref: user)
                } else {
                    invoke(nil)
                }
            } statusChanged: { status in
                self.state = self.state.copy(sessionStatus: status)
            }
        }
    }
    
    func findPrefStringMainMain(
        _ key: String,
        value: @MainActor @escaping (String?) -> Unit
    ) {
        scope.launchMain {
            self.back!.findPrefString(key, value: value)
        }
    }
    
    func findPrefStringMainBack(
        _ key: String,
        value: @BackgroundActor @escaping (String?) -> Unit
    ) {
        scope.launchMain {
            self.back!.findPrefStringMainBack(key, value: value)
        }
    }
    
    func updatePref(
        pref: PreferenceData
    ) {
        scope.launchMain {
            self.back!.updatePref(pref: pref)
        }
    }
    
    func findArg(screen: Screen) -> (any ScreenConfig)? {
        return state.argOf(screen)
    }
    
    func writeArguments(_ route: Screen,_ screenConfig: ScreenConfig) {
        state = state.copy(route, screenConfig)
    }
    
    func signOut(_ invoke: @escaping @MainActor () -> Unit,_ failed: @escaping @MainActor () -> Unit) {
        scope.launchMain {
            self.back!.signOut(invoke, failed)
        }
    }

    
    struct State {
        
        private(set) var homeScreen: Screen = .AUTH_SCREEN_ROUTE
        private(set) var sessionStatus: Int = 0
        private(set) var userPref: UserPref? = nil
        private(set) var args = [Screen : any ScreenConfig]()
        
        @MainActor
        mutating func copy(
            homeScreen: Screen? = nil,
            sessionStatus: Int? = nil,
            userPref: UserPref? = nil,
            args: [Screen : any ScreenConfig]? = nil
        ) -> Self {
            self.homeScreen = homeScreen ?? self.homeScreen
            self.sessionStatus = sessionStatus ?? self.sessionStatus
            self.userPref = userPref ?? self.userPref
            self.args = args ?? self.args
            return self
        }
        
        mutating func argOf(_ screen: Screen) -> (any ScreenConfig)? {
            return args.first { (key: Screen, value: any ScreenConfig) in
                key == screen
            }?.value
        }
        
        mutating func copy<T : ScreenConfig>(_ screen: Screen, _ screenConfig: T) -> Self {
            args[screen] = screenConfig
            return self
        }
    }

    deinit {
        back = nil
    }
}


@BackgroundActor
class AppObserveBack {
    
    private let project: Project
    private var preferences: [PreferenceData] = []
    private var prefsTask: Task<Void, Error>? = nil
    private var job: Task<Void, Error>? = nil
    private var userJob: Task<Void, Error>? = nil

    @MainActor
    private var scope = Scope()
    
    @BackgroundActor
    private var scopeBack = Scope()

    init(project: Project) {
        self.project = project
    }

    @MainActor
    func fetchUser(invoke: @MainActor @escaping (UserPref?) -> Unit) {
        scope.launchBack {
            self.findPrefStringBack(ConstKt.PREF_ID) { id in
                self.findPrefStringBack(ConstKt.PREF_NAME) { name in
                    self.findPrefStringBack(ConstKt.PREF_PROFILE_IMAGE) { profileImage in
                        self.scopeBack.launchBack {
                            guard let user = try? await AuthKt.userInfo()?.copy(id: id ?? "", name: name ?? "", profilePicture: profileImage ?? "") else {
                                self.scopeBack.launchMain {
                                    invoke(nil)
                                }
                                return
                            }
                            self.scopeBack.launchMain {
                                invoke(user)
                            }
                        }
                    }
                }
            }
        }
    }
    
    @MainActor
    func findUser(invoke: @MainActor @escaping (UserPref?) -> Unit) {
        scope.launchBack {
            self.findPrefStringBack(ConstKt.PREF_ID) { id in
                self.findPrefStringBack(ConstKt.PREF_NAME) { name in
                    self.findPrefStringBack(ConstKt.PREF_PROFILE_IMAGE) { profileImage in
                        self.scopeBack.launchBack {
                            guard let user = try? await AuthKt.userInfo()?.copy(id: id ?? "", name: name ?? "", profilePicture: profileImage ?? "") else {
                                self.scopeBack.launchMain {
                                    invoke(nil)
                                }
                                return
                            }
                            self.scopeBack.launchMain {
                                invoke(user)
                            }
                        }
                    }
                }
            }
        }
    }
    
    @MainActor
    func findUserLive(invoke: @escaping @MainActor (UserPref?) -> Unit, statusChanged: @escaping @MainActor (Int) -> Unit) {
        TaskBackSwitcher {
            self.userJob = self.scopeBack.launchBack {
                do {
                    nonisolated func invoker(userPref: UserPref?, status: KotlinInt) -> Unit {
                        TaskBackSwitcher {
                            self.scopeBack.launchBack {
                                guard let userPref = userPref else {
                                    if status == -1 {
                                        self.userJob?.cancel()
                                        self.scopeBack.launchMain {
                                            invoke(nil)
                                        }
                                    }
                                    self.scopeBack.launchMain {
                                        statusChanged(status.intValue)
                                    }
                                    return
                                }
                                self.findPrefStringBack(ConstKt.PREF_ID) { id in
                                    self.findPrefStringBack(ConstKt.PREF_NAME) { name in
                                        self.findPrefStringBack(ConstKt.PREF_PROFILE_IMAGE) { profileImage in
                                            self.scopeBack.launchBack {
                                                let user = userPref.copy(id: id ?? "", name: name ?? "", profilePicture: profileImage ?? "")
                                                self.userJob?.cancel()
                                                self.scopeBack.launchMain {
                                                    invoke(user)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    try await AuthKt.fetchSupaBaseUser(invoke: invoker)
                } catch {
                    logger("findUserLive", error.localizedDescription)
                    self.userJob?.cancel()
                    self.scopeBack.launchMain {
                        invoke(nil)
                    }
                }
            }
        }
    }
    
    @MainActor
    func findPrefStringMainBack(
        _ key: String,
        value: @BackgroundActor @escaping (String?) -> Unit
    ) {
        scope.launchBack {
            self.findPrefStringBack(key, value: value)
        }
    }
    
    @BackgroundActor
    func findPrefStringBack(
        _ key: String,
        value: @BackgroundActor @escaping (String?) -> Unit
    ) {
        if (preferences.isEmpty) {
            self.inti { prefs in
                value(prefs.first { it1 in it1.keyString == key }?.value)
            }
        } else {
            scopeBack.launchBack {
                value(self.preferences.first { it in it.keyString == key }?.value)
            }
        }
    }
    
    @MainActor
    func findPrefString(
        _ key: String,
        value: @MainActor @escaping (String?) -> Unit
    ) {
        scope.launchBack {
            if (self.preferences.isEmpty) {
                self.inti { prefs in
                    let pref = prefs.first { it1 in it1.keyString == key }?.value
                    self.scopeBack.launchMain {
                        value(pref)
                    }
                }
            } else {
                let pref = self.preferences.first { it in it.keyString == key }?.value
                self.scopeBack.launchMain {
                    value(pref)
                }
            }
        }
    }
    
    @BackgroundActor
    private func inti(invoke: @BackgroundActor @escaping ([PreferenceData]) -> Unit) {
        scopeBack.launchBack {
            do {
                nonisolated func invoker(prefs: [PreferenceData]) {
                    TaskBackSwitcher {
                        self.preferences = prefs
                        invoke(prefs)
                    }
                }
                try await self.project.pref.prefs(invoke: invoker)
            } catch {
                invoke([])
            }
        }
    }
    
    @MainActor
    func updatePref(pref: PreferenceData) {
        scope.launchBack {
            do {
                try await self.project.pref.updatePref(pref: pref, newValue: pref.value)
                await self.updatePrefData(pref: pref)
            } catch {}
        }
    }
    
    @BackgroundActor
    private func updatePrefData(pref: PreferenceData) async {
        if let it = self.preferences.firstIndex(where: { it in
            it.keyString == pref.keyString
        }) {
            self.preferences[it] = pref
        } else {
            self.preferences += [pref]
        }
    }
    
    @MainActor
    func signOut(_ invoke: @escaping @MainActor () -> Unit,_ failed: @escaping @MainActor () -> Unit) {
        scope.launchBack {
            guard let result = try? await self.project.pref.deletePrefAll() else {
                self.scopeBack.launchMain {
                    failed()
                }
                return
            }
            if result.int32Value == RealmKt.CLOUD_SUCCESS {
                self.scopeBack.launchMain {
                    invoke()
                }
            } else {
                self.scopeBack.launchMain {
                    failed()
                }
            }
        }
    }

    
    private func cancelSession() {
        prefsTask?.cancel()
        prefsTask = nil
    }
    
    deinit {
        prefsTask?.cancel()
        prefsTask = nil
        userJob?.cancel()
        userJob = nil
        scope.deInit()
    }

}
