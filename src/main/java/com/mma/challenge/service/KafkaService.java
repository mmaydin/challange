package com.mma.challenge.service;

import com.mma.challenge.entity.CityLogData;

public interface KafkaService {

	public void sendCityLog(CityLogData cityLogData);

}