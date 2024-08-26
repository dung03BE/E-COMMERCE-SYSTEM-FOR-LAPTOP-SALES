package com.dung.order_service.feignclients;

import com.dung.order_service.config.FeignConfig;
import com.dung.order_service.dto.ProductDto;
import com.dung.order_service.exception.DataNotFoundException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="product-service", url = "${product.service.url}", configuration = FeignConfig.class)
public interface ProductServiceClient {
    @GetMapping("/products/{productId}")
    ProductDto getProductById(@PathVariable(name = "productId") int id);


}
