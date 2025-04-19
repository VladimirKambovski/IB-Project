package com.kambovski.ibproject.repository;

import com.kambovski.ibproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Custom query methods if needed, e.g., findByUsername or findByEmbg
    User findByEmbg(String embg);
}
