//
// Created by Prajakta Gupte on 10/29/22.
//

import SwiftUI
import CoreLocation
import MapKit

struct CreateTripView: View {
    
    @State private var startAddress = "";
    @State private var endAddress = "";
    @State private var date = Date();
    @ObservedObject public var authState: AuthContext;
    @Binding public var userTrips: [TripObject];
    
    private let completer = MKLocalSearchCompleter();
    
    var body: some View {
        
        VStack {
            Text("Create a Trip").font(.title);
            Form {
                TextField("Start address", text: $startAddress).padding()
                
                TextField("End address", text: $endAddress).padding();
                
                DatePicker("Travel Date", selection: $date, displayedComponents: [.date])
                    .padding();
                
                Button(action: {
                    handleCreateTripSubmit();
                }) {
                    Text("Create").font(.title3.bold()).padding();
                }.background(Color.green).foregroundColor(Color.white)
                    .cornerRadius(5.0).padding();
            }
        }
    }
    
    func handleCreateTripSubmit() {
        var request = URLRequest(url: URL(string: "http://localhost:8080/trip/create")!);
        request.httpMethod = "POST";
        request.addValue("Bearer \(authState.authToken!)", forHTTPHeaderField: "Authorization");
        request.addValue("application/json", forHTTPHeaderField: "Content-Type");
        let formatter = DateFormatter();
        formatter.dateStyle = .short;
        let body: [String: String] = ["startAddress": startAddress, "stopAddress": endAddress, "travelDate": formatter.string(from: date)];
        guard let reqBody = try? JSONEncoder().encode(body) else {return};
        URLSession.shared.uploadTask(with: request, from: reqBody) {data, response, error in
            guard let data = data else {return};
            guard let response: TripObject = try? JSONDecoder().decode(TripObject.self, from: data) else {return};
            DispatchQueue.main.async {
                self.userTrips.append(response);
            }
        }.resume();
    }
}
