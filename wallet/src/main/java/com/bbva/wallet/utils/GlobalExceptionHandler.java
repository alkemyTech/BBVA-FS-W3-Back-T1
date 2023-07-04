package com.bbva.wallet.utils;

import com.bbva.wallet.enums.ErrorCodes;
import com.bbva.wallet.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<String>> handlerCustomExcetions(CustomException ex) {
        Response<String> response = new Response<>();
        response.addError(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String field = Objects.nonNull(ex.getBindingResult().getFieldError()) ? ex.getBindingResult().getFieldError().getField() :
                                                                                ex.getBindingResult().getObjectName();
        String error = Objects.nonNull(ex.getBindingResult().getFieldError()) ? ex.getBindingResult().getFieldError().getDefaultMessage() : "";
        Response<String> response = new Response<String>();
        response.addError(ErrorCodes.VALOR_INVALIDO);
        response.setMessage(String.format("El campo %s no es valido: [%s]", field, error));
        response.setData(field);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
