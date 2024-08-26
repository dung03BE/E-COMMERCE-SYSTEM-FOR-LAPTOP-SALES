    package com.dung.order_service.service;

    import com.dung.order_service.dto.OrderDetailRequest;
    import com.dung.order_service.dto.ProductDto;
    import com.dung.order_service.entity.Order;
    import com.dung.order_service.entity.OrderDetail;
    import com.dung.order_service.exception.DataNotFoundException;
    import com.dung.order_service.feignclients.ProductServiceClient;
    import com.dung.order_service.repository.IOrderDetailRepository;
    import com.dung.order_service.repository.IOrderRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class OrderDetailService implements IOrderDetailService{
        private final IOrderDetailRepository orderDetailRepository;
        private final IOrderRepository orderRepository;
        private final ProductServiceClient productServiceClient;
        private final RestTemplate restTemplate;
        private final RedisTemplate<String,Object> redisTemplate;
        @Override
        public OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) throws DataNotFoundException {
            Integer pQuantityStock = getIntegerValue(orderDetailRequest.getProductId() + "_quantity_stock");

            System.out.println("Quantity stock **************:" + pQuantityStock);

            if (pQuantityStock == null) {
                System.out.println("Quantity stock is null for product ID: " + orderDetailRequest.getProductId());
                throw new DataNotFoundException("Số lượng tồn kho không tồn tại trong Redis cho Product ID: " + orderDetailRequest.getProductId());
            }
            // Can key lu tren redis luu so dien thoai da ban
            String productSoldKey = orderDetailRequest.getProductId()  + "_sold";
            redisTemplate.opsForValue().setIfAbsent(productSoldKey, 0);
            Object totalSold = redisTemplate.opsForValue().increment(productSoldKey, orderDetailRequest.getQuantity());
            System.out.println("Total sold:********************:" + totalSold);
            System.out.println("OrderQuantity:********************:" + orderDetailRequest.getQuantity());
            if (Integer.parseInt(totalSold.toString())  > pQuantityStock) {
                System.out.println("Out of Stock!!!!!!!!!!!!!!!!!");
                return null;
            }

            ProductDto existingProduct = productServiceClient.getProductById(orderDetailRequest.getProductId());
            if (existingProduct == null) {
                throw new DataNotFoundException("Product không tồn tại");
            }

            Order order = orderRepository.findById(orderDetailRequest.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException("Order không tồn tại"));
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productId(orderDetailRequest.getProductId())
                    .quantity(orderDetailRequest.getQuantity())
                    .color(orderDetailRequest.getColor())
                    .price(orderDetailRequest.getPrice())
                    .build();
            orderDetail = orderDetailRepository.save(orderDetail);
            order.setTotalMoney(calculateTotalForOrder(order.getId()));
            orderRepository.save(order);
            return orderDetail;
        }

        private Integer getIntegerValue(String key) {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                try {
                    return Integer.parseInt(value.toString());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Value for key " + key + "is not a valid integer");
                }
            }
            return null;
        }
        public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
            return orderDetailRepository.findByOrderId(orderId);
        }
        @Override
        public BigDecimal calculateTotalForOrder(int orderId) {
            List<OrderDetail> orderDetails = getOrderDetailsByOrderId(orderId);
            BigDecimal total = BigDecimal.ZERO;

            for (OrderDetail detail : orderDetails) {
                BigDecimal quantity = new BigDecimal(detail.getQuantity());
                BigDecimal itemTotal = detail.getPrice().multiply(quantity);
                total = total.add(itemTotal);
            }
            return total.setScale(2, RoundingMode.HALF_UP);
        }

        @Override
        public void deleteOrderDetail(int id) throws DataNotFoundException {
            OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                    ()-> new DataNotFoundException("Dont find OrderDetail with id:"+id)
            );
            orderDetailRepository.deleteById(id);

        }


    }
