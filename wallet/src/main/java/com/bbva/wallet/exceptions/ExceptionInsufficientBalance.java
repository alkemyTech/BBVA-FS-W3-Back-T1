package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionInsufficientBalance extends CustomException{

    public ExceptionInsufficientBalance() {
        super("El saldo de la cuenta es insuficiente",ErrorCodes.SALDO_INSUFICIENTE);
    }
}
