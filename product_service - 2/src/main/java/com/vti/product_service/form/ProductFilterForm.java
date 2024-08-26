package com.vti.product_service.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ProductFilterForm {
    private String search ;
    private Float minPrice;
    private Float maxPrice;
}
