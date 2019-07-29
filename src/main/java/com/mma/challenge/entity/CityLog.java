package com.mma.challenge.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(collection = "city_logs")
public class CityLog implements Serializable {

	private static final long serialVersionUID = 1397274908126381148L;

	@JsonIgnore
	@Id
    private String id;

    private Date createdAt;

    private LogLevelType level;

    @Indexed
    private String cityName;

    private String detail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public LogLevelType getLevel() {
		return level;
	}

	public void setLevel(LogLevelType level) {
		this.level = level;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}