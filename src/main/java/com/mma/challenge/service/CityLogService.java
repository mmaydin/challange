package com.mma.challenge.service;

import java.util.List;

import com.mma.challenge.entity.CityLog;

public interface CityLogService {

    public CityLog save(CityLog cityLog);

    public List<CityLog> saveAll(List<CityLog> cityLogs);

    public List<CityLog> findByCityName(String cityName);

    public List<CityLog> findAll();

}