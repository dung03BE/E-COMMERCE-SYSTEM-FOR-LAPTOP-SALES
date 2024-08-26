package com.dung.inventory_service.repository;

import com.dung.inventory_service.entity.InventoryItem;
import com.dung.inventory_service.service.InventoryService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IInventoryRepository extends JpaRepository<InventoryItem,Integer> {

    InventoryItem findByProductId(int product_id);

}
