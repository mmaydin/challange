package com.mma.challenge.service;

import java.util.List;

public interface KafkaService {

	public void sendCityLog(List<String> lines);

}