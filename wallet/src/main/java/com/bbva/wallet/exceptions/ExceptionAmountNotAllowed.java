package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionAmountNotAllowed extends CustomException{
    public ExceptionAmountNotAllowed(){
        super("No se puede solicitar un prestamo negativo, ni nulo", ErrorCodes.CANTIDAD_NO_PERMITIDA);
    }
}
