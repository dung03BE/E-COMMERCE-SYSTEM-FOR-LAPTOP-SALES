package com.dung.email_service.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`order`")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Order extends BaseEntity  implements Serializable {
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
    @Column(name="customer_email")
    private String customerEmail;
}
