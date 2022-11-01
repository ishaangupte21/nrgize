//
// Created by Prajakta Gupte on 10/29/22.
//

import SwiftUI

struct DashboardView: View{
    @Binding public var tripsLoaded: Bool
    @Binding public var userTrips: [TripObject];
    @ObservedObject public var authState: AuthContext
    @State public var overlayOpen = false;
    @State public var currTrip: TripObject? = nil;
    
    var body: some View {
        if tripsLoaded {
            VStack {
                Text("My Trips").font(.title.bold());
                List(0..<userTrips.count, id: \.self) { i in
                    VStack {
                        Text("From: \(userTrips[i].startAddress)")
                        Text("To: \(userTrips[i].stopAddress)")
                        Text("On: \(userTrips[i].travelDate)")
                    }.onTapGesture {
                        currTrip = userTrips[i];
                        overlayOpen = true;
                    }
                }
            }.sheet(isPresented: $overlayOpen, onDismiss:{self.overlayOpen = false}) {
                if currTrip != nil {
                    TripViewModal(open: $overlayOpen, trip: $currTrip, authState: authState)
                }
            }
        } else {
            Text("Fetching Trips...")
        }
    }
}
