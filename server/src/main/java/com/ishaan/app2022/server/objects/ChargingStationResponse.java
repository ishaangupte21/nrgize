package com.ishaan.app2022.server.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

// This is the response to be obtained from the Energy.gov API
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargingStationResponse implements Serializable {
    @JsonProperty("fuel_stations")
    private List<ChargingStation> stations;
}
