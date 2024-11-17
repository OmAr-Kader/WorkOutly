//
//  HomeScreen.swift
//  iosApp
//
//  Created by OmAr on 16/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct HomeScreen : View {
    var body: some View {
        VStack {
            ImageCacheView("https://goodrequest-web-development.s3.eu-central-1.amazonaws.com/61bc806440f0c568c0900490_Jetpack_20_Compose_20_Basics_20_20_Modal_20_Bottom_20_Sheet_bb7392c41d.png")
                .frame(width: 150, height: 150)
            ImageCacheView("https://gymvisual.com/modules/productmedia/uploads/93411201preview.mp4", isVideoPreview: true)
                .frame(width: 150, height: 150)
        }
    }
}
