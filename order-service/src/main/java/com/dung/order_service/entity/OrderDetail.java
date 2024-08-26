package com.dung.order_service.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    @Column(name = "product_id")
    private int productId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private int quantity;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    private String color;

}
