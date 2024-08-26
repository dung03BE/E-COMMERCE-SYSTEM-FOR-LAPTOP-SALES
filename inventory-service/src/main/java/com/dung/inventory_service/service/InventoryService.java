package com.dung.inventory_service.service;

import com.dung.inventory_service.dto.InventoryItemRequest;
import com.dung.inventory_service.dto.ProductDto;
import com.dung.inventory_service.entity.InventoryItem;
import com.dung.inventory_service.entity.ProductCreatedEvent;
import com.dung.inventory_service.exception.DataNotFoundException;
import com.dung.inventory_service.feignclients.ProductServiceClient;
import com.dung.inventory_service.repository.IInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class InventoryService implements IInventoryService {
    private final IInventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final ProductServiceClient productServiceClient;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate redisTemplate;

    @Override
    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public InventoryItem getInventory(int id) throws DataNotFoundException {
        InventoryItem inventoryItem = inventoryRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Dont find InventoryItem with Id:" + id));
        return inventoryItem;
    }

    @Override
    public InventoryItem createInventoryItem(InventoryItemRequest itemRequest) throws DataNotFoundException {
        // Kiểm tra xem sản phẩm có tồn tại hay không
        ProductDto productDto = productServiceClient.getProductById(itemRequest.getProduct_id());
        if (productDto == null) {
            System.out.println("Server down");
            throw new DataNotFoundException("Server down");
        }

        // Tìm mục inventory với productId tương ứng
        InventoryItem existingInventoryItem = (InventoryItem) inventoryRepository
                .findByProductId(itemRequest.getProduct_id());

        if (existingInventoryItem != null) {
            // Nếu mục inventory đã tồn tại, cộng thêm quantity
            existingInventoryItem.setQuantity(existingInventoryItem.getQuantity() + itemRequest.getQuantity());
            inventoryRepository.save(existingInventoryItem);
            String redisQuantityStock = itemRequest.getProduct_id() + "_quantity_stock";
            redisTemplate.opsForValue().increment(redisQuantityStock, itemRequest.getQuantity());
            return existingInventoryItem;
        } else {
            // Nếu mục inventory chưa tồn tại, tạo mới
            modelMapper.typeMap(InventoryItemRequest.class, InventoryItem.class)
                    .addMappings(mapper -> mapper.skip(InventoryItem::setId));
            InventoryItem inventoryItem = new InventoryItem();

            modelMapper.map(itemRequest, inventoryItem);
            inventoryItem.setQuantity(itemRequest.getQuantity());
            inventoryItem.setProductId(itemRequest.getProduct_id());
            inventoryRepository.save(inventoryItem);
            String redisQuantityStock = existingInventoryItem.getProductId() + "_quantity_stock";
            redisTemplate.opsForValue().set(redisQuantityStock, itemRequest.getQuantity());
            return inventoryItem;
        }
    }

    @Override
    public InventoryItem updateInventoryItem(int id, InventoryItemRequest itemRequest) throws DataNotFoundException {
        InventoryItem existingInventoryItem = (InventoryItem) inventoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Dont find InventoryItem with id:" + id));
        // modelMapper.map(existingInventoryItem,itemRequest);
        existingInventoryItem.setId(id);
        modelMapper.typeMap(InventoryItemRequest.class, InventoryItem.class)
                .addMappings(mapper -> mapper.skip(InventoryItem::setProductId));
        existingInventoryItem.setQuantity(itemRequest.getQuantity());

        inventoryRepository.save(existingInventoryItem);
        // Cập nhật Redis
        String redisQuantityStock = existingInventoryItem.getProductId() + "_quantity_stock";
        redisTemplate.opsForValue().set(redisQuantityStock, itemRequest.getQuantity());
        return existingInventoryItem;
    }

    @Override
    public void deleteInventoryItem(int id) throws DataNotFoundException {
        InventoryItem existingInventoryItem = (InventoryItem) inventoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Dont find InventoryItem with id:" + id));
        inventoryRepository.deleteById(id);
    }

    @Override
    public InventoryItem findByProductId(int productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @RabbitListener(queues = "productQueue")
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
        InventoryItem inventory = new InventoryItem();
        inventory.setProductId(event.getProductId());
        inventory.setQuantity(0); // Khởi tạo với số lượng ban đầu là 0
        inventoryRepository.save(inventory);

        String redisQuantityStock = event.getProductId() + "_quantity_stock";
        redisTemplate.opsForValue().set(redisQuantityStock, 0);
    }

}
