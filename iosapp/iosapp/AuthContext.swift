//
// Created by Ishaan Gupte on 10/29/22.
//
import GoogleSignIn

@MainActor
class AuthContext : ObservableObject {
    // This is the main Sign in object for google

    // This holds the current JWT
    @Published public var authToken: String? = nil;

    init() {
        GIDSignIn.sharedInstance.configuration =
                GIDConfiguration(clientID: "718436457626-faql8kkjqmnaiafkf5uh9n0auaaff8h9.apps.googleusercontent.com",
                        serverClientID: "718436457626-ce8o9icup3k9v4ama8tio39rt922hkd4.apps.googleusercontent.com");
    }

    public func signOut() {
        authToken = nil;
    }

    public func signIn() {
        guard let presentingController = (UIApplication.shared.connectedScenes.first as? UIWindowScene)?.windows.first?.rootViewController else {return};

        GIDSignIn.sharedInstance.signIn(withPresenting: presentingController, completion: {user, error in
            guard error == nil else { return }
            guard user != nil else { return }

            user?.authentication.do{authentication, error in
                guard error == nil else { return }
                guard authentication != nil else { return }

                let googleTok = authentication?.idToken;

                // Now we can send an HTTp post request to the server
                guard let reqBody = try? JSONEncoder().encode(["googleToken": googleTok]) else {return};
                var request = URLRequest(url: URL(string: "http://localhost:8080/auth/authenticate")!);
                request.httpMethod = "POST";
                request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                let task = URLSession.shared.uploadTask(with: request, from: reqBody) { [self] data, response, error in
                    let decoded: [String: String]? = try? JSONDecoder().decode([String: String].self, from: data!);
                    authToken = decoded!["token"];
                }
                task.resume()
            }
        })
    }
}
