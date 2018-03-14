package com.dom5230.utility.walletmanager;

/**
 * Created by root on 14/3/18.
 */

public class TransactionHistoryItem {
    private String timestamp, trans_mode, amount, balance;

    public TransactionHistoryItem(String timestamp, String trans_mode, String amount, String balance){
        this.timestamp = timestamp;
        this.trans_mode = trans_mode;
        this.amount = amount;
        this.balance = balance;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTrans_mode() {
        return trans_mode;
    }

    public String getAmount() {
        return amount;
    }

    public String getBalance() {
        return balance;
    }

}
