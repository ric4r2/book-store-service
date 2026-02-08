package com.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client entity representing customers in the book store.
 */
@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;
}
