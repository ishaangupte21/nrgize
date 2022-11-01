package com.ishaan.app2022.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishaan.app2022.server.objects.CoordinatePair;
import com.ishaan.app2022.server.objects.TripObject;
import com.ishaan.app2022.server.objects.UserObject;
import com.ishaan.app2022.server.repositories.TripRepository;
import com.ishaan.app2022.server.repositories.UserRepository;
import com.ishaan.app2022.server.rest.RestService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/trip")
public class TripController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RestService restService;

    @PostMapping("/create")
    public ResponseEntity<?> handleCreateTrip(@RequestBody CreateTripRequest body, HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("email");
        System.out.println("date: " + body.getTravelDate());
        try {
            CoordinatePair[] coords = restService.getCoordinatesFromAddress(body.getStartAddress(), body.getStopAddress());
            UserObject user = userRepository.findByEmail(userEmail).get();
            TripObject newTrip = new TripObject(body.getStartAddress(), body.getStopAddress(), body.getTravelDate(), System.currentTimeMillis(),
                    coords[0], coords[1], user
                    );
            tripRepository.save(newTrip);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTrip);

        } catch(JsonProcessingException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid addresses"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> handleGetAllTrips(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        UserObject user = userRepository.findByEmail(email).get();
        return ResponseEntity.ok(user.getTrips());
    }

    @Data
    @NoArgsConstructor
    private static class CreateTripRequest {
        private String startAddress;
        private String stopAddress;
        private String travelDate;
    }
}
