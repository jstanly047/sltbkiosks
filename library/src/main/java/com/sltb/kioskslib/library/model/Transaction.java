package com.sltb.kioskslib.library.model;

import com.sltb.kioskslib.library.model.kafka.CheckOut;
import com.sltb.kioskslib.library.model.kafka.TopUp;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @Column(name = "account_id")
    private Long accountid;
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;
    private float amount;
    private float balance;
    private String origin;
    private String destination;
    private Date date;



    public Transaction(){

    }

    public Transaction(Integer charge, CheckOut checkOut, LastCheckIn lastCheckIn, Account account){
        this.accountid = checkOut.getAccountid();
        this.type = TransactionType.TRAVEL;
        this.amount = charge;
        this.balance = account.getBalance();
        this.origin = lastCheckIn.getOrigin();
        this.destination = checkOut.getDestination();
        this.date = checkOut.getDate();
    }

    public Transaction(TopUp topUp, Account account){
        this.accountid = account.getAccountId();
        this.type = TransactionType.TOP_UP;
        this.amount = topUp.getAmount();
        this.balance = account.getBalance();
        this.date = topUp.getTime();
    }

    public Long getAccountid() {
        return accountid;
    }

    public void setAccountid(Long accountid) {
        this.accountid = accountid;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
