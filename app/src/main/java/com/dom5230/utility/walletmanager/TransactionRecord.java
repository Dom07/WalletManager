package com.dom5230.utility.walletmanager;

/**
 * Created by root on 14/3/18.
 */

public class TransactionRecord {
    private String Date, Time, DayOfWeek, Category, Amouont;

    public TransactionRecord(String Date, String Time, String DayOFWeek, String Category, String Amount){
        this.Date = Date;
        this.Time = Time;
        this.DayOfWeek = DayOFWeek;
        this.Category = Category;
        this.Amouont = Amount;
    }


    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public String getCategory() {
        return Category;
    }

    public String getAmouont() {
        return Amouont;
    }
}
