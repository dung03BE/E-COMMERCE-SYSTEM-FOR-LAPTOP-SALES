package com.vti.product_service.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class CreatingProductForm {

    private int id;
    @NotBlank(message = "ProductName is required")
    @Size(min = 3, max=200, message = "ProductName must be between 3-> 200 charecter")
    private String name;
    @Min(value = 0 , message = "Price must be greater than and equal 0")
    @Max(value = 1000000000, message = "Price must be less than and equal 100000000")
    private float price;

    private String description;

}
