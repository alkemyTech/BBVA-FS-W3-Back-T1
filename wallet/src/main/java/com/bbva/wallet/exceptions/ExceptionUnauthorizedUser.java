package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionUnauthorizedUser extends CustomException{

    public ExceptionUnauthorizedUser() {
        super("No posee permisos para realizar esta acción", ErrorCodes.USUARIO_NO_AUTORIZADO);
    }
}
