package com.dung.inventory_service.controller;

import com.dung.inventory_service.dto.InventoryItemRequest;
import com.dung.inventory_service.dto.ProductDto;
import com.dung.inventory_service.entity.InventoryItem;
import com.dung.inventory_service.exception.DataNotFoundException;
import com.dung.inventory_service.feignclients.ProductServiceClient;
import com.dung.inventory_service.service.IInventoryService;
import com.dung.inventory_service.service.InventoryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/inventories")
@RequiredArgsConstructor
@Validated
public class InventoryController {
    private final ProductServiceClient productServiceClient;

    private final IInventoryService inventoryService;
    @GetMapping
    public List<InventoryItem> getAllInventoryItem()
    {
        List<InventoryItem> inventoryItems = inventoryService.getAllInventory();
        return inventoryItems;
    }
    @GetMapping("{id}")
    public InventoryItem getInventory(@PathVariable int id) throws DataNotFoundException {
        return inventoryService.getInventory(id);
    }
    @PostMapping
    public ResponseEntity<?> createInventoryItem(@Valid  @RequestBody  InventoryItemRequest itemRequest) throws DataNotFoundException {

           InventoryItem inventoryItem = inventoryService.createInventoryItem(itemRequest);
           return  ResponseEntity.ok().body(inventoryItem);

    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateInventoryItem(@PathVariable int id, @RequestBody InventoryItemRequest itemRequest) throws DataNotFoundException {
        InventoryItem inventoryItem = inventoryService.updateInventoryItem(id,itemRequest);
        return ResponseEntity.ok().body(inventoryItem);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable int id) throws DataNotFoundException {
        inventoryService.deleteInventoryItem(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    @CircuitBreaker(name = "productService", fallbackMethod = "checkProductExistsFallback")
    @GetMapping("/product/{productId}")
    public String getInventorybyProductId(@PathVariable int productId)
    {
        ProductDto productDto = productServiceClient.getProductById(productId);

        InventoryItem inventoryItem = inventoryService.findByProductId(productId);
        return inventoryItem.toString();
    }
    public String checkProductExistsFallback(int productId, Throwable throwable)
    {
        return "Server down";
    }
}
