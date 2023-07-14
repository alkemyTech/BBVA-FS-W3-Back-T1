package com.bbva.wallet.exceptions;

import com.bbva.wallet.enums.ErrorCodes;

public class ExceptionUserWithNoTransactions extends CustomException{
    public ExceptionUserWithNoTransactions(){
        super("Usuario sin transacciones realizadas",ErrorCodes.USUARIO_SIN_TRANSACCIONES);
    }

}
