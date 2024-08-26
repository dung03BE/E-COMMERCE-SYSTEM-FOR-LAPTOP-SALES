package com.dung.order_service.service;

import com.dung.order_service.dto.OrderDetailRequest;
import com.dung.order_service.entity.OrderDetail;
import com.dung.order_service.exception.DataNotFoundException;

import java.math.BigDecimal;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) throws DataNotFoundException;
    public BigDecimal calculateTotalForOrder(int orderId);

    void deleteOrderDetail(int id) throws DataNotFoundException;
}
