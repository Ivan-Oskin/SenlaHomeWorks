package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autoservice")
public class UserController {
    private final UserService userService;

    @Autowired
    UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reg")
    public void save(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
    }
}
