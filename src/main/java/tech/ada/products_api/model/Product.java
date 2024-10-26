package tech.ada.products_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String sku;
    @Column(nullable = false, length = 100)
    private String name;
    private String description;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;
    @Column(precision = 10)
    private Double weight;
}
