package org.tutorial.clique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.clique.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByResetPasswordToken(String token);
}
