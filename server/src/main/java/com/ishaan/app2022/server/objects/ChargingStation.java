package com.ishaan.app2022.server.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

// This is the Charging Station class which will be populated by the REST request
// and redis cache.
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargingStation implements Serializable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("station_name")
    private String name;

    @JsonProperty("latitude")
    private float latitude;

    @JsonProperty("longitude")
    private float longitude;

    @JsonProperty("street_address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("access_days_time")
    private String accessDaysTime;

    @JsonProperty("ev_connector_types")
    private List<String> connectorTypes;

    @JsonProperty("facility_type")
    private String facilityType;
}
