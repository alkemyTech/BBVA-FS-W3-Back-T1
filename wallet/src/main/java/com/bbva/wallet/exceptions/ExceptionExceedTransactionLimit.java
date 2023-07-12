package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionExceedTransactionLimit extends CustomException{
    public ExceptionExceedTransactionLimit() {
        super("Estas excediendo el limite de transaccion", ErrorCodes.SUPERASTE_LIMITE_DE_TRANSACCION);
    }
}
