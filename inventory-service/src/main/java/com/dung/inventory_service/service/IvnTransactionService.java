package com.dung.inventory_service.service;

import com.dung.inventory_service.dto.InvTransactionRequest;
import com.dung.inventory_service.entity.InventoryItem;
import com.dung.inventory_service.entity.InventoryTransaction;
import com.dung.inventory_service.exception.DataNotFoundException;
import com.dung.inventory_service.feignclients.ProductServiceClient;
import com.dung.inventory_service.repository.IInvTransactionRepository;
import com.dung.inventory_service.repository.IInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IvnTransactionService {
    private final IInvTransactionRepository invTransactionRepository;
    private final ModelMapper modelMapper;
    private final IInventoryRepository inventoryRepository;
    public Page<InventoryTransaction> getAllInvTransaction(Pageable page) {
        return invTransactionRepository.findAll(page);
    }

    public InventoryTransaction createInvTransaction(InvTransactionRequest invTransactionRequest) throws DataNotFoundException {
        InventoryItem inventoryItem=inventoryRepository.findById(invTransactionRequest.getInventoryItemId())
                .orElseThrow(()->new DataNotFoundException("Dont find InventoryItem with id:"+invTransactionRequest.getInventoryItemId()));

        modelMapper.typeMap(InvTransactionRequest.class,InventoryTransaction.class)
                .addMappings(mapper->mapper.skip(InventoryTransaction::setId));
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        modelMapper.map(invTransactionRequest,inventoryTransaction);
        inventoryTransaction.setInventoryItem(inventoryItem);
        inventoryTransaction.setQuantityChange(invTransactionRequest.getQuantityChange());
        inventoryTransaction.setTransactionType(invTransactionRequest.getTransactionType());
        inventoryTransaction.setReason(invTransactionRequest.getReason());
        inventoryTransaction.setCreatedBy(invTransactionRequest.getCreatedBy());
        inventoryTransaction.setCreatedAt(invTransactionRequest.getCreatedAt());
        return invTransactionRepository.save(inventoryTransaction);
    }
}
