package com.ishaan.app2022.server.controllers;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishaan.app2022.server.objects.ChargingStation;
import com.ishaan.app2022.server.rest.RestService;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

// This is the REST controller to get charging station data to the app
@RestController
@CrossOrigin
@RequestMapping("/stations")
public class ChargingStationController {
    @Autowired
    private RestService restService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllChargingStations() throws JsonProcessingException {
//        This method will just return all the charging stations
        return ResponseEntity.ok(restService.getChargingStations());
    }

    @GetMapping("/within")
    public ResponseEntity<?> getChangingStationsWithin(@RequestParam("radius") int radius, @RequestParam("latitude") float latitude, @RequestParam("longitude") float longitude) throws JsonProcessingException {
        List<ChargingStation> rawStations = restService.getChargingStations();
        List<ChargingStationWithDistance> stationsWithDistance = new ArrayList<>();
        for (ChargingStation s : rawStations) {
            double distance = computeDistance(latitude, longitude, s);
            if (distance <= radius) {
                stationsWithDistance.add(new ChargingStationWithDistance(s, distance));
            }
        }
        Collections.sort(stationsWithDistance);

        return ResponseEntity.ok(stationsWithDistance);
    }

    @GetMapping("/nearest")
    public ResponseEntity<?> getChargingStationsNearest(@RequestParam("latitude") float latitude, @RequestParam("longitude") float longitude) throws JsonProcessingException {
        System.out.println(latitude);
        System.out.println(longitude);
        return ResponseEntity.ok(restService.getChargingStationsNearPoint(latitude, longitude));
    }

    @Data
    private static class ChargingStationWithDistance implements Serializable, Comparable<ChargingStationWithDistance> {
        @JsonUnwrapped
        @NonNull
        private ChargingStation station;

        @NonNull
        private double distance;

        @Override
        public int compareTo(ChargingStationWithDistance o) {
            return Double.compare(distance, o.getDistance());
        }
    }

    private static final int MILES_CONSTANT = 3956;

    private static double computeDistance(float latitude, float longitude, ChargingStation second) {
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(second.getLatitude());
        double lon1 = Math.toRadians(longitude);
        double lon2 = Math.toRadians(second.getLongitude());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        return c * MILES_CONSTANT;
    }
}
