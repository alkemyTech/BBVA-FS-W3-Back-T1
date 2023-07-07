package com.bbva.wallet.utils;

import com.bbva.wallet.enums.Currencies;

public class CurrencyLimit {
    public static double getTransactionLimitForCurrency(Currencies currency) {
        return currency == Currencies.ARS ? 300000.0 : 1000.0;
    }
}
