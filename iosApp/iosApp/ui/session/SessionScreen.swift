//
//  SessionScreen.swift
//  iosApp
//
//  Created by OmAr on 21/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SessionScreen : View {
    
    let screenConfig: @MainActor (Screen) -> (any ScreenConfig)?
    let backPress: @MainActor () -> Unit
    
    @MainActor
    @StateObject private var obs: SessionObserve = SessionObserve()
    
    @Inject
    private var theme: Theme
    
    var body: some View {
        let state = obs.state
        ZStack(alignment: .topLeading) {
            VStack {
                BackButton(tint: theme.textForGradientColor) {
                    backPress()
                }
                HStack {
                    Spacer()
                    FitnessMetricLabel(metric: state.session, theme: theme) {}
                    Spacer()
                }
                Spacer().frame(height: 10)
                VStack {
                    VStack {
                        VStack {
                            VStack {
                                Text("History")
                                    .font(.system(size: 16))
                                    .foregroundColor(theme.textGrayColor)
                                    .lineLimit(1)
                            }.padding(5).onStart()
                            ScrollView {
                                LazyVStack {
                                    ForEach(state.sessionHistories, id: \.self) { metric in
                                        HStack(alignment: .center) {
                                            HStack {
                                                Text(metric.valueStr)
                                                    .font(.system(size: 16, weight: .bold))
                                                    .foregroundColor(theme.textColor)
                                                Spacer()
                                            }.frame(width: 70)
                                            HStack {
                                                Text(metric.date)
                                                    .font(.system(size: 14))
                                                    .foregroundColor(theme.textGrayColor)
                                                Spacer()
                                            }.frame(width: 130)
                                            if metric.isMoreThanPreviousSession {
                                                ImageSystem(systemIcon: "arrowtriangle.up.fill", tint: .green).frame(width: 14, height: 14)
                                            } else {
                                                ImageSystem(systemIcon: "arrowtriangle.down.fill", tint: .red).frame(width: 14, height: 14)
                                            }
                                            Spacer()
                                        }
                                        .padding(.horizontal, 16)
                                        .padding(.vertical, 6)
                                    }
                                }
                            }
                        }.padding(top: 16, leading: 16, bottom: 0, trailing: 16)
                        Spacer()
                    }
                    .background(theme.background)
                    .cornerRadius(16)
                    .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4)
                }.padding(16)
            }.padding(16)
        }.background(theme.backgroundGradient).onAppear {
            guard let sessionRoute = screenConfig(.SESSION_SCREEN_ROUTE) as? SessionRoute else {
                return
            }
            obs.loadData(metric: sessionRoute.metric, days: sessionRoute.days)
        }.navigationBarBackButtonHidden()
    }
}


struct FitnessMetricLabel: View {
    
    let metric: FitnessMetric
    let theme: Theme
    let invoke: () -> Void
    let iconAsst: String
    
    init(metric: FitnessMetric, theme: Theme, invoke: @escaping () -> Void) {
        self.metric = metric
        self.theme = theme
        self.invoke = invoke
        self.iconAsst =  switch metric.id {
        case ConstKt.STEPS: "steps"
        case ConstKt.HEART_RATE: "heart"
        case ConstKt.CALORIES_BURNED: "fire"
        case ConstKt.DISTANCE: "distance"
        case ConstKt.SLEEP: "sleep"
        case ConstKt.METABOLIC_RATE: "body_system"
        default: "heart"
        }
    }
    
    var body: some View {
        Button(action: invoke) {
            HStack {
                Spacer()
                VStack {
                    ImageAsset(icon: iconAsst, tint: Color(argb: UInt32(metric.iconColor)))
                        .scaledToFit()
                        .frame(width: 40, height: 40)
                    Spacer().frame(height: 8)
                    Text(metric.title)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(theme.textColor)
                        .lineLimit(1)
                    Text(metric.valueStr)
                        .font(.system(size: 14))
                        .foregroundColor(theme.textGrayColor)
                        .lineLimit(1)
                }.padding(16)
                Spacer()
            }
            .padding(8)
            .frame(height: 150)
            .background(theme.background)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4)
        }
    }
}
