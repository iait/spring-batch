package com.iait.springbatch.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transactionRecord")
public class Transaction {

    private String username;
    private int userId;
    private double amount;
    private Date transactionDate;

    public Transaction() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "Transaction [username=" + username + ", userId=" + userId + ", transactionDate=" 
                + transactionDate + ", amount=" + amount + "]";
    }
}
