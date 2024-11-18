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
            /// @OmAr-Kader => Test The Sheet
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
