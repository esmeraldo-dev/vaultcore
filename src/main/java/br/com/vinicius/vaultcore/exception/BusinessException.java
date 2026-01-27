package br.com.vinicius.vaultcore.exception;

public class BusinessException extends RuntimeException {

    public String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}
