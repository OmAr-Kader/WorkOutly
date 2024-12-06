//
//  CreateExerciseScreen.swift
//  iosApp
//
//  Created by OmAr on 01/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import _PhotosUI_SwiftUI

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
            VStack {
                switch state.slide {
                case 0: VideoAndTitleItem(title: state.exercise.title, theme: theme, isLoading: obs.setMainProcess, videoPicker: obs.setVideo, setTitle: obs.setTitle)
                case 1: DescriptionItem(description: state.exercise.desc, theme: theme, setDescription: obs.setDescription)
                default : CategoriesItem(selectedCategory: state.exercise.cato, theme: theme, setCategory: obs.setCato)
                }
                Spacer()
                HStack {
                    Spacer()
                    if state.slide != 0 {
                        Button {
                            if (state.slide > 0) {
                                withAnimation {
                                    obs.setSlide(state.slide - 1)
                                }
                            }
                        } label: {
                            HStack {
                                ImageAsset(icon: "previous", tint: theme.textColor).frame(height: 24)
                                Spacer().frame(width: 5)
                                Text("Previous").foregroundColor(theme.textColor)
                            }
                            .padding(14)
                            .background(RoundedRectangle(cornerRadius: 15).fill(theme.backDark))
                        }.buttonStyle(PlainButtonStyle())
                            .animation(.easeInOut(duration: 0.3), value: state.slide)
                    }
                    Spacer().frame(width: 10)
                    Button {
                        if (!state.exercise.cato.isEmpty) {
                            obs.upload {
                                toast = Toast(style: .success, message: "Uploaded Successfully")
                                Task { @MainActor in
                                    await Task.sleep(seconds: 1)
                                    backPress()
                                }
                            } failed: {
                                toast = Toast(style: .error, message: "Failed")
                            }
                        } else {
                            toast = Toast(style: .error, message: "Not Completed")
                        }
                    } label: {
                        HStack {
                            ImageAsset(icon: "cloud_upload", tint: theme.textForPrimaryColor).frame(height: 24)
                            Spacer().frame(width: 5)
                            Text("Post").foregroundColor(theme.textForPrimaryColor)
                        }
                        .padding(14)
                        .background(RoundedRectangle(cornerRadius: 15).fill(!state.exercise.cato.isEmpty ? theme.primary : theme.primary.darken(by: 0.3)))
                    }.buttonStyle(PlainButtonStyle())
                    Spacer().frame(width: 10)
                    if state.slide != 2 {
                        Button {
                            if (state.slide == 0 && (state.exercise.title.isEmpty || state.videoUrl == nil)) {
                                toast = Toast(style: .error, message: "Please Fill the required details")
                                return
                            } else if (state.slide == 1 && (state.exercise.desc.isEmpty)) {
                                toast = Toast(style: .error, message: "Please Fill the required details")
                                return
                            } else if (state.slide == 2) {
                                return
                            }
                            withAnimation {
                                obs.setSlide(state.slide + 1)
                            }
                        } label: {
                            HStack {
                                ImageAsset(icon: "next", tint: theme.textColor).frame(height: 24)
                                Spacer().frame(width: 5)
                                Text("Next").foregroundColor(theme.textColor)
                            }
                            .padding(14)
                            .background(RoundedRectangle(cornerRadius: 15).fill(theme.backDark))
                        }.buttonStyle(PlainButtonStyle())
                            .animation(.easeInOut(duration: 0.3), value: state.slide)
                    }
                    Spacer()
                }.frame(height: 80)
            }
            
            LoadingScreen(isLoading: state.isProcess)
        }.background(theme.backgroundGradient)
            .toastView(toast: $toast)
            .navigationBarBackButtonHidden(true).toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    BackBarButton(tint: theme.textForGradientColor, action: backPress)
                }
            }.onAppear {
                theme.enableSwipeBackGesture()
            }
    }
}


struct VideoAndTitleItem : View {
    var videoUrl : URL?
    var title : String
    var theme: Theme
    let isLoading: (Bool) -> Void
    let videoPicker: @Sendable @MainActor (URL) -> Void
    var setTitle: (String) -> Unit

    @State
    private var selectedItem: PhotosPickerItem?
    
    @State
    private var locaThumb: UIImage? = nil
    
    var body: some View {
        let locaThumb = self.locaThumb
        PhotosPicker(
            selection: $selectedItem,
            matching: .any(of: [.videos])
        ) {
            PickerBody(locaThumb: locaThumb, videoUrl: videoUrl, theme: theme)
        }.onChangeNil(selectedItem) { newIt in
            isLoading(true)
            if let newIt {
                newIt.getURL { url, isVideo in
                    loadThumbnail(videoURL: url) { image in
                        isLoading(false)
                        self.locaThumb = image
                    }
                    TaskMainSwitcher {
                        videoPicker(url)
                    }
                } failed: {
                    isLoading(false)
                }
            }
        }
        VStack {
            HStack(alignment: .top) {
                VStack(alignment: .leading) {
                    OutlinedTextField(text: title, onChange: setTitle, hint: "Exercise Title", isError: false, errorMsg: "Title Is Empty", theme: theme, cornerRadius: 12, lineLimit: 1, keyboardType: UIKeyboardType.alphabet).transition(.opacity)
                }.padding(10)
            }.padding(5).background(theme.background).clipShape(.rect(cornerRadius: 16))//.shadow(radius: 8)
        }.padding(.vertical, 10).padding(.horizontal, 25)
    }
}

struct PickerBody : View {
 
    //let image: Image
    
    let locaThumb : UIImage?
    var videoUrl : URL?
    var theme: Theme

    var body: some View {
        VStack {
            HStack(alignment: .top) {
                if let locaThumb {
                    ZStack {
                        Image(uiImage: locaThumb)
                            .resizable()
                            .renderingMode(.original)
                            .background(Color.clear)
                            .imageScale(.large)
                            .aspectRatio(contentMode: .fill)
                            .frame(height: 200)
                        FullZStack {}.background(UIColor(50, 50, 50).withAlphaComponent(0.5).toC).frame(width: 300, height: 300)
                        ImageAsset(icon: "replace", tint: Color.white).frame(width: 24, height: 24)
                    }.frame(height: 300)
                } else {
                    if let videoUrl {
                        ZStack {
                            ImageCacheView(videoUrl.absoluteString, isVideoPreview: true, contentMode: .fill).frame(height: 200)
                            FullZStack {}.background(UIColor(50, 50, 50).withAlphaComponent(0.5).toC).frame(width: 300, height: 300)
                            ImageAsset(icon: "replace", tint: Color.white).frame(width: 24, height: 24)
                        }.frame(height: 300)
                    } else {
                        HStack(alignment: .center) {
                            Spacer()
                            ImageAsset(icon: "cloud_upload", tint: theme.textColor).frame(width: 40, height: 40)
                            Spacer()
                        }.frame(height: 200)
                    }
                }
            }.padding(5).background(theme.background).frame(height: 200).clipShape(.rect(cornerRadius: 16)).shadow(radius: 8)
        }.padding(.vertical, 10).padding(.horizontal, 25)
    }
}

struct DescriptionItem : View {
    var description: String
    var theme: Theme
    var setDescription: (String) -> Void

    var body: some View {
        VStack {
            HStack(alignment: .top) {
                VStack(alignment: .leading) {
                OutlinedTextField(text: description, onChange: setDescription, hint: "Description", isError: false, errorMsg: "Description Is Empty", theme: theme, cornerRadius: 12, lineLimit: nil, keyboardType: UIKeyboardType.alphabet).transition(.opacity)
                    Spacer()
                }.padding(10)
            }.background(theme.background).padding(5).clipShape(.rect(cornerRadius: 16)).shadow(radius: 8)
        }.padding(.vertical, 10).padding(.horizontal, 25)
    }
}

struct CategoriesItem : View {
    var selectedCategory: String
    var theme: Theme
    let setCategory: (String) -> Unit
    
    private let exerciseCategories: [String] = ConstKt.exerciseCategories
    
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    Text("Choose a muscle group")
                        .foregroundColor(theme.textColor)
                        .multilineTextAlignment(.center)
                        .padding(.top, 20)
                    VerticalGridNonIdentifiable(columns: isPortraitMode() ? 2 : 4, list: exerciseCategories) { category in
                        ZStack {
                            RoundedRectangle(cornerRadius: 25)
                                .fill(selectedCategory == category ? theme.background : Color.gray.opacity(0.3))
                                .frame(minHeight: 50)
                            HStack {
                                Spacer()
                                Text(category)
                                    .foregroundColor(theme.textColor)
                                    .padding()
                                Spacer()
                            }.onTapGesture {
                                setCategory(category)
                            }
                        }
                    }.padding(16)
                }
            }
            Spacer()
        }
    }
}

