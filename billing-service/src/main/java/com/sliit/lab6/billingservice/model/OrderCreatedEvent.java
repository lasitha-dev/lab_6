package com.sliit.lab6.billingservice.model;

public record OrderCreatedEvent(String orderId, String item, int quantity) {
}