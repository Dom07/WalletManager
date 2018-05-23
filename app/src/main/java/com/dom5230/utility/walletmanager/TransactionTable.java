package com.dom5230.utility.walletmanager;

public class TransactionTable {
    String SERIAL_NUMBER = "SERIAL_NUMBER";
    String TABLE_NAME = "TRANSACTION_TABLE";
    String DATE = "TRANSACTION_DATE";
    String TIME = "TRANSACTION_TIME";
    String DAY_OF_WEEK = "DAY_OF_WEEK";
    String CATEGORY = "CATEGORY";
    String AMOUNT = "AMOUNT";
    String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+SERIAL_NUMBER+" INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            +DATE+" STRING, "
            +TIME+" STRING, "
            +DAY_OF_WEEK+" STRING, "
            +CATEGORY+" STRING, "
            +AMOUNT+" STRING)";
}
