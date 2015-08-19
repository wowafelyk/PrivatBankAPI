package com.example.fenix.privatbankapi;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fenix.privatbankapi.data.Currency;
import com.example.fenix.privatbankapi.data.CurrentExchangeTask;
import com.example.fenix.privatbankapi.data.DatePickerFragment;
import com.example.fenix.privatbankapi.data.ExchangePerDate;
import com.example.fenix.privatbankapi.data.ExchangeTask;
import com.example.fenix.privatbankapi.settings.SettingsFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;

/**
 * Created by fenix on 19.08.2015.
 */
public class Chart extends Fragment implements
        LoaderManager.LoaderCallbacks<ExchangePerDate> {


    static final int LOADER_ID = 2;
    private int mYear = 2015;
    private int mMonth = 2;
    private int mDay = 25;
    private final static String TEST = "fragment2";
    private static LinkedList<ExchangePerDate> dataBase = new LinkedList<>();
    private static LineGraphSeries<DataPoint> series;
    private int mPeriod = 60;
    private DataPoint[] list1 = new DataPoint[31];
    private DataPoint[] list2 = new DataPoint[8];
    private Handler mHandler = new Handler();
    private int num = 0;
    private Currency cur;
    GraphView graph;


    public Chart() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, new Bundle(), this).forceLoad();
        setHasOptionsMenu(true);
        setRetainInstance(true);
        series = new LineGraphSeries<DataPoint>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_layout, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);


        series.setThickness(3);
        num = 0;
        series.setTitle(" курс гривні ");
        graph.setTitle("USD - 25.02.2015-25.04.2015");
        graph.animate();
        graph.addSeries(series);
        mYear = 2015;
        mMonth = 2;
        mDay = 25;
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity2, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                mPeriod = 60;
                mYear = 2015;
                mMonth = 2;
                mDay = 25;
                series=new LineGraphSeries<DataPoint>();
                graph.addSeries(series);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ExchangePerDate> onCreateLoader(int id, Bundle args) {
        Loader<ExchangePerDate> loader = null;
        if (mDay == 29) {
            mDay = 1;
            mMonth = mMonth + 1;
        }
        if (id == LOADER_ID) {
            Log.d(TEST, mDay + "." + mMonth + "." + mYear);
            loader = new ExchangeTask(getActivity(), mDay + "." + mMonth + "." + mYear);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ExchangePerDate> loader, ExchangePerDate data) {

        Log.e(TEST, "дата " + data.getDate() + "");
        Log.e(TEST, "дата " + mPeriod);
        if (mPeriod != 0) {
            dataBase.add(data);
            mPeriod--;
            mDay++;
            getLoaderManager().restartLoader(LOADER_ID, new Bundle(), this).forceLoad();
        }
        if (mPeriod == 0) {
            getLoaderManager().restartLoader(LOADER_ID, new Bundle(), this).cancelLoad();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < dataBase.size(); i++) {
                        ExchangePerDate p = dataBase.get(i);

                        LinkedList<Currency> l = p.getList();
                        for (Currency c : l) {
                            if ((c.getCurrency()).equals("USD")) {
                                Log.e(TEST, i + " for "+c.getCurrency());
                                cur = c;
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TEST, "int=" + num + "  " + cur.getPurchaseRateNB());
                                        series.appendData(new DataPoint(num++, cur.getPurchaseRateNB()), true, 60);
                                    }
                                });
                            }
                        }
                    }
                    if (series == null) Log.d(TEST, "series = null");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            graph.addSeries(series);
                        }
                    });
                }
            }).start();

        }

    }

    @Override
    public void onLoaderReset(Loader<ExchangePerDate> loader) {

    }

}
