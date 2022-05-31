package com.assesment.poc.controller;

import com.assesment.poc.storage.StorageFileNotFoundException;
import com.assesment.poc.storage.StorageService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String lattitude,
                                   @RequestParam String longitude,
                                   @RequestParam String comment,
                                   Authentication authentication) throws IOException {

        TestingAuthenticationToken token = (TestingAuthenticationToken) authentication;
        DecodedJWT jwt = JWT.decode(token.getCredentials().toString());
        String email = jwt.getClaims().get("email").asString();

        System.out.println("email: " + email);
        System.out.println("file: " + file.getOriginalFilename());
        System.out.println("comment: " + comment);
        System.out.println("lattitude: " + lattitude);
        System.out.println("longitude: " + longitude);

        if (!file.isEmpty()) {
            storageService.store(file);
        }


        return "redirect:/dashboard";

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
