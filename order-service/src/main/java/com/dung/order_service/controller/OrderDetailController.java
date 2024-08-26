package com.dung.order_service.controller;

import com.dung.order_service.dto.OrderDetailRequest;
import com.dung.order_service.entity.OrderDetail;
import com.dung.order_service.exception.DataNotFoundException;
import com.dung.order_service.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orderdetails")
@RequiredArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    @PostMapping
    public ResponseEntity<?> createOrderDetail(@RequestBody OrderDetailRequest orderDetailRequest)
    {
        try {
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailRequest);
            return new ResponseEntity<>("OK",HttpStatus.CREATED);
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi. Vui lòng thử lại.");
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable int id) throws DataNotFoundException {
        orderDetailService.deleteOrderDetail(id);
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }

}
