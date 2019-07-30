package com.mma.challenge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mma.challenge.entity.CityLog;
import com.mma.challenge.service.CityLogService;

@RestController
@RequestMapping(value = "/api/citylog", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityLogController {

    @Autowired
    private CityLogService cityLogService;

    @GetMapping
    public List<CityLog> findAll() {
        return cityLogService.findAll();
    }

    @GetMapping("/{cityName}")
    public List<CityLog> findByCityName(@PathVariable String cityName) {
        return cityLogService.findByCityName(cityName);
    }

}