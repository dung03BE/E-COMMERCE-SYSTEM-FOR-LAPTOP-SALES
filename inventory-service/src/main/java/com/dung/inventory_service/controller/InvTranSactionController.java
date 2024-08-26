package com.dung.inventory_service.controller;

import com.dung.inventory_service.dto.InvTransactionRequest;
import com.dung.inventory_service.entity.InventoryTransaction;
import com.dung.inventory_service.exception.DataNotFoundException;
import com.dung.inventory_service.service.IvnTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/invtransaction")
@RequiredArgsConstructor
public class InvTranSactionController {
    private final IvnTransactionService ivnTransactionService;
    @GetMapping
    public Page<InventoryTransaction>getAllInvTransaction(@PageableDefault(page = 0,size =5,  sort = "createdAt", direction = Sort.Direction.DESC)Pageable page)
    {
        Page<InventoryTransaction> inventoryTransactions = ivnTransactionService.getAllInvTransaction(page);
        return inventoryTransactions;
    }
    @PostMapping
    public ResponseEntity<?> createInvTransaction(@RequestBody InvTransactionRequest invTransactionRequest) throws DataNotFoundException {
        InventoryTransaction inventoryTransaction = ivnTransactionService.createInvTransaction(invTransactionRequest);
        return ResponseEntity.ok(inventoryTransaction);
    }
}
