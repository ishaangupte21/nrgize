package com.ishaan.app2022.server.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CoordinateResponse implements Serializable {
    private List<CoordinatePair> data;
}
