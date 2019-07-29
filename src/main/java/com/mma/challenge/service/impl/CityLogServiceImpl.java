package com.mma.challenge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mma.challenge.entity.CityLog;
import com.mma.challenge.repository.CityLogRepository;
import com.mma.challenge.service.CityLogService;

@Service
public class CityLogServiceImpl implements CityLogService {

	@Autowired
	private CityLogRepository cityLogRepository;

	@Override
	public CityLog save(CityLog cityLog) {
		return cityLogRepository.save(cityLog);
	}

	@Override
	public List<CityLog> saveAll(List<CityLog> cityLogs) {
		return cityLogRepository.saveAll(cityLogs);
	}

	@Override
	public List<CityLog> findByCityName(String cityName) {
		return cityLogRepository.findByCityName(cityName);
	}

}