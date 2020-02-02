package com.gmail.ramawthar.priyash.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BatchIngestServiceImpl implements BatchIngestService {

    public String processCSVFile(MultipartFile file){
    	String status = "Empty file!";
    	if (!(file.isEmpty())){
    		status = "File is being processed";
	    	BufferedReader br;
	    	List<String> result = new ArrayList<>();
	    	try {
	
	    	     String line;
	    	     InputStream is = file.getInputStream();
	    	     br = new BufferedReader(new InputStreamReader(is));
	    	     while ((line = br.readLine()) != null) {
	    	          result.add(line);
	    	          System.out.println(line);
	    	          /*TO DO: process the line and push it to the rabbit Queue"*/
	    	     }
	
	    	  } catch (IOException e) {
	    	    System.err.println(e.getMessage());       
	    	  }
	    	
	    	
	    	status = "File processed";
    	}
    	
    	return status;
    }
}
