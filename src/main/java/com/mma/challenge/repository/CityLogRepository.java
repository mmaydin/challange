package com.mma.challenge.repository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mma.challenge.entity.CityLog;

@Repository
public interface CityLogRepository extends MongoRepository<CityLog, String> {

	Set<CityLog> findByCityName(String cityName);
}
