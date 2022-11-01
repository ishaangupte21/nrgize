//
//  TripViewModal.swift
//  iosapp
//
//  Created by Prajakta Gupte on 10/31/22.
//

import SwiftUI
import MapKit
import Awesome

struct TripViewModal: View {
    
    @Binding public var open: Bool;
    @Binding public var trip: TripObject?;
    @ObservedObject public var authState: AuthContext
    @State public var closestStations: [ChargingStationObject] = [];
    @State public var gotData = false;
    
    var body: some View {
        if gotData {
            VStack {
                MapView(start: trip!.startCoords, end: trip!.endCoords, stations: self.closestStations);
                Button("Close", action: {
                    open = false;
                }).padding()
            }
        } else {
            Text("loading").onAppear {
                getNearestStations()
            }
        }
    }
    
    public func getNearestStations() {
        print("sending request")
        var request = URLRequest(url: URL(string: "http://localhost:8080/stations/nearest?latitude=\(trip!.startCoords.latitude)&longitude=\(trip!.startCoords.longitude)")!);
        request.httpMethod = "GET";
        request.addValue("Bearer \(authState.authToken!)", forHTTPHeaderField: "Authorization");
        print(authState.authToken!)
        URLSession.shared.dataTask(with: request) { data, response, error in
            do {
                print(response)
                guard let data = data else {return};
                let response = try JSONDecoder().decode([ChargingStationObject].self, from: data)
                DispatchQueue.main.async {
                    print("got response")
                    self.closestStations = response;
                    self.gotData = true;
                }
                } catch {
                print(error);
            }
        }.resume();
    }
}

struct MapView: UIViewRepresentable {
    func updateUIView(_ uiView: MKMapView, context: Context) {
    }
    
    typealias UIViewType = MKMapView;
    
    public var start: TripCoordinate, end: TripCoordinate;
    
    public var stations: [ChargingStationObject];
    
    func makeUIView(context: Context) -> MKMapView {
        let mapView = MKMapView();
        mapView.delegate = context.coordinator;
        let region = MKCoordinateRegion(
            center: CLLocationCoordinate2D(latitude: 40.71, longitude: -70.4),
            span: MKCoordinateSpan(latitudeDelta: 0.5, longitudeDelta: 0.5)
        );
        
        mapView.setRegion(region, animated: true);
        
        let p1 = MKPlacemark(coordinate: CLLocationCoordinate2D(latitude: CLLocationDegrees(start.latitude), longitude: CLLocationDegrees(start.longitude)));
        let p2 = MKPlacemark(coordinate: CLLocationCoordinate2D(latitude: CLLocationDegrees(end.latitude), longitude: CLLocationDegrees(end.longitude)));
        
        let request = MKDirections.Request();
        
        for station in stations {
//            let annotation = MKAnnotation(coordinate: CLLocationCoordinate2D(latitude: station.latitude, longitude: station.longitude)) {
//                        ZStack {
//                            Awesome.Solid.plug.image.size(15).foregroundColor(Color.white);
//                        }
//                                .frame(width: 15, height: 15)
//                                .background {
//                                    ZStack {
//                                        Circle().fill(Color.green).frame(width: 20, height: 20);
//                                    }
//                                }
//            };
            let annotation = MKPointAnnotation();
            annotation.coordinate = CLLocationCoordinate2D(latitude: station.latitude, longitude: station.longitude);
            annotation.title = station.station_name;
            mapView.addAnnotation(annotation);
        }
        
        request.source = MKMapItem(placemark: p1);
        request.destination = MKMapItem(placemark: p2);
        request.transportType = .automobile;
        let directions = MKDirections(request: request)
         directions.calculate { response, error in
             guard let route = response?.routes.first else { print(error); return;}
           mapView.addAnnotations([p1, p2])
             print("got route successfully")
             mapView.addOverlay(route.polyline)
             print(route.polyline)
           mapView.setVisibleMapRect(
             route.polyline.boundingMapRect,
             edgePadding: UIEdgeInsets(top: 20, left: 40, bottom: 20, right: 40),
             animated: true)
         }
        
        return mapView;
   }

    func makeCoordinator() -> MapViewCoordinator {
        return MapViewCoordinator();
    }
    
    class MapViewCoordinator: NSObject, MKMapViewDelegate {
        func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
            print("called renderer")
              let renderer = MKPolylineRenderer(overlay: overlay)
              renderer.strokeColor = .systemBlue
              renderer.lineWidth = 5
            renderer.alpha = 1.0
              return renderer
            }
    }
}
