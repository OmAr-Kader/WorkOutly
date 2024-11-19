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
    
    @State private var expandedHeight: CGFloat = 420 // RideSheetDragHandler + RideSheet + Padding
    @State private var collapsedHeight: CGFloat = 60
    @State private var currentOffset: CGFloat = -(420 - 60)

    var body: some View {
        let state = obs.state
        ZStack(alignment: .topLeading) {
            ScrollView {
                VStack(spacing: 16) {
                    VerticalGrid(columns: isPortraitMode() ? 2 : 4, list: state.metrics) { metric in
                        FitnessMetricItem(metric: metric, theme: theme) {
                            navigateToScreen(SessionRoute(metric: metric), .SESSION_SCREEN_ROUTE)
                        }
                    }
                    .padding(16)
                    ForEach(state.exercises, id: \.id) { exercise in
                        ExerciseItem(exercise: exercise, theme: theme) {
                            navigateToScreen(ExerciseRoute(exercise: exercise), .EXERCISE_SCREEN_ROUTE)
                        }
                    }
                    Spacer().frame(height: 80)
                }
            }.padding(.top, 16)
            LoadingScreen(isLoading: state.isProcess)
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
        }.background(theme.backgroundGradient).overlay {
            ZStack {
                Button {
                    obs.setIsLiveVisible(it: true)
                } label: {
                    HStack {
                        ImageAsset(icon: "dumbbell", tint: theme.textForPrimaryColor).frame(width: 30, height: 30)
                        Spacer().frame(width: 3)
                        Text("Live Session")
                            .foregroundColor(theme.textForPrimaryColor)
                            .font(.system(size: 18))
                    }.padding(top: 7, leading: 15, bottom: 7, trailing: 15)
                        .background(RoundedRectangle(cornerRadius: 14, style: .continuous).fill(theme.primary))
                }
                .tint(theme.textForPrimaryColor)
                .frame(width: 60, height: 60)
                .background(RoundedRectangle(cornerRadius: 30).fill(theme.primary))
                .shadow(radius: 10)
                .onBottomEnd()
            }.padding(trailing: 20)
        }.sheet(isPresented: Binding(get: {
            obs.state.isLiveVisible
        }, set: { it in
            obs.setIsLiveVisible(it: it)
        })) {
            if #available(iOS 16.4, *) {
                ChatView(list: state.messages) { imageUrl in
                    self.imageUrl = imageUrl
                } inVideo: { videoUrl in
                    self.videoUrl = videoUrl
                } send: { txt in
                    
                } file: {
                    
                }.background(theme.background)
                    .edgesIgnoringSafeArea(.all)
                    .presentationBackground(theme.background)
                    .presentationDetents([.large, .custom(CommentSheetDetent.self)])
                    .presentationContentInteraction(.scrolls)
                    .interactiveDismissDisabled()
            } else {
                ChatView(list: state.messages) { imageUrl in
                    self.imageUrl = imageUrl
                } inVideo: { videoUrl in
                    self.videoUrl = videoUrl
                } send: { txt in
                    
                } file: {
                    
                }.background(theme.background)
                    .edgesIgnoringSafeArea(.all).presentationDetents([.large, .custom(CommentSheetDetent.self)])
                    .interactiveDismissDisabled()
            }
        }.onAppear {
            obs.loadData(userPref: userPref, isDarkMode: theme.isDarkMode) {
                toast = Toast(style: .error, message: "Permissions is required")
            }
        }
    }
}

struct MainContent: View {
    let metrics: [FitnessMetric]
    let exercises: [Exercise]
    let theme: Theme
    let navigateToScreen: (Screen) -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                VerticalGrid(columns: isPortraitMode() ? 2 : 4, list: metrics) { metric in
                    FitnessMetricItem(metric: metric, theme: theme) {
                        //navigateToScreen()//sessionRoute
                    }
                }
                .padding(16)
                ForEach(exercises, id: \.id) { exercise in
                    ExerciseItem(exercise: exercise, theme: theme) {
                        //navigateToScreen()//exerciseRoute
                    }
                }
                Spacer().frame(height: 80)
            }
        }.padding(.top, 16)
    }
}

struct BarMainScreen: View {

    let userPref: UserPref
    let theme: Theme
    let onOptions: () -> Void

    var body: some View {
        ZStack(alignment: .center) {
            HStack(alignment: .center) {
                Spacer()
                Button(action: onOptions) {
                    ImageAsset(icon: "plus", tint: theme.textForGradientColor)
                        .frame(width: 30, height: 30).padding(5)
                }
            }.padding(.horizontal, 10)
            HStack(alignment: .center) {
                Text("Hi, \(userPref.name)")
                    .foregroundColor(theme.textForGradientColor)
                    .font(.system(size: 20, weight: .regular))
            }
        }
        .frame(height: 60)
        .background(Color.clear)
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
    let exercise: Exercise
    let theme: Theme
    let onClick: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Button(action: onClick) {
                HStack {
                    VStack {
                        ImageCacheView(exercise.videoUri, isVideoPreview: true, contentMode: .fit)
                            .frame(width: 80, height: 80, alignment: .center)
                    }.frame(width: 80, height: 80, alignment: .center)
                        .clipShape(
                            .rect(cornerRadius: 15)
                        )
                    VStack(alignment: .leading, spacing: 5) {
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
                        
                        HStack {
                            HStack(spacing: 2) {
                                ImageSystem(systemIcon: "person.fill", tint: theme.textColor)
                                    .frame(width: 15, height: 15)
                                Text("\(exercise.views)")
                                    .font(.system(size: 10))
                                    .foregroundColor(theme.textGrayColor)
                            }
                            .padding(.trailing, 50)
                            HStack(spacing: 2) {
                                ImageAsset(icon: "timer", tint: theme.primary)
                                    .frame(width: 15, height: 15)
                                Text(exercise.lengthStr)
                                    .font(.system(size: 10))
                                    .foregroundColor(theme.textColor)
                            }
                        }
                    }
                    .padding(.leading, 10)
                }
                .frame(height: 80)
                .padding(12)
                .background(theme.background)
                .cornerRadius(15)
                .shadow(color: Color.black.opacity(0.2), radius: 8, x: 0, y: 4)
            }
        }
    }
}

struct ChatView : View {
    
    let list: [Message]
    let inImage: (String) -> Void
    let inVideo: (String) -> Void
    let send: (String) -> Void
    let file: () -> Void

    @State
    private var chatText: String = ""
    
    @Inject
    private var theme: Theme
    
    @FocusState
    private var isFoucesed: Bool

    var body: some View {
        VStack {
            VStack {
                Capsule()
                    .fill(theme.textGrayColor)
                    .frame(width: 40, height: 6)
                    .padding(.top, 10)
                    .padding(.bottom, 6)
                VStack(alignment: .leading) {
                    Text(
                        "Live Session"
                    ).foregroundStyle(theme.textColor).font(.system(size: 16))
                    //Spacer().frame(height: 10)
                }
            }
            ScrollViewReader { proxy in
                ScrollView(Axis.Set.vertical, showsIndicators: false) {
                    LazyVStack {
                        ForEach(list, id: \.id) { msg in
                            MessageItem(msg: msg, theme: theme, inImage: inImage, inVideo: inVideo)
                        }
                    }.padding(leading: 5, bottom: 7, trailing: 5)
                }.onAppear {
                    proxy.scrollTo(list.count - 1)
                }
            }
            Spacer()
            HStack {
                HStack {
                    TextField("", text: $chatText, axis: Axis.vertical)
                        .placeholder(when: chatText.isEmpty, alignment: .leading, placeholder: {
                            Text("Question?")
                                .foregroundColor(theme.textHintColor)
                        }).focused($isFoucesed).multilineTextAlignment(.leading).foregroundColor(theme.textColor).frame(alignment: .leading)
                        .onTapGesture {
                            isFoucesed = true
                        }
                    Button(action: {
                        isFoucesed = false
                        file()
                    }, label: {
                        ImageAsset(icon: "file", tint: theme.textGrayColor)
                            .frame(width: 30, height: 30)
                    }).frame(width: 50, height: 50, alignment: .center)
                    Button(action: {
                        isFoucesed = false
                        let chatText = self.chatText
                        self.chatText = ""
                        send(chatText)
                    }, label: {
                        ImageAsset(icon: "send", tint: theme.textGrayColor)
                            .frame(width: 30, height: 30)
                    }).frame(width: 50, height: 50, alignment: .center)
                }.padding(leading: 10, trailing: 5)
            }.shadow(radius: 3).background(theme.backDark)
                .clipShape(.rect(topLeadingRadius: 8, topTrailingRadius: 8))
        }.background(theme.background)
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
        VStack {
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
                ImageCacheView(msg.fileUrl, isVideoPreview: true, contentMode: .fill).clipShape(RoundedRectangle(cornerRadius: 20)).frame(width: 300).onTapGesture {
                    inVideo(msg.fileUrl)
                }
                FullZStack {}.background(UIColor(red: 50 / 255, green: 50 / 255, blue: 50 / 255, alpha: 0.5).toC)
                ImageSystem(systemIcon: "play.fill", tint: Color.white).frame(width: 24, height: 24)
            }.frame(height: 300)
            default: ZStack {}
            }
        }.frame(minWidth: /*@START_MENU_TOKEN@*/0/*@END_MENU_TOKEN@*/, maxWidth: 300).padding(5).background(
            RoundedRectangle(cornerRadius: 20).fill(colorCard)
        ).shadow(radius: 2)
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
    @State private var isBackVisiable : Bool = true
    @State private var controller = AVPlayerViewController()
    
    var body: some View {
        FullScreenDialog {
            onClose()
        } content: {
            VideoPlayer(
                player: player
            ).onAppear() {
                guard let url = URL(string: videoUrl) else {
                    return
                }
                let player = AVPlayer(url: url)
                self.player = player
                controller.modalPresentationStyle = UIModalPresentationStyle.fullScreen
                controller.player = player
                
                controller.player?.play()
            }.onDisappear() {
                player?.pause()
            }
        }
    }
    
}

/*
 
 @Composable
 fun VideoViewer(videoItems: List<VideoPlayerMediaItem>, onClose: () -> Unit) {
     Dialog(
         onDismissRequest = onClose,
         properties = DialogProperties(usePlatformDefaultWidth = false)
     ) {
         Box(
             modifier = Modifier
                 .fillMaxSize()
                 .background(Color.Black)
         ) {
             val videoConfig = remember { videoConfig.copy(showFullScreenButton = false) }
             VideoPlayer(
                 mediaItems = videoItems,
                 handleLifecycle = true,
                 autoPlay = false,
                 usePlayerController = true,
                 enablePip = true,
                 handleAudioFocus = true,
                 controllerConfig = videoConfig,
                 repeatMode = RepeatMode.ALL,
                 modifier = Modifier.fillMaxSize(),
             )
             BackButtonLess(Color.White, onClose)
         }
     }
 }
 
 */
