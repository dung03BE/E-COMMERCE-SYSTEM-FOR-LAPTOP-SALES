package com.dung.email_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfirmationMessage {
    private int id;
    private String customerEmail;
}
