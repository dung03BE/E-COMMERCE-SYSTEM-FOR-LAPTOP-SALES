package com.dung.inventory_service.feignclients;


import com.dung.inventory_service.config.FeignConfig;
import com.dung.inventory_service.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "product-service", url = "${product.service.url}", configuration = FeignConfig.class)
public interface ProductServiceClient {

    @GetMapping("/products/{productId}")
  //  @CircuitBreaker(name = "productService", fallbackMethod = "checkProductExistsFallback")
    ProductDto getProductById(@PathVariable(name = "productId") int id);

//    default ProductDto checkProductExistsFallback(int productId, Throwable throwable) {
//        Logger log = LoggerFactory.getLogger(ProductServiceClient.class);
//        log.error("Failed to check product existence: {}", throwable.getMessage());
//        // Trả về một đối tượng ProductDto mặc định hoặc null
//        return new ProductDto(); // hoặc return null;
//    }
}