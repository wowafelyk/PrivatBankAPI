package com.example.fenix.privatbankapi.data;

/**
 * Created by fenix on 18.08.2015.
 */
public class CurrentExchangeData {
    private String mCurentMoney;
    private String mBaseMoney;
    private Double mBuy;
    private Double mSale;

    public CurrentExchangeData(String mCurentMoney, String mBaseMoney, Double mBuy, Double mSale) {
        this.mCurentMoney = mCurentMoney;
        this.mBaseMoney = mBaseMoney;
        this.mBuy = mBuy;
        this.mSale = mSale;
    }

    public String getmCurentMoney() {
        return mCurentMoney;
    }

    public void setmCurentMoney(String mCurentMoney) {
        this.mCurentMoney = mCurentMoney;
    }

    public String getmBaseMoney() {
        return mBaseMoney;
    }

    public void setmBaseMoney(String mBaseMoney) {
        this.mBaseMoney = mBaseMoney;
    }

    public Double getmBuy() {
        return mBuy;
    }

    public void setmBuy(Double mBuy) {
        this.mBuy = mBuy;
    }

    public Double getmSale() {
        return mSale;
    }

    public void setmSale(Double mSale) {
        this.mSale = mSale;
    }
}
