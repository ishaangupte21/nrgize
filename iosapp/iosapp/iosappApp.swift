//
//  iosappApp.swift
//  iosapp
//
//  Created by Prajakta Gupte on 10/29/22.
//
//

import SwiftUI
import GoogleSignIn

@main
struct iosappApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView().onOpenURL{url in GIDSignIn.sharedInstance.handle(url)};
        }
    }
}
