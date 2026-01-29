package br.com.vinicius.vaultcore.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessage> handleBusinessException(BusinessException ex) {
        ErrorMessage error = new ErrorMessage(
                LocalDateTime.now(),
                400,
                "Erro de Negócio",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintException(Exception ex) {
        ErrorMessage error = new ErrorMessage(
                LocalDateTime.now(),
                400,
                "Erro de Validação",
                "Dados inválidos ou já cadastrados no sistema."
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception ex) {
        ErrorMessage error = new ErrorMessage(
                LocalDateTime.now(),
                500,
                "Erro Interno",
                "Ocorreu um erro inesperado no servidor."
        );
        return ResponseEntity.internalServerError().body(error);
    }
}