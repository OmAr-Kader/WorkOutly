import SwiftUI
import shared

extension View {
    
    @MainActor @ViewBuilder func targetScreen(
        _ target: Screen,
        _ app: AppObserve,
        navigateTo: @MainActor @escaping (Screen) -> Unit,
        navigateToScreen: @MainActor @escaping (ScreenConfig, Screen) -> Unit,
        navigateHome: @MainActor @escaping (Screen) -> Unit,
        backPress: @MainActor @escaping () -> Unit,
        screenConfig: @MainActor @escaping (Screen) -> (any ScreenConfig)?,
        findPreferenceMainBack: @escaping @MainActor (String, @BackgroundActor @escaping (String?) -> Unit) -> Unit,
        findPreferenceMainMain: @escaping @MainActor (String, @MainActor @escaping (String?) -> Unit) -> Unit
    ) -> some View {
        switch target {
        case .AUTH_SCREEN_ROUTE:
            SplashScreen()
        case .HOME_SCREEN_ROUTE:
            HomeScreen(userPref: app.state.userPref ?? UserPref(), findPreferenceMainMain: findPreferenceMainMain, navigateToScreen: navigateToScreen)
            //HomeScreen(userPref: app.state.userPref ?? UserPref(), findPreference: findPreference, navigateToScreen: navigateToScreen, navigateHome: navigateHome)
        }
    }
}


enum Screen : Hashable {
    case AUTH_SCREEN_ROUTE
    case HOME_SCREEN_ROUTE

}

protocol ScreenConfig {}

struct HomeRoute: ScreenConfig {
    let userPref: UserPref
}
