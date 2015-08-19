package com.example.fenix.privatbankapi.data;

import java.util.LinkedList;

/**
 * Created by fenix on 18.08.2015.
 */
public class ExchangePerDate {
    private LinkedList<Currency> list = new LinkedList<>();
    private String date;

    public ExchangePerDate(LinkedList<Currency> list, String date) {
        this.list = list;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LinkedList<Currency> getList() {
        return list;
    }

    public void setList(LinkedList<Currency> list) {
        this.list = list;
    }
}

