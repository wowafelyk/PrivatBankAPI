package com.example.fenix.privatbankapi.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
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
public class CurrentExchangeTask extends AsyncTaskLoader<LinkedList<CurrentExchangeData>> {

    private final static String TEST = "exchangeTask";
    private static final  String BASE_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public CurrentExchangeTask(Context context) {
        super(context);
    }


    @Override
    public LinkedList<CurrentExchangeData> loadInBackground() {
        LinkedList<CurrentExchangeData> result=null;
        String response = new CurrencyClient().getCurencyData(BASE_URL);
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

        public LinkedList<CurrentExchangeData> getCurencyData(String s) throws JSONException {
            LinkedList<CurrentExchangeData> dataList = new LinkedList<>();
            Log.d(TEST, s.toString());
            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                CurrentExchangeData data = new CurrentExchangeData(
                        obj.getString("ccy"),
                        obj.getString("base_ccy"),
                        obj.getDouble("buy"),
                        obj.getDouble("sale")
                );
                dataList.add(data);
            }
            return dataList;
        }
    }
}
