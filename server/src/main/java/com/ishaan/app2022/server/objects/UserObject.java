package com.ishaan.app2022.server.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// This is the User object for authentication and data
@Entity
@NoArgsConstructor
public class UserObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @Getter
    private String email;

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @Getter
    @OneToMany(mappedBy = "owner")
    private List<TripObject> trips;

    public UserObject(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
