package com.bookstore.repository;

import com.bookstore.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.deletedAt IS NULL")
    java.util.List<Employee> findAllActive();
}
