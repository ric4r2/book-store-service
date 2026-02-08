package com.bookstore.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Employee entity representing staff members in the book store.
 */
@Entity
@Table(name = "employees")
@DiscriminatorValue("EMPLOYEE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {

    private String position;

    private String department;
}
