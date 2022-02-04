package com.example.expensetracker.repository;

import com.example.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    Optional<User> findUserById(Long id);
    Optional<User> findUserByEmail(String email);

    @Override
    @Transactional
    User save(User user);
}
