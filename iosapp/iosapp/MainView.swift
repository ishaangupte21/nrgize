//
// Created by Prajakta Gupte on 10/29/22.
//

import SwiftUI
import Awesome
import CoreLocation

struct MainView: View{
    @ObservedObject public var authState: AuthContext;
    @State private var dataLoaded = false;
    @State private var defaultStations: [ChargingStationObject] = [];
    @State private var tripsLoaded = false;
    @State private var userTrips: [TripObject] = [];

    var body: some View {
        TabView {
            DashboardView(tripsLoaded: $tripsLoaded, userTrips: $userTrips, authState: authState).tabItem {
                Label(title: {Text("Dashboard")}, icon: {Awesome.Solid.houseUser.image})
            }.onAppear {
                getAllTrips();
            }
            FullMapView(dataLoaded: $dataLoaded, chargingStations: $defaultStations).tabItem {
                Label(title: {Text("View Map")}, icon: {Awesome.Solid.map.image})
            }.onAppear {
                        getAllChargingStations();
                    }
            CreateTripView(authState: authState, userTrips: $userTrips).tabItem {
                Label(title: {Text("Create Trip")}, icon: {Awesome.Solid.plusCircle.image})
            }
        }.accentColor(Color.green)
    }
    public func getAllChargingStations() {
        var request = URLRequest(url: URL(string: "http://localhost:8080/stations/all")!);
        request.httpMethod = "GET";
        request.addValue("Bearer " + authState.authToken!, forHTTPHeaderField: "Authorization");
        print(authState.authToken!)
        print("sending request")
        URLSession.shared.dataTask(with: request) { data, response, error in
                    if let data = data {
//                        print(String(decoding: data, as: UTF8.self));
                                if let response = try? JSONDecoder().decode([ChargingStationObject].self, from: data) {
                                DispatchQueue.main.async {
                                    self.defaultStations =  response
                                    self.dataLoaded = true;
                                }
                                    return
                                } else {
                                    print("unable to decode")
                                }
                    } else {
                    }
                }.resume()
    }
    
    public func getAllTrips() {
        var request = URLRequest(url: URL(string: "http://localhost:8080/trip/all")!);
        request.httpMethod = "GET";
        request.addValue("Bearer " + authState.authToken!, forHTTPHeaderField: "Authorization");
        URLSession.shared.dataTask(with: request) {data, response, error in
//            print(response! as Any);
            guard let data = data else {return};
            do {
                let response = try JSONDecoder().decode([TripObject].self, from: data)
                    DispatchQueue.main.async {
                        self.userTrips = response;
                        self.tripsLoaded = true;
                    }
                
            } catch {
                print(error);
            }
        }.resume()
    }
}
