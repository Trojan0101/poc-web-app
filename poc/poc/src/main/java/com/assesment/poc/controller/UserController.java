package com.assesment.poc.controller;

import com.assesment.poc.config.AuthConfig;
import com.assesment.poc.model.LoginDetails;
import com.assesment.poc.repository.LoginDetailsRepository;
import com.assesment.poc.service.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Value(value = "${com.auth0.domain}")
    private String domain;

    @Value(value = "${com.auth0.managementApi.clientId}")
    private String managementApiClientId;

    @Value(value = "${com.auth0.managementApi.clientSecret}")
    private String managementApiClientSecret;

    @Autowired
    private ApiService apiService;

    @Autowired
    private AuthConfig config;

    @Autowired
    private AuthController controller;

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @GetMapping(value = "/users")
    @ResponseBody
    public String users(HttpServletRequest request, HttpServletResponse response) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + controller.getManagementApiToken());

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange("https://" + domain + "/api/v2/users?fields=email,user_metadata", HttpMethod.GET, entity, String.class);
        return result.getBody();

    }

    @GetMapping(value = "/userByEmail")
    @ResponseBody
    public ResponseEntity<String> userByEmail(HttpServletResponse response, @RequestParam String email) {

        ResponseEntity<String> result = apiService.getCall(config.getUsersByEmailUrl() + email);
        return result;

    }

    public List<?> getUsersData(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        String userDetails = users(request, response);
        JSONArray userDetailsArray = new JSONArray(userDetails);

        List<List<?>> latLngList = new ArrayList<>();
        List<String> firstnameList = new ArrayList<>();
        List<String> emailList = new ArrayList<>();

        for (int i = 0; i < userDetailsArray.length(); i++) {
            JSONObject userData = userDetailsArray.getJSONObject(i);
            String email = userData.getString("email");

            JSONObject userMetadata = (JSONObject) userData.get("user_metadata");
            String userFirstName = userMetadata.get("first_name").toString();
            // String address = userMetadata.get("address").toString();
            // String country = userMetadata.get("location").toString();

            JSONObject userGeoIp = (JSONObject) userMetadata.get("geoip");
            List<Double> userLatLng = new ArrayList<>();
            userLatLng.add(userGeoIp.getDouble("latitude"));
            userLatLng.add(userGeoIp.getDouble("longitude"));

            latLngList.add(userLatLng);
            firstnameList.add(userFirstName);
            emailList.add(email);
        }
        return List.of(emailList, firstnameList, latLngList);

    }

    public List<?> getActiveUsers() {

        List<LoginDetails> allUsers = loginDetailsRepository.findAll();
        List<String> emailList = new ArrayList<>();

        if (!allUsers.isEmpty()) {
            for (LoginDetails user : allUsers) {
                if (user.isLoggedIn()) {
                    emailList.add(user.getEmail());
                }
            }
        }
        return emailList;

    }

}
