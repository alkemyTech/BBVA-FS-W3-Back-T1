package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionTransactionNotAllowed extends CustomException{
    public ExceptionTransactionNotAllowed() {
        super("Esta transaccion no esta permitida", ErrorCodes.TRANSACCION_NO_PERMITIDA);
    }

    public ExceptionTransactionNotAllowed(String message) {
        super(message, ErrorCodes.TRANSACCION_NO_PERMITIDA);
    }
}
