package com.dung.order_service.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Table(name = "`order`")

@Data
public class Order extends BaseEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private Integer  userId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone_number", length = 100, nullable = false)
    private String phoneNumber;
    @Column(name = "note", length = 100)
    private String note;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "total_money")
    private BigDecimal totalMoney;
    @Column(name = "shipping_method")
    private String shippingMethod;
    @Column(name = "shipping_address", length = 100)
    private String shippingAddress;
    @Column(name = "shipping_date")
    private LocalDate shippingDate;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "active")
    private boolean active; // thuoc ve admin
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    @Column(name="customer_email")
    private String customerEmail;

}
