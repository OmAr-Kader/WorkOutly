//
//  ExerciseScreen.swift
//  iosApp
//
//  Created by OmAr on 21/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import AVKit
import shared

struct ExerciseScreen : View {
    
    let screenConfig: @MainActor (Screen) -> (any ScreenConfig)?
    let backPress: @MainActor () -> Unit
    
    @MainActor
    @StateObject private var obs: ExerciseObserve = ExerciseObserve()
    
    @State private var player : AVPlayer? = nil
    
    @State private var isPlayed: Bool = false
    
    @Inject
    private var theme: Theme
    
    @MainActor
    @State private var videoUrl: String?
    
    var body: some View {
        let state = obs.state
        ZStack(alignment: .topLeading) {
            VStack(alignment:.leading) {
                BackButton(tint: theme.textForGradientColor) {
                    backPress()
                }
                VStack {
                    ZStack {
                        if let player = self.player {
                            PlayerViewController(player: player)
                                .frame(height: 200)
                                .background(theme.background)
                                .cornerRadius(16)
                                .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4)
                        }
                        if !isPlayed {
                            ZStack {
                                FullZStack {}.background(UIColor(50, 50, 50).withAlphaComponent(0.5).toC)
                                ImageSystem(systemIcon: "play.fill", tint: Color.white).frame(width: 24, height: 24)
                            }.onTapGesture {
                                isPlayed.toggle()
                                player?.play()
                            }
                        }
                    }
                    .frame(height: 200)
                    .background(theme.background)
                    .cornerRadius(16)
                    .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4).onChange(state.exercise.videoUrl) { it in
                        guard let url = URL(string: it) else {
                            return
                        }
                        let player = AVPlayer(url: url)
                        self.player = player
                    }.onDisappear() {
                        player?.pause()
                    }
                }
                //.padding(8)
                .padding(.vertical, 10).padding(.horizontal, 25)
                
                ExerciseTitle(exercise: state.exercise, theme: theme)
                VStack {
                    ExerciseCardView(theme: theme) {
                        VStack(alignment: .leading, spacing: 5) {
                            Text("Description")
                                .font(.system(size: 16))
                                .foregroundColor(theme.textGrayColor)
                                .padding(.horizontal, 5)
                                .padding(.top, 5)
                            ScrollView {
                                Text(state.exercise.desc + "\n")
                                    .foregroundColor(theme.textHintColor)
                                    .font(.system(size: 16))
                                    .padding(.horizontal, 15)
                            }
                        }.padding()
                    }
                    .frame(maxHeight: .infinity)
                }
                .padding(.vertical, 10)
                .padding(.horizontal, 25)
                Spacer()
            }.background( theme.backgroundGradient)
            
            if let videoUrl {
                VideoViewer(videoUrl: videoUrl) {
                    self.videoUrl = nil
                }
            }
        }.background(theme.backgroundGradient).onAppear {
            guard let exerciseRoute = screenConfig(.EXERCISE_SCREEN_ROUTE) as? ExerciseRoute else {
                return
            }
            obs.loadData(exercise: exerciseRoute.exercise)
        }.navigationBarBackButtonHidden()
    }
}



struct ExerciseTitle : View {
    
    let exercise: Exercise
    let theme: Theme

    var body: some View {
        VStack(alignment: .leading) {
            ExerciseCardView(height: 110, theme: theme) {
                VStack(alignment: .leading) {
                    Text(exercise.title)
                        .multilineTextAlignment(.leading)
                        .foregroundColor(theme.textColor)
                        .font(.system(size: 14, weight: .regular))
                        .padding(.horizontal, 5)
                        .padding(.top, 5)
                        .lineLimit(2, reservesSpace: true)
                    Spacer()
                    HStack {
                        Text("Category: ")
                            .lineLimit(1)
                            .foregroundStyle(theme.textColor)
                            .font(.system(size: 14))
                            .padding(5)
                        Text(exercise.cato)
                            .foregroundStyle(theme.textHintColor)
                            .font(.system(size: 14))
                            .lineLimit(1)
                            .padding(5)
                        Spacer()
                    }
                    HStack {
                        HStack {
                            ImageSystem(systemIcon: "person.fill", tint: theme.textColor)
                                .frame(width: 15, height: 15)
                            Text("\(exercise.views)")
                                .font(.system(size: 10))
                                .foregroundColor(theme.textGrayColor)
                        }
                        Spacer().frame(width: 50)
                        HStack {
                            ImageAsset(icon: "timer", tint: theme.primary)
                                .frame(width: 15, height: 15)
                            Text(exercise.lengthStr)
                                .font(.system(size: 10))
                                .foregroundColor(theme.textColor)
                        }
                    }
                    .padding([.leading, .trailing], 15)
                    .padding(.bottom, 3)
                }.padding(5).onStart()
            }
        }
        .padding(.vertical, 10)
        .padding(.horizontal, 25)
    }
}


struct ExerciseCardView<Content: View>: View {

    let theme: Theme
    let height: CGFloat?
    let content: Content

    init(height: CGFloat? = nil, theme: Theme, @ViewBuilder content: () -> Content) {
        self.height = height
        self.theme = theme
        self.content = content()
    }

    var body: some View {
        RoundedRectangle(cornerRadius: 15)
            .fill(theme.background)
            .shadow(color: .gray.opacity(0.5), radius: 8, x: 0, y: 4)
            .overlay(
                content
            )
            .frame(height: height)
    }
}
