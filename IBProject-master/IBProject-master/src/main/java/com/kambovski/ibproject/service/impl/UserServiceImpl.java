package com.kambovski.ibproject.service.impl;

import com.kambovski.ibproject.model.User;
import com.kambovski.ibproject.repository.UserRepository;
import com.kambovski.ibproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByEmbg(String embg) {
        return userRepository.findByEmbg(embg);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }



}
