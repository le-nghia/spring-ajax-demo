package com.training.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerid;

    @Column(name = "customername", columnDefinition = "varchar(50) not null")
    private String customername;

    @Column(name = "age", nullable = false)
    private int age;
}
