package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionTransactionNotExist extends CustomException{
    public ExceptionTransactionNotExist(){
        super("La transacción no existe", ErrorCodes.TRANSACCION_NO_EXISTE);
    }
}
