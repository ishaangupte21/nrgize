//
// Created by Prajakta Gupte on 10/29/22.
//

import SwiftUI
import Awesome

struct AuthView: View {
    @ObservedObject var authState: AuthContext;
    var body: some View {
        Text("NRgize").font(.title.bold()).padding()
        Button(action: {
            authState.signIn()
        }) {
            Label(
                    title: {
                        Text("Sign in With Google").font(.title3.bold());
                    },
                    icon: {
                        Awesome.Brand.google.image.size(30).font(.title3.bold()).foregroundColor(Color.white);
                    }
            ).padding()
                    .background(Color.green)
                    .foregroundColor(Color.white)
                    .cornerRadius(5.0)
        }
    }

    func handleSignInButton() {
        authState.signIn();
    }

}
