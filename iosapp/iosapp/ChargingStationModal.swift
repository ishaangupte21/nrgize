//
//  ChargingStationModal.swift
//  iosapp
//
//  Created by Prajakta Gupte on 10/29/22.
//

import SwiftUI

struct ChargingStationModal: View {
    @Binding public var currStation: ChargingStationObject?;
    
    var body: some View {
                    VStack {
                        Text(self.currStation!.station_name).font(.title).padding();
                        Text(self.currStation!.street_address).font(.title2).padding();
                        Text("\(self.currStation!.city), \(self.currStation!.state) \(self.currStation!.zip)").font(.title2).padding();
                        Text("Connector Types").font(.title3);
                        List(self.currStation!.ev_connector_types, id: \.self) {
                           str in Text(str)
                        }
                    }
    }
}

