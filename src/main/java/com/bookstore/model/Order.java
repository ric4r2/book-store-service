package com.bookstore.model;

import com.bookstore.model.base.BaseEntity;
import com.bookstore.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing customer orders.
 */
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_client", columnList = "client_id"),
        @Index(name = "idx_orders_employee", columnList = "employee_id"),
        @Index(name = "idx_orders_status", columnList = "status"),
        @Index(name = "idx_orders_date", columnList = "order_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookItem> bookItems = new ArrayList<>();

    /**
     * Add a book item to the order.
     */
    public void addBookItem(BookItem bookItem) {
        bookItems.add(bookItem);
        bookItem.setOrder(this);
    }

    /**
     * Remove a book item from the order.
     */
    public void removeBookItem(BookItem bookItem) {
        bookItems.remove(bookItem);
        bookItem.setOrder(null);
    }

    /**
     * Calculate total price from book items.
     */
    public void calculateTotalPrice() {
        this.totalPrice = bookItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
