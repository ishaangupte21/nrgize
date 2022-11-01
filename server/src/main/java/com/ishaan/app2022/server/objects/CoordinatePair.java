package com.ishaan.app2022.server.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinatePair implements Serializable {
   private float latitude, longitude;
}
