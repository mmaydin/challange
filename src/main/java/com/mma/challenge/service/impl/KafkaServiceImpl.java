package com.mma.challenge.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.mma.challenge.entity.LogLevelType;
import com.mma.challenge.repository.CityLogRepository;
import com.mma.challenge.service.KafkaService;

@Service
public class KafkaServiceImpl implements KafkaService {

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS");

	@Autowired
	private CityLogRepository cityLogRepository;
	
	@Value("${kafka.topic}")
    private String topic;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
    private SimpMessagingTemplate template;

	public void sendCityLog(String lines) {
		if (lines != null && !lines.equals("")) {
			kafkaTemplate.send(topic, lines);
		}
	}

	@KafkaListener(topics = "${kafka.topic}")
	public void readCityLog(@Payload String lines) {
		if (lines != null && !lines.equals("")) {			
			String[] lineArray = lines.split("\n");
			if (lineArray.length > 0) {

				List<CityLog> cityLogs = new ArrayList<CityLog>();
				for (String line: lineArray) {
					String[] lineParts = line.split("\t");
					if (lineParts.length > 0) {
						CityLog cityLog = new CityLog();
						try {
							cityLog.setCreatedAt(dateFormatter.parse(lineParts[0]));
						} catch (ParseException e) {
						}
						cityLog.setCityName(lineParts[2]);
						cityLog.setLevel(LogLevelType.valueOf(lineParts[1]));
						cityLog.setDetail(lineParts[3]);
						
						cityLogs.add(cityLog);
						
						template.convertAndSend("/topic/citylog", cityLogs);
					}
				}
				
				if (cityLogs.size() > 0) {
					cityLogRepository.saveAll(cityLogs);
				}
			}
		}
	}

}