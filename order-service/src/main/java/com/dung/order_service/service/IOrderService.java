package com.dung.order_service.service;

import com.dung.order_service.dto.OrderRequest;
import com.dung.order_service.entity.Order;
import com.dung.order_service.exception.DataNotFoundException;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderRequest orderRequest) throws DataNotFoundException;

    List<Order> getAllOrders();

    void deleteOrderById(int id) throws DataNotFoundException;
}
