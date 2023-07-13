package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionUserNotAuthenticated extends CustomException{

    public ExceptionUserNotAuthenticated(){
        super("Usuario sin permisos", ErrorCodes.USUARIO_SIN_PERMISOS);
    }
}
