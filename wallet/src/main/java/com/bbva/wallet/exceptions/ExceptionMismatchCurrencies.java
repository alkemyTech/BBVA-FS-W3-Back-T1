package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionMismatchCurrencies extends CustomException {

    public ExceptionMismatchCurrencies() {
        super("El tipo de moneda del monto no coincide con el tipo de moneda de la cuenta",ErrorCodes.NO_COINCIDEN_TIPO_DE_MONEDAS);
    }
}
