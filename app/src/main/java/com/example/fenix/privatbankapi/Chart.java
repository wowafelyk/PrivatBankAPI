package com.example.fenix.privatbankapi;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fenix.privatbankapi.data.CurrentExchangeTask;
import com.example.fenix.privatbankapi.data.ExchangePerDate;
import com.example.fenix.privatbankapi.data.ExchangeTask;

import java.util.LinkedList;

/**
 * Created by fenix on 19.08.2015.
 */
public class Chart extends Fragment implements
        LoaderManager.LoaderCallbacks<ExchangePerDate> {

    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private CurrentExchangeTask loader;
    static final int LOADER_ID = 2;
    private final static int REQUEST_CODE = 101;
    private int mYear=2013 ;
    private int mMonth=0;
    private int mDay=1;
    private TextView mDateView;
    private final static String TEST = "fragment2";
    private static LinkedList<ExchangePerDate> dataBase = new LinkedList<>();


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

        getLoaderManager().initLoader(LOADER_ID, new Bundle(),this).forceLoad();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        return view;
    }

    @Override
    public Loader<ExchangePerDate> onCreateLoader(int id, Bundle args) {
        Loader<ExchangePerDate> loader = null;
        if(mDay==29){
            mDay=1;
            mMonth=mMonth+1;
        }
        if (id == LOADER_ID) {
            Log.d(TEST, mDay + "." + mMonth + "." + mYear);
            loader = new ExchangeTask(getActivity(),mDay+"."+mMonth+"."+mYear);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ExchangePerDate> loader, ExchangePerDate data) {
        if(data==null)
        dataBase.add(data);
        mDay++;
        if(mMonth<10) {
            getLoaderManager().restartLoader(LOADER_ID, new Bundle(), this).forceLoad();
        }
        if(mMonth>7){
            Log.d(TEST, "  " + dataBase.size() + "");
        }
        Log.d(TEST,"LoadING "+mDay+mMonth);
    }

    @Override
    public void onLoaderReset(Loader<ExchangePerDate> loader) {

    }
}
