package com.assesment.poc.controller;

import com.assesment.poc.model.UserComments;
import com.assesment.poc.repository.UserCommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String home() {

        return "index";

    }

    @GetMapping("/dashboard")
    public String getDashboard(HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {

        pageData(request, response, model);

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
        List<String> usersEmail = (List<String>) userData.get(0);
        List<String> usersFirstName = (List<String>) userData.get(1);
        List<?> usersLocation = (List<?>) userData.get(2);
        List<String> activeUsersEmail = (List<String>) userController.getActiveUsers();
        List<String> activeUsersFirstName = new ArrayList<>();

        for (String email : activeUsersEmail) {
            int activeUserEmailIdx = usersEmail.indexOf(email);
            activeUsersFirstName.add(usersFirstName.get(activeUserEmailIdx));
            usersFirstName.remove(activeUserEmailIdx);
        }

        model.addAttribute("activeUsersFirstName", activeUsersFirstName);
        model.addAttribute("inactiveUsersFirstName", usersFirstName);
        model.addAttribute("usersLocation", usersLocation);

    }

}
