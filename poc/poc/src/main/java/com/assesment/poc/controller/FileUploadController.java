package com.assesment.poc.controller;

import com.assesment.poc.storage.StorageFileNotFoundException;
import com.assesment.poc.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/addComments")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam String comment) throws IOException {

        System.out.println("file: " + file);
        System.out.println("comment: " + comment);

        storageService.store(file);

        return "redirect:/dashboard";

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
