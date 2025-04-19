package com.kambovski.ibproject.service.impl;

import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Пребарување корисник по EMBG (CN од сертификатот)
        User user = userService.findUserByEmbg(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Додај ја улогата на корисникот (ADMIN или CUSTOMER)
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType()));

        // Врати го корисникот со неговите овластувања
        return new org.springframework.security.core.userdetails.User(user.getEmbg(), "", authorities);
    }
}
