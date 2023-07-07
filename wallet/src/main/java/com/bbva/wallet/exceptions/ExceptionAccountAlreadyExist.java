package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionAccountAlreadyExist extends CustomException{
    public ExceptionAccountAlreadyExist() {
        super("El usuario ya tiene una cuenta de este tipo", ErrorCodes.CUENTA_YA_CREADA);
    }
}