package com.dung.order_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class OrderRequest   {

    @JsonProperty("user_id")
    @Min(value = 1, message = "User's ID must be > 0")
    private Integer userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, message = "Phone number must be at least 5 characters")
    private String phoneNumber;

    @JsonProperty("note")
    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
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
