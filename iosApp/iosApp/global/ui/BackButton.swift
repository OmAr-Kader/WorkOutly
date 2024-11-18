import SwiftUI


struct BackButtonModifier: ViewModifier {

    let title: String
    let onBackPressed: @MainActor () -> Unit
    
    func body(content: Content) -> some View {
        content
            .navigationBarBackButtonHidden(true)
            .toolbar {
                if !title.isEmpty {
                    ToolbarItem(placement: .principal) {
                        VStack(alignment: .center) {
                            Text(title).font(.headline).foregroundStyle(
                                Color(red: 9 / 255, green: 131 / 255, blue: 1)
                            )
                        }
                    }
                }
                ToolbarItem(placement: .navigationBarLeading) {
                    BackPressButton(action: onBackPressed)
                }
            }
    }
}

extension View {
    func withCustomBackButton(_ onBackPressed: @escaping @MainActor () -> Unit) -> some View {
        modifier(BackButtonModifier(title: "", onBackPressed: onBackPressed))
    }
    
    func withCustomBackButton(title: String,_ onBackPressed: @escaping @MainActor () -> Unit) -> some View {
        modifier(BackButtonModifier(title: title, onBackPressed: onBackPressed))
    }
}


struct BackButton: View {

    let title: String
    let action: @MainActor () -> Void
    
    init(title: String = "", action: @MainActor @escaping () -> Void) {
        self.title = title
        self.action = action
    }
    
    var body: some View {
        HStack {
            Button(action: {
                action()
            }) {
                HStack {
                    ImageSystem(systemIcon: "chevron.backward", tint: Color(red: 9 / 255, green: 131 / 255, blue: 1))
                        .frame(width: 12, height: 18, alignment: .topLeading)
                    Text(
                        "Back"
                    ).font(.system(size: 17))
                        .foregroundStyle(
                            Color(red: 9 / 255, green: 131 / 255, blue: 1)
                        ).padding(leading: -4)
                }.frame(width: 90, height: 45)
            }
            Spacer()
            if !title.isEmpty {
                Text(title).font(.headline).foregroundStyle(Color(red: 9 / 255, green: 131 / 255, blue: 1)).onCenter()
                Spacer()
            }
        }
    }
}



struct BackPressButton: View {

    let action: @MainActor () -> Void
    
    init(action: @MainActor @escaping () -> Void) {
        self.action = action
    }
    
    var body: some View {
        Button(action: {
            action()
        }) {
            HStack {
                Image(
                    uiImage: UIImage(
                        named: "chevron.backward"
                    )?.withTintColor(
                        UIColor(Color.cyan)
                    ) ?? UIImage()
                ).resizable()
                    .imageScale(.medium)
                    .scaledToFit().frame(
                        width: 20, height: 22, alignment: .topLeading
                    )
                Text(
                    "Back"
                ).font(.system(size: 17))
                    .foregroundStyle(
                        Color(red: 9 / 255, green: 131 / 255, blue: 1)
                    ).padding(leading: -7)
            }.frame(width: 90, height: 45)
        }
    }
}

struct ToolBarButton: View {
    let icon: String
    let action: () -> Void
    
    var body: some View {
        VStack {
            Spacer().frame(height: 10)
            Button(action: {
                action()
            }) {
                VStack {
                    VStack {
                        ImageAsset(
                            icon: icon,
                            tint: Color.black
                        )
                    }.padding(7).background(Color.red)
                }.clipShape(Circle())
            }.frame(width: 35, height: 35)
        }
    }
}

