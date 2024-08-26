package com.dung.email_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderRequest implements Serializable {
    @JsonProperty("user_id")

    private Integer userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")

    private String phoneNumber;

    @JsonProperty("note")
    private String note;

    @JsonProperty("total_money")

    private BigDecimal totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("customer_email")
    private String customerEmail;
}
