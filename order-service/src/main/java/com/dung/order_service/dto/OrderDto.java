package com.dung.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {

    private int id;
    private Integer userId;
    private String fullName;

    private String phoneNumber;
    private String note;

    private BigDecimal totalMoney;

    private String shippingMethod;

    private String shippingAddress;

    private LocalDate shippingDate;

    private String paymentMethod;

    private List<OrderDetail> orderDetails;
    @Data
    public static class OrderDetail{
        int id;
        int product_id;
        int quantity;
        int price;
    }
}
