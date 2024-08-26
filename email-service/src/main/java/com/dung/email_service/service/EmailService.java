package com.dung.email_service.service;

import com.dung.email_service.entity.OrderConfirmationMessage;
import com.google.zxing.WriterException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @RabbitListener(queues = "orderQueue")
    public void sendOrderConfirmationEmail(OrderConfirmationMessage message) {
        try {
            String esimData = generateEsimData(message);
            byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage(esimData);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(message.getCustomerEmail());
            helper.setSubject("Nguyen Chi Dung come here");
            helper.setText("Tôi là Nguyen Chi Dung hay quyet QR ben duoi nhe !!!" + message.getId(), true);
            // Đính kèm mã QR vào email
            helper.addAttachment("esim-qr-code.png", new ByteArrayResource(qrCodeImage));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | WriterException | IOException e) {
            e.printStackTrace();
        }
    }
    private String generateEsimData(OrderConfirmationMessage message) {
        // Tạo dữ liệu eSIM từ thông điệp đơn hàng (ví dụ: ID eSIM, thông tin cấu hình)
        return "Chào em Loan anh là DUNGX đẹp trai hahahaha, hay gui cho ANH 2 link fb em nhe, " + message.getId();
    }
}
