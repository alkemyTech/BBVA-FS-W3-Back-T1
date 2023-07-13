package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionUserAccountsNotFound extends CustomException {

    public ExceptionUserAccountsNotFound() {
        super("El usuario no posee cuentas", ErrorCodes.CUENTA_NO_ENCONTRADA);
    }

}
