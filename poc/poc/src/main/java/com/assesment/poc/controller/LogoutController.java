package com.assesment.poc.controller;

import com.assesment.poc.config.AuthConfig;
import com.assesment.poc.model.LoginDetails;
import com.assesment.poc.repository.LoginDetailsRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class LogoutController implements LogoutSuccessHandler {

    @Autowired
    private AuthConfig config;

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest req,
                                HttpServletResponse res,
                                Authentication authentication) {

        if (req.getSession() != null) {
            req.getSession().invalidate();
        }

        String returnTo = config.getContextPath(req);
        String logoutUrl = config.getLogoutUrl() + "?client_id=" + config.getClientId() + "&returnTo=" + returnTo;

        try {
            TestingAuthenticationToken token = (TestingAuthenticationToken) authentication;
            DecodedJWT jwt = JWT.decode(token.getCredentials().toString());
            String email = jwt.getClaims().get("email").asString();
            Optional<LoginDetails> loginDetails = loginDetailsRepository.findById(email);
            if (loginDetails.isPresent()) {
                LoginDetails userLoginDetails = loginDetails.get();
                userLoginDetails.setLoggedIn(false);
                loginDetailsRepository.save(userLoginDetails);
            }
            res.sendRedirect(logoutUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
