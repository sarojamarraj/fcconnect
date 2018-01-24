package com.freightcom.api.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.freightcom.api.model.User;
import com.freightcom.api.repositories.UserRepository;

public class UserDetailsServiceImpl implements  UserDetailsService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        List<User> users = userRepository.findByLogin(username);

        if (users.size() == 1) {
            int number_of_authorities = users.get(0).getAuthorities().size();

            log.debug("NUMBER OF AUTHORITIES FOR USER " + users.get(0) + " " + number_of_authorities);

            return new UserDetailsImpl(users.get(0));
        } else {
            throw new BadCredentialsException("Username not found.");
        }
    }

}
