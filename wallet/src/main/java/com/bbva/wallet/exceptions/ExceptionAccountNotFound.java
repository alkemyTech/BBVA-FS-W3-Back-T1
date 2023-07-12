package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionAccountNotFound extends CustomException{

    public ExceptionAccountNotFound() {
        super("La cuenta no existe",ErrorCodes.CUENTA_NO_ENCONTRADA);

    }
}
