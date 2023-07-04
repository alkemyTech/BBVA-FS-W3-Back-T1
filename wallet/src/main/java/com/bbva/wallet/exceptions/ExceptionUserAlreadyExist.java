package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionUserAlreadyExist extends CustomException{
    public ExceptionUserAlreadyExist() {
        super("Este usuario ya existe", ErrorCodes.USUARIO_YA_CREADO);
    }
}
