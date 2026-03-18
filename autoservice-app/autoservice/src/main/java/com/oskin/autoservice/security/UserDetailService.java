package com.oskin.autoservice.security;

import com.oskin.autoservice.model.User;
import com.oskin.autoservice.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) throw new UsernameNotFoundException("User " + login + " no found");
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                List.of(user.getRole().toAuthority())
        );
    }
}
