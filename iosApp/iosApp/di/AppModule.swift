//
//  AppModule.swift
//  iosApp
//
//  Created by OmAr on 02/10/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Swinject
import shared

//chmod +x gradlew
//git rm -r --cached iosApp/iosApp/global/ui/Views.swift -f
//https://github.com/Swinject/Swinject
//https://github.com/ryohey/Zoomable.git

func buildContainer() -> Container {
    let container = Container()
    let theme = Theme(isDarkMode: UITraitCollection.current.userInterfaceStyle.isDarkMode)
    
    container.register(Project.self) { _  in
        return DIHelper().project
    }.inObjectScope(.container)
    container.register(Theme.self) { _  in
        return theme
    }.inObjectScope(.container)
    return container
}

func initModule() async {
#if DEBUG
    try? await IosAppModuleKt.doInitKoin(isDebugMode: true)
#else
    try? await IosAppModuleKt.doInitKoin(isDebugMode: false)
#endif
}

class Resolver {
    @MainActor static let shared = Resolver()
    
    private var container = buildContainer()
    
    func resolve<T>(_ type: T.Type) -> T {
        container.resolve(T.self)!
    }
}

@propertyWrapper
struct Inject<I> {
    let wrappedValue: I
    
    @MainActor
    init() {
        self.wrappedValue = Resolver.shared.resolve(I.self)
    }
}

/**
 //
 //  AppModule.swift
 //  iosApp
 //
 //  Created by OmAr on 02/10/2024.
 //  Copyright © 2024 orgName. All rights reserved.
 //

 import Foundation
 import SwiftUI
 import Swinject
 import shared

 //chmod +x gradlew
 //git rm -r --cached iosApp/iosApp/global/ui/Views.swift -f
 //https://github.com/Swinject/Swinject
 //https://github.com/googlemaps/ios-maps-sdk

 extension Container : @unchecked @retroactive Sendable {
     
 }
 func buildContainer() -> Container {
     let container = Container()
     let theme = Theme(isDarkMode: UITraitCollection.current.userInterfaceStyle.isDarkMode)
     
     container.register(Project.self) { _  in
         return DIHelper().project
     }.inObjectScope(.container)
     container.register(Theme.self) { _  in
         return theme
     }.inObjectScope(.container)
     return container
 }

 func initModule() async {
 #if DEBUG
     try? await IosAppModuleKt.doInitKoin(isDebugMode: true)
 #else
     try? await IosAppModuleKt.doInitKoin(isDebugMode: false)
 #endif
 }

 final class Resolver : Sendable {
     static let shared = Resolver()
     
     private let container = buildContainer()
     
     func resolve<T>(_ type: T.Type) -> T {
         container.resolve(T.self)!
     }
 }

 @propertyWrapper
 struct Inject<I> {
     let wrappedValue: I
     
     init() {
         self.wrappedValue = Resolver.shared.resolve(I.self)
     }
 }

 
 */
