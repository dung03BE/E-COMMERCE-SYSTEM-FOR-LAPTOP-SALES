//package com.dung.order_service.service;
//import com.dung.order_service.entity.Order;
//import com.dung.order_service.entity.OrderConfirmationMessage;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//
//
//@Service
//public class EmailServiceListener {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @RabbitListener(queues = "orderQueue")
//    public void sendOrderConfirmationEmail(OrderConfirmationMessage message) {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setTo(message.getCustomerEmail());
//            helper.setSubject("Order Confirmation");
//            helper.setText("Thank you for your order. Your order ID is " + message.getId(), true);
//            javaMailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            e.printStackTrace(); // Để dễ dàng phát hiện lỗi trong log
//        }
//    }
//}