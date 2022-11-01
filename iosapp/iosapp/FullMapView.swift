//
// Created by Prajakta Gupte on 10/29/22.
//

import SwiftUI
import MapKit
import Awesome

struct FullMapView: View{
    @StateObject private var manager = LocationManager();
    @Binding public var dataLoaded: Bool;
    @Binding public var chargingStations: [ChargingStationObject];
    @State private var popUpOpen = false;
    @State private var currStation: ChargingStationObject? = nil;

    var body: some View {
        if dataLoaded {
            ZStack {
                Map(coordinateRegion: $manager.region, annotationItems: chargingStations) {
                    place in
                    MapAnnotation(coordinate: CLLocationCoordinate2D(latitude: place.latitude, longitude: place.longitude)) {
                        ZStack {
                            Awesome.Solid.plug.image.size(15).foregroundColor(Color.white);
                        }
                                .frame(width: 15, height: 15)
                                .background {
                                    ZStack {
                                        Circle().fill(Color.green).frame(width: 20, height: 20);
                                    }
                                }
                                .onTapGesture {
                                    self.currStation = place;
                                    self.popUpOpen = true;
                                }
                    }
                }
                        .edgesIgnoringSafeArea(.top)
//                Rectangle().size(width: 50, height: 50).background(Color.white)
            }.sheet(isPresented: $popUpOpen) {
                if self.currStation != nil {
                    ChargingStationModal(currStation: $currStation);
                }
                    }
        } else {
            Text("Data Loading")
        }
    }

}
