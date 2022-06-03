package com.assesment.poc.controller;

import com.assesment.poc.Exception.StorageFileNotFoundException;
import com.assesment.poc.service.StorageService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    private UserController userController;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/addComments")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam String latitude,
                                   @RequestParam String longitude,
                                   @RequestParam String comment,
                                   Authentication authentication) {

        TestingAuthenticationToken token = (TestingAuthenticationToken) authentication;
        DecodedJWT jwt = JWT.decode(token.getCredentials().toString());
        String email = jwt.getClaims().get("email").asString();
        String userFirstName = userController.getUserFirstName(email);

        if (!file.isEmpty()) {
            storageService.store(file, email, userFirstName, latitude, longitude, comment);
        }

        return "redirect:/dashboard";

    }

    @GetMapping("/getCommentPicture")
    @ResponseBody
    public ResponseEntity<Resource> getCommentPicture(@RequestParam String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
