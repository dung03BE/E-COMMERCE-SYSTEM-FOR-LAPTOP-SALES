package com.dung.inventory_service.repository;

import com.dung.inventory_service.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvTransactionRepository extends JpaRepository<InventoryTransaction,Integer> {
}
