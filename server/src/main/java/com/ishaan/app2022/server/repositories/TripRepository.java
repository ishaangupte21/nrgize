package com.ishaan.app2022.server.repositories;

import com.ishaan.app2022.server.objects.TripObject;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<TripObject, Integer> {
}
