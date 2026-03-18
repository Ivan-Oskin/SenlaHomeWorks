package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.JwtDto;
import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.service.UserDetailService;
import com.oskin.autoservice.service.UserService;
import com.oskin.autoservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autoservice")
public class UserController {
    private final UserService userService;
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    UserController (UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
                    AuthenticationManager authenticationManager, UserDetailService userDetailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailService = userDetailService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/reg")
    public void save(@RequestBody UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userService.createUser(userRequest);
    }

    @PostMapping("/auth")
    public JwtDto createAuthToken(@RequestBody UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getLogin(), userRequest.getPassword()));
        UserDetails userDetails = userDetailService.loadUserByUsername(userRequest.getLogin());
        String token = jwtUtils.generateToken(userDetails);
        return new JwtDto(token);
    }
}
