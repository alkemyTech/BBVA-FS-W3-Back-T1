package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionUserNotFound extends CustomException{
    public ExceptionUserNotFound() {
        super("El usuario no existe", ErrorCodes.USUARIO_NO_ENCONTRADO);
    }
}