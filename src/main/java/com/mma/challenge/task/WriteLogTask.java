package com.mma.challenge.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mma.challenge.entity.LogLevelType;
import com.mma.challenge.utils.MainUtil;

@Component
public class WriteLogTask {

	private static Logger log = LoggerFactory.getLogger(WriteLogTask.class);
	
	@Value("${challange.logPath}")
	private String logPath;
	
	@Value("${challange.maxLogSize}")
	private Integer maxLogSize; 
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS");
	
	@Scheduled(cron = "*/3 * * * * ?")
	public void writeCityLog() {
		//log.info("WRITE CITY LOG START");

		List<LogLevelType> logLevelList = Arrays.asList(LogLevelType.values());
		
		List<String> cities = new ArrayList<String>();
		cities.add("Istanbul");
		cities.add("Tokyo");
		cities.add("Moscow");
		cities.add("Beijing");
		cities.add("London");
		
		File directory = new File(logPath);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		
	    File[] files = MainUtil.sortFilesByName(directory.listFiles());
	    String writePath;
	    
	    
	    if (files != null) {
	    	Integer fileCount = files.length;
	    	if (fileCount == 0) {
	    		writePath = logPath + "city_0.log";
	    	} else {
	    		File lastFile = files[fileCount - 1];
	    		int lastFileNumber = MainUtil.extractNumberByFile(lastFile);
	    		
	    		Boolean createNewFile = false;
	    		long fileSizeInBytes = lastFile.length();
	    		if (fileSizeInBytes > 0) {
	    			long fileSizeInKB = fileSizeInBytes / 1024;
	    			
	    			// FIXME
	    			//if (fileSizeInKB > 2048) {
	    			if (fileSizeInKB >= maxLogSize) {
	    				createNewFile = true;
	    			}
	    		}
	    		
	    		if (createNewFile) {
	    			writePath = logPath + "city_" + (lastFileNumber + 1) + ".log";
	    			
	    		} else {
	    			writePath = lastFile.getAbsolutePath();
	    		}
	    	}
	    } else {
	    	writePath = logPath + "city_0.log";
	    }

	    if (writePath != null) {
	    	String city = (String)MainUtil.getRandomFromList(cities);
	    	StringBuilder line = new StringBuilder();
	    	line.append(dateFormatter.format(new Date()));
	    	line.append("\t");
	    	line.append(MainUtil.getRandomFromList(logLevelList));
	    	line.append("\t");
	    	line.append(city);
	    	line.append("\t");
	    	line.append("Hello-from-" + city);
	    	
	    	try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(writePath, true)))) {
	    	    out.println(line.toString());
	    	} catch (IOException e) {
	    	    System.err.println(e);
	    	}
	    }

		//log.info("WRITE CITY LOG END");
	}

}