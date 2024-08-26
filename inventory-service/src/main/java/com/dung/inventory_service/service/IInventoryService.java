package com.dung.inventory_service.service;

import com.dung.inventory_service.dto.InventoryItemRequest;
import com.dung.inventory_service.entity.InventoryItem;
import com.dung.inventory_service.exception.DataNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IInventoryService {
    List<InventoryItem> getAllInventory();

    InventoryItem getInventory(int id) throws DataNotFoundException;

    InventoryItem createInventoryItem(InventoryItemRequest itemRequest) throws DataNotFoundException;

    InventoryItem updateInventoryItem(int id, InventoryItemRequest itemRequest) throws DataNotFoundException;

    void deleteInventoryItem(int id) throws DataNotFoundException;

    InventoryItem findByProductId(int productId);
}
