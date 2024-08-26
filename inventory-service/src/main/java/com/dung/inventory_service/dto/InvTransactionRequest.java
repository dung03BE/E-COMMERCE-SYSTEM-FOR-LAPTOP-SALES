package com.dung.inventory_service.dto;

import com.dung.inventory_service.entity.InventoryItem;
import com.dung.inventory_service.entity.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvTransactionRequest {
    private int inventoryItemId;
    private int quantityChange ;
    private TransactionType transactionType;
    private String reason;
    private String referenceId;
  //  @Size(min = 5, message = "CreatedBy must be at least 5 characters")
    private String createdBy;
    private LocalDateTime createdAt;
}
