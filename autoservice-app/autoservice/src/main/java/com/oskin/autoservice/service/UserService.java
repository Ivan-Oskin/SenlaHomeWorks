package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.model.User;
import com.oskin.autoservice.repository.UserRepository;
import com.oskin.autoservice.utils.MapperToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MapperToEntity mapperToEntity;

    @Autowired
    public UserService(UserRepository userRepository, MapperToEntity mapperToEntity) {
        this.userRepository = userRepository;
        this.mapperToEntity = mapperToEntity;
    }

    @Transactional
    public void createUser (UserRequest userRequest) {
        User user = mapperToEntity.mapToUserEntity(userRequest);
        userRepository.create(user);
    }

}
