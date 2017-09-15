package com.unifacisa.decentralized.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.unifacisa.decentralized.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path="/authenticate", params={"username", "password"})
    @Timed
    public String authorize(@RequestParam String username, @RequestParam String password) {
        System.out.println("AUTHORIZE");
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
        System.out.println("AUTHORIZE 2");
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            System.out.println("AUTHORIZE 3");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return tokenProvider.createToken(authentication, true);
        } catch (AuthenticationException ae) {
            log.trace("Authentication exception trace: {}", ae);
            return null;
        }
    }

}
