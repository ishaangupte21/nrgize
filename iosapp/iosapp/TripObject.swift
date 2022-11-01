//
//  TripObject.swift
//  iosapp
//
//  Created by Prajakta Gupte on 10/30/22.
//

class TripCoordinate: Codable, Identifiable {
    public var latitude: Float;
    public var longitude: Float;
}

class TripObject: Codable, Identifiable {
    public var id: Int;
    public var startAddress: String;
    public var stopAddress: String;
    public var travelDate: String;
    public var created: Int64;
    public var startCoords: TripCoordinate;
    public var endCoords: TripCoordinate;
}
