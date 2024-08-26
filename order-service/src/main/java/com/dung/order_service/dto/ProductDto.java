package com.dung.order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class ProductDto {
    @JsonProperty("product_id")
    private int productId;
    private String name;
    private BigDecimal price;
    private String description;

}

