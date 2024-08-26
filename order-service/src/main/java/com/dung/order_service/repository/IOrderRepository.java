package com.dung.order_service.repository;

import com.dung.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order,Integer> {
}
