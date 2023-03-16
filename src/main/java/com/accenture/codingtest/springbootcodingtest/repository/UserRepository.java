package com.accenture.codingtest.springbootcodingtest.repository;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Parag Sansare
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}
