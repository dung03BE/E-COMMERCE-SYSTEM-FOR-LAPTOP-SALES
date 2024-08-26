package com.vti.product_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDto {
    @JsonProperty("product_id")
    @Min(value = 1,message = "ProductId must be >0")
    private int productId;
    @Size(min=5,max=200,message = "Image's name")
    @JsonProperty("image_url")
    private String imageUrl;
}
