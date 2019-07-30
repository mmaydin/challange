package com.mma.challenge.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityLogData implements Serializable {

	private static final long serialVersionUID = 1447744396577345013L;

	private List<String> cityLogs = new ArrayList<String>();

	public void addCityLog(String cityLog) {
		cityLogs.add(cityLog);
	}

	public List<String> getCityLogs() {
		return cityLogs;
	}

	public void setCityLogs(List<String> cityLogs) {
		this.cityLogs = cityLogs;
	}

}