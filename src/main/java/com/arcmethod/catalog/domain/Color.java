package com.arcmethod.catalog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40, unique = true)
    private String name;
    @Column(nullable = false, length =7)
    private String hex;
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
