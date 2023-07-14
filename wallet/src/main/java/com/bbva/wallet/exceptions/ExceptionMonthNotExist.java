package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionMonthNotExist extends CustomException{
    public ExceptionMonthNotExist(){
        super("No puede ingresar un valor en meses negativo ni cero", ErrorCodes.CANTIDAD_MESES_INVALIDA);
    }
}
