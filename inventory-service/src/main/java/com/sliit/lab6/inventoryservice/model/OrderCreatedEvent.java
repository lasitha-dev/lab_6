package com.sliit.lab6.inventoryservice.model;

public record OrderCreatedEvent(String orderId, String item, int quantity) {
}