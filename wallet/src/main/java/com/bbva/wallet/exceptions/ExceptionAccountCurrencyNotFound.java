package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionAccountCurrencyNotFound extends CustomException{

    public ExceptionAccountCurrencyNotFound() {
        super("El usuario no tiene una cuenta de ese tipo de moneda", ErrorCodes.CUENTA_NO_ENCONTRADA);
    }
}
