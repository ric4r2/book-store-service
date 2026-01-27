package com.bookstore.repository;

import com.bookstore.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Client entity.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.deletedAt IS NULL")
    java.util.List<Client> findAllActive();
}
