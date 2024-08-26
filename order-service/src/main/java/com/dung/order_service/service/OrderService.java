package com.dung.order_service.service;

import com.dung.order_service.config.RabbitMQConfig;
import com.dung.order_service.dto.OrderRequest;
import com.dung.order_service.entity.Order;
import com.dung.order_service.entity.OrderConfirmationMessage;
import com.dung.order_service.entity.OrderDetail;
import com.dung.order_service.entity.Status;
import com.dung.order_service.exception.DataNotFoundException;
import com.dung.order_service.repository.IOrderDetailRepository;
import com.dung.order_service.repository.IOrderRepository;
import com.rabbitmq.client.AMQP;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Order createOrder(OrderRequest orderRequest) throws DataNotFoundException {

        // Cấu hình ModelMapper để bỏ qua ID khi mapping từ OrderRequest sang Order
        modelMapper.typeMap(OrderRequest.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Tạo một đối tượng Order mới
        Order tempOrder = new Order();
        modelMapper.map(orderRequest, tempOrder);
        tempOrder.setStatus(Status.Pending);
        // Validate và set shipping date
        LocalDate shippingDate = orderRequest.getShippingDate() == null ?
                LocalDate.now() : orderRequest.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be today or later.");
        }
        tempOrder.setShippingDate(shippingDate);
        tempOrder.setActive(true);
        tempOrder.setShippingAddress(orderRequest.getShippingAddress());
        tempOrder.setCustomerEmail(orderRequest.getCustomerEmail());
        orderRepository.save(tempOrder);
        OrderConfirmationMessage message =new OrderConfirmationMessage(tempOrder.getId(), tempOrder.getCustomerEmail());
        rabbitTemplate.convertAndSend("orderExchange", "order.confirmed", message);

        return tempOrder;
    }
    // chuyen doi tu string sang integer ( tu redis)

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrderById(int id) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Dont find Order with id ="+id)
        );
        if(existingOrder!=null)
        {
            existingOrder.setActive(false);
            orderRepository.save(existingOrder);
        }

    }
}
