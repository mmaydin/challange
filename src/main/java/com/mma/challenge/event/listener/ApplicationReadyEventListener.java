package com.mma.challenge.event.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mma.challenge.service.KafkaService;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	@Value("${challange.logPath}")
	private String logPath;
	
	private Date lastLogCreatedAt = new Date();
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS");
	
	@Autowired
	private KafkaService kafkaService;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		this.checkFileChanges();
	}

	public void checkFileChanges() {
		try {
	        WatchService watcher = FileSystems.getDefault().newWatchService();

	        Path path = Paths.get(logPath);
	        path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
	
	        while (true) {
	            WatchKey key;
	            try {
	                key = watcher.take();
	            } catch (InterruptedException e) {
	                return;
	            }
	
	            for (WatchEvent<?> event : key.pollEvents()) {
	            	WatchEvent<Path> ev = cast(event);
	            	Path filename = ev.context();

	            	String filePath = logPath + filename;

	            	List<String> lines = new ArrayList<String>();

                	try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {

                        // read line by line
                        String line;
                        Date lastLineDate = null;
                        while ((line = br.readLine()) != null) {
                        	String[] lineParts = line.split("\t");
                        	if (lineParts != null && lineParts.length > 0) {
                        		try {
									Date logDate = dateFormatter.parse(lineParts[0]);
									
									if (logDate.after(lastLogCreatedAt)) {

										lines.add(line);

										lastLineDate = logDate;
									}
								} catch (ParseException e) {
								}
                        	}
                        }

                        if (lastLineDate != null) {
                        	lastLogCreatedAt = lastLineDate;
                        }

                        kafkaService.sendCityLog(String.join("\n", lines));

                    } catch (IOException e) {
                        System.err.format("IOException: %s%n", e);
                    }
	            }

	            key.reset();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

}
