import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    @State var isInjected: Bool = false
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                if isInjected {
                    Main(app: delegate.app)
                } else {
                    SplashScreen().task {
                        await initModule()
                        let _ = await Task { @MainActor in
                            //await delegate.app.findUserLive { it in
                            //logger(String(isInjected))
                                if !isInjected {
                                    withAnimation {
                                        /*delegate.app.navigateHomeNoAnimation(
                                            .HOME_SCREEN_ROUTE//it == nil ? .AUTH_SCREEN_ROUTE : .HOME_SCREEN_ROUTE
                                        )*/
                                        isInjected.toggle()
                                    }
                                }
                            //}
                        }.result
                    }
                }
            }
        }
    }
}
