package com.g4.fauxexchange.service.impl;

import com.g4.fauxexchange.model.User;
import com.g4.fauxexchange.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

/* Class User Details
This class loads the user into the session */

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if("GENERIC".equals(user.getRole())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        } else if("ADMIN".equals(user.getRole())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        }
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
