package com.gmail.ramawthar.priyash.services;


import org.springframework.stereotype.Service;

@Service
public class BatchIngestServiceImpl implements BatchIngestService {
    public String processFile(String test){
        System.out.println("testing the service");
        return test+" good job!";
    }
}
