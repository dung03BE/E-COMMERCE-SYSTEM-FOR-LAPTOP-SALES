package com.dung.order_service.controller;

import com.dung.order_service.dto.OrderDto;
import com.dung.order_service.dto.OrderRequest;
import com.dung.order_service.entity.Order;
import com.dung.order_service.exception.DataNotFoundException;
import com.dung.order_service.service.IOrderDetailService;
import com.dung.order_service.service.IOrderService;
import com.dung.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) throws DataNotFoundException {
        Order order= orderService.createOrder(orderRequest);
        return ResponseEntity.ok().body(order);
    }
    @GetMapping
    public List<OrderDto> getAllOrders()
    {
        List<Order> orders = orderService.getAllOrders();
        return modelMapper.map(orders,new TypeToken<List<OrderDto>>(){}.getType());
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable  int id) throws DataNotFoundException {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
