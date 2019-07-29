package com.mma.challenge.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainUtil {

	public static File[] sortFilesByName(File[] files) {
		if (files != null && files.length > 0) {
			Arrays.sort(files, new Comparator<File>() {
	            @Override
	            public int compare(File o1, File o2) {
	            	int n1 = extractNumberByFile(o1);
	            	int n2 = extractNumberByFile(o2);
	                return n1 - n2;
	            }
	        });
		}

		return files;
	}
	
	public static int extractNumberByFile(File file) {
		int i = 0;
		String name = file.getName();
        try {
            int s = name.indexOf('_')+1;
            int e = name.lastIndexOf('.');
            String number = name.substring(s, e);
            i = Integer.parseInt(number);
        } catch(Exception e) {
            i = 0;
        }
        return i;
	}

	public static <T> Object getRandomFromList(List<T> list) {
		Random rand = new Random();

        return list.get(rand.nextInt(list.size())); 
	}

}