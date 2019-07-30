package com.mma.challenge.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mma.challenge.entity.CityLogData;
import com.mma.challenge.service.KafkaService;
import com.mma.challenge.util.MainUtil;

@Component
public class ReadLogTask {

	@Value("${challange.logPath}")
	private String logPath;
	
	@Autowired
	private WatchService watchService;

	@Autowired
	private KafkaService kafkaService;

	private Date lastLogCreatedAt = new Date();	

	@Scheduled(fixedDelay = 500)
	public void readCityLog() {
		WatchKey key;
        try {
            key = watchService.take();
        } catch (InterruptedException e) {
            return;
        }

        for (WatchEvent<?> event : key.pollEvents()) {
        	WatchEvent<Path> ev = cast(event);
        	Path filename = ev.context();

        	String filePath = logPath + filename;

        	try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {

        		CityLogData cityLogData = new CityLogData();

                // read line by line
                String line;
                Date lastLineDate = null;
                while ((line = br.readLine()) != null) {
                	String[] lineParts = line.split("\t");
                	if (lineParts != null && lineParts.length > 0) {
                		try {
                			Date logDate = MainUtil.getDateFormatter().parse(lineParts[0]);

                			if (logDate.after(lastLogCreatedAt)) {
                				cityLogData.addCityLog(line);

                				lastLineDate = logDate;
                			}
            			} catch (ParseException e) {

            			}
                	}
                }

                if (lastLineDate != null) {
                    lastLogCreatedAt = lastLineDate;
                }

                kafkaService.sendCityLog(cityLogData);

            } catch (IOException e) {

            }
        }

        key.reset();
	}
	
	@SuppressWarnings("unchecked")
	private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>)event;
	}

}