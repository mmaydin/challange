package com.mma.challenge.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.mma.challenge.entity.CityLog;
import com.mma.challenge.entity.CityLogData;
import com.mma.challenge.entity.LogLevelType;
import com.mma.challenge.service.CityLogService;
import com.mma.challenge.service.KafkaService;
import com.mma.challenge.util.MainUtil;

@Service
public class KafkaServiceImpl implements KafkaService {

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, CityLogData> kafkaTemplate;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private CityLogService cityLogService;

    public void sendCityLog(CityLogData cityLogData) {
        if (cityLogData != null && cityLogData.getCityLogs().size() > 0) {

            kafkaTemplate.send(topic, cityLogData);
        }
    }

    @KafkaListener(topics = "${kafka.topic}")
    public void readCityLog(@Payload CityLogData cityLogData) {
        if (cityLogData != null && cityLogData.getCityLogs().size() > 0) {

            List<CityLog> cityLogs = new ArrayList<CityLog>();
            for (String line : cityLogData.getCityLogs()) {
                String[] lineParts = line.split("\t");
                if (lineParts.length > 0) {
                    CityLog cityLog = new CityLog();
                    try {
                        cityLog.setCreatedAt(MainUtil.getDateFormatter().parse(lineParts[0]));
                    } catch (ParseException e) {
                    }
                    cityLog.setCityName(lineParts[2]);
                    cityLog.setLevel(LogLevelType.valueOf(lineParts[1]));
                    cityLog.setDetail(lineParts[3]);

                    cityLogs.add(cityLog);
                }
            }

            if (cityLogs.size() > 0) {
                template.convertAndSend("/topic/citylog", cityLogs);

                cityLogService.saveAll(cityLogs);
            }
        }
    }

}