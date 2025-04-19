package com.kambovski.ibproject.service;

import com.kambovski.ibproject.model.User;

public interface UserService {
    User findUserByEmbg(String embg);
    User createUser(User user);
}
