//
//  CreateExerciseScreen.swift
//  iosApp
//
//  Created by OmAr on 01/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct CreateExerciseScreen : View {
    
    let screenConfig: @MainActor (Screen) -> (any ScreenConfig)?
    let backPress: @MainActor () -> Unit
    @MainActor
    @StateObject private var obs: CreateExerciseObserve = CreateExerciseObserve()
    
    @MainActor
    @State private var toast: Toast? = nil
    
    @Inject
    private var theme: Theme
    var body: some View {
        let state = obs.state
        ZStack {
            
        }
    }
}
