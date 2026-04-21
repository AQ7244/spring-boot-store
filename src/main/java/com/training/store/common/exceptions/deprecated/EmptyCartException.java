package com.training.store.common.exceptions.deprecated;

public class EmptyCartException extends RuntimeException {

  public EmptyCartException() {
      super("Cart is Empty.");
    }

    public EmptyCartException(String message) {
        super(message);
    }
}
