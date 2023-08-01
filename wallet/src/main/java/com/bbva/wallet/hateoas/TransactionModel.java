package com.bbva.wallet.hateoas;

import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TransactionType;
import com.bbva.wallet.entities.Account;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class TransactionModel extends RepresentationModel<TransactionModel> {

    private Long id;
    private Double amount;
    private TransactionType type;
    private String description;
    private Account account;
    private LocalDateTime transactionDate;
    private Double accountBalance;

    public TransactionModel(){
    }

    public TransactionModel(Transaction transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.account = transaction.getAccount();
        this.transactionDate = transaction.getTransactionDate();
        this.accountBalance = transaction.getAccountBalance();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
