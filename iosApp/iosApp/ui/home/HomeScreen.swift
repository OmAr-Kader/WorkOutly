//
//  HomeScreen.swift
//  iosApp
//
//  Created by OmAr on 16/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import Zoomable
import AVKit
import _PhotosUI_SwiftUI

struct HomeScreen : View {
    
    let userPref: UserPref
    var findPreferenceMainMain: @MainActor (String, @MainActor @escaping (String?) -> Unit) -> Unit
    let navigateToScreen: @MainActor (ScreenConfig, Screen) -> Unit
        
    @MainActor
    @StateObject private var obs: HomeObserve = HomeObserve()
    
    @MainActor
    @State private var toast: Toast? = nil
    
    @Inject
    private var theme: Theme

    @State private var isFullScreen = false
    
    @MainActor
    @State private var imageUrl: String?
    
    @MainActor
    @State private var videoUrl: String?
    
    @State private var expandedHeight: CGFloat = 420 
    @State private var collapsedHeight: CGFloat = 0
    @State private var currentOffset: CGFloat = 0

    @State private var isPresented = false

    var body: some View {
        let state = obs.state
        ZStack(alignment: .topLeading) {
            
            BarMainScreen(userPref: userPref, days: state.days, sortBy: state.sortBy, theme: theme) { it in
                obs.setFilterDays(userPref: userPref, isDarkMode: theme.isDarkMode, it: it)
            } changeSortBy: { it in
                obs.setSortBy(it: it)
            } signOut: {
                
            }
            ScrollView {
                LazyVStack {
                    VerticalGrid(columns: isPortraitMode() ? 2 : 4, list: state.metrics) { metric in
                        FitnessMetricItem(metric: metric, theme: theme) {
                            navigateToScreen(SessionRoute(metric: metric, days: state.days), .SESSION_SCREEN_ROUTE)
                        }
                    }.padding(16)
                    VStack {
                        ForEach(state.exercises, id: \.id) { exercise in
                            ExerciseItem(exercise: exercise, theme: theme) {
                                navigateToScreen(ExerciseRoute(exercise: exercise), .EXERCISE_SCREEN_ROUTE)
                            }
                        }
                    }.padding(16)
                    Spacer().frame(height: 80)
                }
            }.padding(.top, 60)
            ZStack {
                Button(action: {
                    withAnimation {
                        //currentOffset = -1 * (expandedHeight - 20)
                        obs.setIsLiveVisible(it: true)
                    }
                }) {
                    HStack {
                        ImageAsset(icon: "dumbbell", tint: theme.textForPrimaryColor).frame(width: 30, height: 30)
                        Spacer().frame(width: 5)
                        Text("Live Session")
                            .foregroundColor(theme.textForPrimaryColor)
                            .font(.system(size: 18))
                    }.padding(top: 7, leading: 15, bottom: 7, trailing: 15)
                }
                .tint(theme.textForPrimaryColor)
                .frame(height: 60)
                .background(RoundedRectangle(cornerRadius: 15).fill(theme.primary))
                .shadow(radius: 10)
                .onBottomEnd()
            }.padding( bottom: 20, trailing: 20).onBottomEnd()
            LoadingScreen(isLoading: state.isProcess)
        }.sheet(isPresented: Binding(get: {
            obs.state.isLiveVisible
        }, set: { it in
            obs.setIsLiveVisible(it: it)
        })) {
            NavigationStack {
                ZStack {
                    ChatView(list: state.messages) { imageUrl in
                        self.imageUrl = imageUrl
                    } inVideo: { videoUrl in
                        self.videoUrl = videoUrl
                    } send: { txt in
                        
                    } file: { url, isVideo in
                        
                    }
                    
                    if let imageUrl {
                        ImageViewer(imageUrl: imageUrl) {
                            self.imageUrl = nil
                        }
                    }
                    if let videoUrl {
                        VideoViewer(videoUrl: videoUrl) {
                            self.videoUrl = nil
                        }
                    }
                }
            }.background(theme.background)
                .presentationBackground(theme.background)
                .presentationDetents([.large, .custom(CommentSheetDetent.self)])
                .presentationContentInteraction(.scrolls)
                .presentationDetents([.height(expandedHeight)])
        }.background(theme.backgroundGradient).onAppear {
            findPreferenceMainMain(ConstKt.PREF_DAYS_COUNT) { days in
                obs.loadData(userPref: userPref, days: Int(days ?? "7") ?? 7, isDarkMode: theme.isDarkMode) {
                    toast = Toast(style: .error, message: "Permissions is required")
                }
            }
        }.onAppear { // TO FULL SCREEN DRAGABLE SHEET
            DispatchQueue.main.async {
                let screenHeight = UIScreen.main.bounds.height
                expandedHeight = screenHeight
            }
        }.onChange(of: UIDevice.current.orientation) { _ in  // TO FULL SCREEN DRAGABLE SHEET
            DispatchQueue.main.async {
                let screenHeight = UIScreen.main.bounds.height
                expandedHeight = screenHeight
            }
        }
    }
}

struct BarMainScreen: View {

    let userPref: UserPref
    let days: Int
    let sortBy: Int
    let theme: Theme
    let changeDays: (Int) -> Void
    let changeSortBy: (Int) -> Void
    let signOut: () -> Void

    var body: some View {
        ZStack(alignment: .center) {
            HStack(alignment: .center) {
                Spacer()
                Menu {
                    Menu("Health Metrics Days") {
                        Button(action: { changeDays(1) }) {
                            Label("Today", systemImage: days == 1 ? "checkmark" : "")
                        }
                        Button(action: { changeDays(3) }) {
                            Label("Last 3 days", systemImage: days == 3 ? "checkmark" : "")
                        }
                        Button(action: { changeDays(7) }) {
                            Label("Last 7 days", systemImage: days == 7 ? "checkmark" : "")
                        }
                        Button(action: { changeDays(14) }) {
                            Label("Last 14 days", systemImage: days == 14 ? "checkmark" : "")
                        }
                        Button(action: { changeDays(30) }) {
                            Label("Last 30 days", systemImage: days == 30 ? "checkmark" : "")
                        }
                        Button(action: { changeDays(90) }) {
                            Label("Last 90 days", systemImage: days == 90 ? "checkmark" : "")
                        }
                    }
                    Menu("Sort Exercise By") {
                        Button(action: { changeSortBy(1) }) {
                            Label("Newer First", systemImage: sortBy == 1 ? "checkmark" : "")
                        }
                        Button(action: { changeSortBy(2) }) {
                            Label("Views Count", systemImage: sortBy == 2 ? "checkmark" : "")
                        }
                        Button(action: { changeSortBy(3) }) {
                            Label("Top Coaches", systemImage: sortBy == 3 ? "checkmark" : "")
                        }
                        Button(action: { changeSortBy(4) }) {
                            Label("Name", systemImage: sortBy == 4 ? "checkmark" : "")
                        }
                    }
                    Button("Sign out", action: signOut)
                } label: {
                    ImageAsset(icon: "more", tint: theme.textForGradientColor)
                        .padding(5)
                        .frame(width: 30, height: 30)
                }
            }.padding(.horizontal, 15)
            HStack(alignment: .center) {
                Text("Hi, \(userPref.name)")
                    .foregroundColor(theme.textForGradientColor)
                    .font(.system(size: 20, weight: .regular))
            }
        }
        .frame(height: 60)
        .background(Color.clear)
        //.popover(isPresented: $isPresented) {}
    }
}

struct FitnessMetricItem: View {
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
            VStack {
                ImageAsset(icon: iconAsst, tint: Color(argb: UInt32(metric.iconColor)))
                    .scaledToFit()
                    .frame(width: 30, height: 30)
                Spacer().frame(height: 8)
                Text(metric.title)
                    .font(.system(size: 14, weight: .bold))
                    .foregroundColor(theme.textColor)
                    .lineLimit(1)
                Text(metric.valueStr)
                    .font(.system(size: 14))
                    .foregroundColor(theme.textGrayColor)
                    .lineLimit(1)
            }
            .padding(8)
            .frame(maxWidth: .infinity, maxHeight: 90)
            .background(theme.background)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4)
        }
    }
}


struct ExerciseItem: View {

    @Environment(\.dismiss) var dismiss

    private let exercise: Exercise
    private let theme: Theme
    private let onClick: () -> Void
    
    init(exercise: Exercise, theme: Theme, onClick: @escaping () -> Void) {
        self.exercise = exercise
        self.theme = theme
        self.onClick = onClick
    }
    
    var body: some View {
        VStack(alignment: .leading) {
                HStack {
                    VStack {
                        ImageCacheView(exercise.videoUri, isVideoPreview: true, contentMode: .fill)
                            .frame(width: 80, height: 80, alignment: .center)
                    }.frame(width: 80, height: 80, alignment: .center)
                        .clipShape(
                            .rect(cornerRadius: 15)
                        )
                    VStack(alignment: .leading) {
                        Text(exercise.title)
                            .font(.system(size: 14))
                            .fontWeight(.semibold)
                            .foregroundColor(theme.textColor)
                            .lineLimit(1)
                            .padding(.bottom, 2)
                        Text(exercise.description)
                            .font(.system(size: 10))
                            .foregroundColor(theme.textGrayColor)
                            .lineLimit(1)
                            .truncationMode(.tail)
                            .padding(.bottom, 2)
                        HStack(alignment: .center) {
                            HStack {
                                ImageSystem(systemIcon: "person.fill", tint: theme.textColor)
                                    .frame(width: 15, height: 15)
                                Text("\(exercise.views)")
                                    .font(.system(size: 10))
                                    .foregroundColor(theme.textGrayColor)
                            }
                            Spacer().frame(width: 2)
                            HStack {
                                ImageAsset(icon: "timer", tint: theme.primary)
                                    .frame(width: 15, height: 15)
                                Text(exercise.lengthStr)
                                    .font(.system(size: 10))
                                    .foregroundColor(theme.textColor)
                            }
                        }.padding(leading: 15, trailing: 15)
                    }.padding(5)
                }
                .frame(height: 80)
                .background(theme.background)
                .cornerRadius(15)
                .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4).onTapGesture(perform: onClick)
        }.padding(top: 6, bottom: 6)
    }
}

struct ChatView : View {
    
    let list: [Message]
    let inImage: (String) -> Void
    let inVideo: (String) -> Void
    let send: (String) -> Void
    let file: @Sendable (URL, _ isVideo: Bool) -> Void

    @State
    private var chatText: String = ""
    
    @Inject
    private var theme: Theme
    
    @FocusState
    private var isFoucesed: Bool

    @State
    private var selectedItem: PhotosPickerItem?

    var body: some View {
        VStack(alignment: .center) {
            VStack {
                HStack(alignment: .center) {
                    Spacer()
                    Capsule()
                        .fill(theme.textGrayColor)
                        .frame(width: 40, height: 6)
                        .padding(.top, 10)
                        .padding(.bottom, 6)
                    Spacer()
                }
                HStack {
                    Text(
                        "Live Session"
                    ).foregroundStyle(theme.textColor).font(.system(size: 18)).padding(leading: 7, bottom: 7)
                    Spacer()
                    //Spacer().frame(height: 10)
                }
            }.frame(height: 60).background(theme.backDark)
            VStack(alignment: .leading) {
                ScrollViewReader { proxy in
                    ScrollView(Axis.Set.vertical, showsIndicators: false) {
                        LazyVStack(alignment: .leading) {
                            ForEach(0..<list.count, id: \.self) { index in
                                MessageItem(msg: list[index], theme: theme, inImage: inImage, inVideo: inVideo).id(index)
                            }
                        }.padding(leading: 5, bottom: 7, trailing: 5)
                    }.onAppear {
                        proxy.scrollTo(list.count - 1, anchor: .bottom)
                    }.onChange(list) { it in
                        proxy.scrollTo(it.count - 1, anchor: .bottom)
                    }
                }
                Spacer()
                HStack {
                    HStack {
                        TextField("", text: $chatText, axis: Axis.vertical)
                            .placeholder(when: chatText.isEmpty, alignment: .leading, placeholder: {
                                Text("Message")
                                    .foregroundColor(theme.textHintColor)
                            }).focused($isFoucesed).multilineTextAlignment(.leading).foregroundColor(theme.textColor).frame(alignment: .leading)
                            .onTapGesture {
                                isFoucesed = true
                            }
                        let textGrayColor = theme.textGrayColor
                        PhotosPicker(
                            selection: $selectedItem,
                            matching: .any(of: [.images, .videos])
                        ) {
                            ImageAsset(icon: "file", tint: textGrayColor)
                                .padding(6)
                                .frame(width: 30, height: 30)
                        }.onChange(selectedItem, forChangePhoto(file))
                            .frame(width: 50, height: 50, alignment: .center)
                        Button(action: {
                            isFoucesed = false
                            let chatText = self.chatText
                            self.chatText = ""
                            send(chatText)
                        }, label: {
                            ImageAsset(icon: "send", tint: theme.textGrayColor)
                                .padding(3)
                                .frame(width: 30, height: 30)
                        }).frame(width: 50, height: 50, alignment: .center)
                    }.padding(leading: 10, trailing: 5)
                }.shadow(radius: 3).background(theme.backDark)
                    .clipShape(.rect(topLeadingRadius: 8, topTrailingRadius: 8))
            }.background(theme.background)
        }
    }
}

struct MessageItem : View {
    
    private let msg: Message
    private let inImage: (String) -> Void
    private let inVideo: (String) -> Void

    private let colorCard: Color
    private let colorText: Color
    
    init(msg: Message, theme: Theme, inImage: @escaping (String) -> Void, inVideo: @escaping (String) -> Void) {
        self.msg = msg
        self.inImage = inImage
        self.inVideo = inVideo
        self.colorCard = if msg.isFromCurrentUser {
            theme.gradientColor
        } else {
            theme.gradientSec
        }
        self.colorText = if (msg.isFromCurrentUser) {
            theme.textForGradientColor
        } else {
            theme.textColor
        }
    }
    
    var body: some View {
        HStack() {
            if msg.isFromCurrentUser {
                Spacer()
            }
            VStack(alignment: .leading) {
                if (!msg.isFromCurrentUser) {
                    Text(msg.senderName).font(.system(size: 12))
                        .foregroundStyle(colorText)
                        .padding(top: 2.5, leading: 10, bottom: 2.5, trailing: 10)
                        .opacity(0.8).onStart()
                }
                switch msg.type {
                case ConstKt.MSG_TEXT: Text(msg.message).font(.system(size: 14))
                        .foregroundStyle(colorText)
                        .padding(top: 2.5, leading: 10, bottom: 2.5, trailing: 10)
                        .foregroundStyle(colorText).onStart()
                case ConstKt.MSG_IMG: ZStack {
                    ImageCacheView(msg.fileUrl, contentMode: .fill).clipShape(RoundedRectangle(cornerRadius: 20)).frame(width: 300, height: 300).onTapGesture {
                        inImage(msg.fileUrl)
                    }
                }.frame(width: 300)
                case ConstKt.MSG_VID: ZStack {
                    ImageCacheView(msg.fileUrl, isVideoPreview: true, contentMode: .fill).frame(width: 300, height: 300).clipShape(RoundedRectangle(cornerRadius: 20))
                    FullZStack {}.background(UIColor(50, 50, 50).withAlphaComponent(0.5).toC).frame(width: 300, height: 300)
                    ImageSystem(systemIcon: "play.fill", tint: Color.white).frame(width: 24, height: 24)
                }.frame(width: 300, height: 300).onTapGesture {
                    inVideo(msg.fileUrl)
                }
                default: ZStack {}
                }
            }.frame(minWidth: /*@START_MENU_TOKEN@*/0/*@END_MENU_TOKEN@*/, maxWidth: 300).padding(5).background(
                RoundedRectangle(cornerRadius: 20).fill(colorCard)
            ).shadow(radius: 2)
        }
    }
}

struct ImageViewer : View {
    
    let imageUrl: String
    let onClose: () -> Unit

    var body: some View {
        FullScreenDialog {
            onClose()
        } content: {
            ImageCacheView(imageUrl)
                .scaledToFit()
                .zoomable()
        }
    }
}

struct VideoViewer : View {
    
    let videoUrl: String
    let onClose: () -> Unit

    @State private var player : AVPlayer? = nil
    @State private var controller = AVPlayerViewController()
    
    var body: some View {
        FullScreenDialog {
            onClose()
        } content: {
            VStack {
                Spacer().frame(height: 40)
                if let player = self.player {
                    PlayerViewController(player: player)
                        .background(Color.black)
                }
            }.onAppear() {
                guard let url = URL(string: videoUrl) else {
                    return
                }
                let player = AVPlayer(url: url)
                self.player = player
                player.play()
            }.onDisappear() {
                player?.pause()
            }
        }
    }
    
}
