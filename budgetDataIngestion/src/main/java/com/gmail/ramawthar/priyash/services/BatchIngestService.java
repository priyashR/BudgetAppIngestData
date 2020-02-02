package com.gmail.ramawthar.priyash.services;

import org.springframework.web.multipart.MultipartFile;

public interface BatchIngestService {
    String processCSVFile(MultipartFile file);
}
