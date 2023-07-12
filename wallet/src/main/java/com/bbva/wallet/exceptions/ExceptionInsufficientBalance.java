package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionInsufficientBalance extends CustomException {
    public ExceptionInsufficientBalance() {
        super("No tenes saldo suficiente",ErrorCodes.SALDO_INSUFICIENTE);
    }
}
