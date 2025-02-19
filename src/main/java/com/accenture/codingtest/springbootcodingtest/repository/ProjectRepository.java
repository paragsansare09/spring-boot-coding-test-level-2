package com.accenture.codingtest.springbootcodingtest.repository;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Parag Sansare
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Project findByName(String name);

    Page<Project> findAllByNameContaining(String name, Pageable pageable);
}
