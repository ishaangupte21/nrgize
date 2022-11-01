package com.ishaan.app2022.server.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class TripObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    private String startAddress;

    @Getter
    private String stopAddress;

    @Getter
    private String travelDate;

    @Getter
    private long created;

    @Getter
    @Embedded
    @AttributeOverrides(value={
           @AttributeOverride(name="latitude", column = @Column(name="start_latitude")),
            @AttributeOverride(name="longitude", column = @Column(name="start_longitude"))
    })
    private CoordinatePair startCoords;

    @Getter
    @Embedded
    @AttributeOverrides(value={
            @AttributeOverride(name="latitude", column = @Column(name="end_latitude")),
            @AttributeOverride(name="longitude", column = @Column(name="end_longitude"))
    })
    private CoordinatePair endCoords;

    public TripObject(String startAddress, String stopAddress, String travelDate, long created, CoordinatePair startCoords, CoordinatePair endCoords, UserObject owner) {
        this.startAddress = startAddress;
        this.stopAddress = stopAddress;
        this.travelDate = travelDate;
        this.created = created;
        this.startCoords = startCoords;
        this.endCoords = endCoords;
        this.owner = owner;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private UserObject owner;

}
