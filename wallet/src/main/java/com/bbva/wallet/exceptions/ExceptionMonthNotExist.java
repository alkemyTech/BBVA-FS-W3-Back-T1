package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionMonthNotExist extends CustomException{
    public ExceptionMonthNotExist(){
        super("Debe ingresar una cantidad en meses válida (1-12)", ErrorCodes.CANTIDAD_MESES_INVALIDA);
    }
}
