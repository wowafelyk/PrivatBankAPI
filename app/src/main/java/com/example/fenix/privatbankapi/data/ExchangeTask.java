package com.example.fenix.privatbankapi.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by fenix on 18.08.2015.
 */
public class ExchangeTask extends AsyncTaskLoader<ExchangePerDate> {

    private final static String TEST = "exchangeTask";
    //01.12.2014
    private static final  String BASE_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
    private String mUrl;


    public ExchangeTask(Context context,String s) {
        super(context);
        mUrl=BASE_URL+s;
    }

    @Override
    public ExchangePerDate loadInBackground() {
        ExchangePerDate result=null;
        String response = new CurrencyClient().getCurencyData(mUrl);
        if(response==null){
            return null;
        }
        try {
            result = new CurrencyExchangeParser().getCurencyData(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    class CurrencyClient {

        public String getCurencyData(String urlString) {

            // Send data
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d(TEST,url.toString());


                // Get the server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "");
                }

                return sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(urlConnection!=null) urlConnection.disconnect();
            }
            return null;
        }
    }

    class CurrencyExchangeParser {

        public ExchangePerDate getCurencyData(String s) throws JSONException {
            ExchangePerDate exchange;
            String date;
            LinkedList<Currency> currencyList = new LinkedList<>();
            JSONObject jsonObject = new JSONObject(s);
            date=jsonObject.getString("date");
            JSONArray jsonArray = jsonObject.getJSONArray("exchangeRate");

            //String baseCurrency, String currency, Double saleRateNB, Double purchaseRateNB, Double saleRate, Double purchaseRate
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                currencyList.add(new Currency(
                        obj.optString("baseCurrency"),
                        obj.optString("currency"),
                        obj.optDouble("saleRateNB"),
                        obj.optDouble("purchaseRateNB"),
                        obj.optDouble("saleRate"),
                        obj.optDouble("purchaseRate")));
            }
            return new ExchangePerDate(currencyList,date);
        }
    }
}