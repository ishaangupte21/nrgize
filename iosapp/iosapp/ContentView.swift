//
//  ContentView.swift
//  iosapp
//
//  Created by Prajakta Gupte on 10/29/22.
//
//

import SwiftUI

struct ContentView: View {
    @StateObject var authState = AuthContext();
    var body: some View {
        Group {
            if authState.authToken != nil {
                MainView(authState: authState)
            } else {
                AuthView(authState: authState)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
