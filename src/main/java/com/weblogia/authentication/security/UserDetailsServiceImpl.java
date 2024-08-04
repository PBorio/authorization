package com.weblogia.authentication.security;

import com.weblogia.authentication.model.User;
import com.weblogia.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @SuppressWarnings("unused")
    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl() {
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("could not found user..!!");
        }

        return new CustomUserDetails(user);
    }
}
