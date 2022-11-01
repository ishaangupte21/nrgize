package com.ishaan.app2022.server.repositories;

import com.ishaan.app2022.server.objects.UserObject;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// JPA Interface for users
public interface UserRepository extends CrudRepository<UserObject, Integer> {
    Optional<UserObject> findByEmail(String email);
}
