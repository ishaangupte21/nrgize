//
// Created by Prajakta Gupte on 10/29/22.
//

import Foundation

class ChargingStationObject: Codable, Identifiable {
    public var id: Int;
    public var station_name: String;
    public var latitude: Double;
    public var longitude: Double;
    public var street_address: String;
    public var city: String;
    public var state: String;
    public var zip: String;
    public var access_days_time: String?;
    public var ev_connector_types: [String];
    public var facility_type: String?
}
