package com.ishaan.app2022.server.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishaan.app2022.server.objects.ChargingStation;
import com.ishaan.app2022.server.objects.ChargingStationResponse;
import com.ishaan.app2022.server.objects.CoordinatePair;
import com.ishaan.app2022.server.objects.CoordinateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Interface for interacting with the Energy.gov API
@Service
@CacheConfig(cacheNames = {"ChargingStationData"})
public class RestService {
    private final RestTemplate template;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${energy.api.key}")
    private String API_KEY;

    public RestService(RestTemplateBuilder builder) {
        template = builder.build();
    }

    @Value("${positionstack.key}")
    private String POSITION_STACK_KEY;

    @Cacheable(key = "#root.methodName")
    public List<ChargingStation> getChargingStations() throws JsonProcessingException {
        String url = String.format("https://developer.nrel.gov/api/alt-fuel-stations/v1.json?api_key=%s&access=public&fuel_type=ELEC&state=NJ&limit=20", API_KEY);
        String jsonData = template.getForObject(url, String.class);
        ChargingStationResponse response = MAPPER.readValue(jsonData, ChargingStationResponse.class);
        return response.getStations();
    }

    public CoordinatePair[] getCoordinatesFromAddress(String startAddress, String endAddress) throws JsonProcessingException {
        CoordinatePair[] coords = new CoordinatePair[2];
        // first, the start address
        String url1 = String.format("http://api.positionstack.com/v1/forward?access_key=%s&query=%s&output=json&limit=1", POSITION_STACK_KEY, startAddress);
        String jsonStr = template.getForObject(url1, String.class);
//        CoordinatePair add1Data= MAPPER.readValue(jsonStr, new TypeReference<ArrayList<CoordinatePair>>() {}).get(0);
        CoordinateResponse add1Data = MAPPER.readValue(jsonStr, CoordinateResponse.class);
        coords[0] = add1Data.getData().get(0);
        // now, the end address
        String url2 = String.format("http://api.positionstack.com/v1/forward?access_key=%s&query=%s&output=json&limit=1", POSITION_STACK_KEY, endAddress);
        jsonStr = template.getForObject(url2, String.class);
//        CoordinatePair add2Data = MAPPER.readValue(jsonStr, new TypeReference<ArrayList<CoordinatePair>>(){}).get(0);
        CoordinateResponse add2Data = MAPPER.readValue(jsonStr, CoordinateResponse.class);
        coords[1] = add2Data.getData().get(0);

        return coords;
    }

    public List<ChargingStation> getChargingStationsNearPoint(float latitude, float longitude) throws JsonProcessingException {
        String url = String.format("https://developer.nrel.gov/api/alt-fuel-stations/v1/nearest.json?api_key=%s&latitude=%f&longitude=%f&limit=4", API_KEY, latitude, longitude);
        String jsonStr = template.getForObject(url, String.class);

        ChargingStationResponse resp = MAPPER.readValue(jsonStr, ChargingStationResponse.class);
        return resp.getStations();
    }
}
