package com.ishaan.app2022.server.config;

import com.ishaan.app2022.server.controllers.AuthController;
import com.ishaan.app2022.server.objects.UserObject;
import com.ishaan.app2022.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // In this case, the email will be the username
        Optional<UserObject> searchedUser = userRepository.findByEmail(email);
        if (searchedUser.isEmpty())
            throw new UsernameNotFoundException(String.format("Invalid email address: %s", email));

        return new User(email, AuthController.EMPTY_STRING, Collections.emptyList());
    }
}
