package com.dung.notification_service.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    public void sendEmailWithQRCode(String to, String subject, String text, String qrCodeData) throws MessagingException, WriterException, IOException, MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // Use HTML

        // Generate QR code
        byte[] qrCode = generateQRCodeImage(qrCodeData, 200, 200);

        // Add QR code as attachment
        helper.addAttachment("QRCode.png", new ByteArrayResource(qrCode));

        emailSender.send(message);
    }
    private byte[] generateQRCodeImage(String text, int width, int height) throws WriterException,  IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

}
