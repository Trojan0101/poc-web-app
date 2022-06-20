package com.assesment.poc.controller;

import com.assesment.poc.model.UserComments;
import com.assesment.poc.model.Users;
import com.assesment.poc.repository.UserCommentsRepository;
import com.assesment.poc.repository.UsersRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserController userController;

    @Autowired
    UserCommentsRepository userCommentsRepository;

    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/")
    public String home() {

        return "index";

    }

    @GetMapping("/dashboard")
    public String getDashboard(HttpServletRequest request,
                               HttpServletResponse response,
                               Model model,
                               Authentication authentication) {

        TestingAuthenticationToken token = (TestingAuthenticationToken) authentication;
        DecodedJWT jwt = JWT.decode(token.getCredentials().toString());
        String email = jwt.getClaims().get("email").asString();

        Users user = usersRepository.findByEmail(email);

        if (!user.isEmailVerified()) {
            return "verifyEmail";
        } else {
            pageData(request, response, model);
        }

        return "dashboard";

    }

    @GetMapping("/viewComments")
    public String getViewComments(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model) {

        pageData(request, response, model);

        List<UserComments> userComments = userCommentsRepository.findAll();
        model.addAttribute("userComments", userComments);

        return "viewComments";

    }

    private void pageData (HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {

        List<?> userData = userController.getUsersData(request, response);

        List<?> usersEmail = (List<?>) userData.get(0);
        List<?> usersFirstName = (List<?>) userData.get(1);
        List<?> usersLocation = (List<?>) userData.get(2);
        List<?> activeUsersEmail = userController.getActiveUsers();
        List<String> activeUsersFirstName = new ArrayList<>();

        List<String> removeNames = new ArrayList<>();

        for (Object email : activeUsersEmail) {
            int activeUserEmailIdx = usersEmail.indexOf(email);
            activeUsersFirstName.add((String) usersFirstName.get(activeUserEmailIdx));
            removeNames.add((String) usersFirstName.get(activeUserEmailIdx));
        }
        for(String name: removeNames) {
            usersFirstName.remove(name);
        }

        model.addAttribute("activeUsersFirstName", activeUsersFirstName);
        model.addAttribute("inactiveUsersFirstName", usersFirstName);
        model.addAttribute("usersLocation", usersLocation);

    }

}
