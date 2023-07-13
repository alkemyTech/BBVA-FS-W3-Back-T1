package com.bbva.wallet.hateoas;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currencies;
import org.springframework.hateoas.RepresentationModel;
public class AccountModel extends RepresentationModel<AccountModel> {
    private Long id;
    private Currencies currency;
    private Double balance;
    private String cbu;
    private User userId;
    private Double transactionLimit;

    public AccountModel() {
    }
    public AccountModel(Account account) {
        this.id = account.getId();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
        this.cbu = account.getCbu();
        this.userId = account.getUserId();
        this.transactionLimit = account.getTransactionLimit();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Currencies getCurrency() {
        return currency;
    }
    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }
    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getCbu() {
        return cbu;
    }
    public void setCbu(String cbu) {
        this.cbu = cbu;
    }
    public User getUserId() {
      return userId;
    }
    public void setUserId(User userId) {
        this.userId = userId;
    }
    public Double getTransactionLimit() {
        return transactionLimit;
    }
    public void setTransactionLimit(Double transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

}

