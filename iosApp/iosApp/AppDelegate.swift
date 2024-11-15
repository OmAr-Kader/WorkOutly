//
//  AppDelegate.swift
//  iosApp
//
//  Created by OmAr on 02/10/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class AppDelegate: NSObject, UIApplicationDelegate {

    private(set) var appSet: AppObserve! = nil
    
    
    var app: AppObserve {
        
        @Inject
        var project: Project
        guard let appSet else {
            let ap = AppObserve(project: project)
            self.appSet = ap
            return ap
        }
        return appSet
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        return true
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        appSet = nil
    }
}
