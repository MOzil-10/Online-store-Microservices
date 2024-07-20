package com.order.Exception;

public class InventoryServiceException extends RuntimeException{
    public InventoryServiceException(String message) {
        super(message);
    }
}
