package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;
import lombok.Getter;

public abstract class CustomException extends RuntimeException {

    @Getter
    private ErrorCodes errorCode;

    CustomException(String message, ErrorCodes errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
