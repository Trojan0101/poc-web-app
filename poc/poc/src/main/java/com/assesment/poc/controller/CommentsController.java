package com.assesment.poc.controller;

import com.assesment.poc.model.UserComments;
import com.assesment.poc.repository.UserCommentsRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentsController {

    @Autowired
    UserCommentsRepository userCommentsRepository;

    @GetMapping("/showComments")
    public ResponseEntity<?> showComments() {

        List<UserComments> userComments = userCommentsRepository.findAll();
        JSONArray jsonArray = new JSONArray(userComments);
        return ResponseEntity.ok().body(jsonArray.toString());

    }

    @GetMapping("/getLatLng")
    public ResponseEntity<?> getLatLng(@RequestParam String comment) {
        UserComments userComments = userCommentsRepository.findByComment(comment);
        return ResponseEntity.ok().body(userComments);
    }


}
