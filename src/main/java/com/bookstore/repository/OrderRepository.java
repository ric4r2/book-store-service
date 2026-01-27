package com.bookstore.repository;

import com.bookstore.model.Order;
import com.bookstore.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.client.email = :email AND o.deletedAt IS NULL")
    List<Order> findByClientEmail(@Param("email") String email);

    @Query("SELECT o FROM Order o WHERE o.employee.email = :email AND o.deletedAt IS NULL")
    List<Order> findByEmployeeEmail(@Param("email") String email);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.deletedAt IS NULL")
    List<Order> findByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL")
    List<Order> findAllActive();
}
