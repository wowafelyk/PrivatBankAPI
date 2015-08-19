package com.example.fenix.privatbankapi;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fenix.privatbankapi.data.*;

import java.util.LinkedList;

/**
 * A placeholder fragment containing a simple view.
 */
public class Activity1Fragment extends Fragment implements
        LoaderManager.LoaderCallbacks<LinkedList<CurrentExchangeData>>{

    private ListView mListView;
    private CurrentExchangeTask loader;
    static final int LOADER_ID = 1;

    public Activity1Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity1, container, false);
        mListView = (ListView)view.findViewById(R.id.listView);

        Bundle bndl = new Bundle();
        getLoaderManager().initLoader(LOADER_ID, bndl,this).forceLoad();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getTitle().equals("Reload")){
            getLoaderManager().restartLoader(LOADER_ID, new Bundle(),this).forceLoad();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Reload");
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Loader<LinkedList<CurrentExchangeData>> loader = null;
        if (id == LOADER_ID) {
            loader = new CurrentExchangeTask(getActivity());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<LinkedList<CurrentExchangeData>> loader,
                               LinkedList<CurrentExchangeData> data) {
        ListAdapter adapter = new ListAdapter(getActivity(),data);
        mListView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }



    public class ListAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        LinkedList<CurrentExchangeData> data;
        private TextView mTextCurrency, mTextBuy, mTextSale;

        ListAdapter(Context context, LinkedList<CurrentExchangeData> objects) {
            ctx = context;
            data = objects;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            //not used
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // используем созданные, но не используемые view
            View v = convertView;
            if (v == null) {
                v = lInflater.inflate(R.layout.linearlayout, parent, false);
            }
            mTextCurrency = (TextView)v.findViewById(R.id.text_cur_name);
            mTextBuy = (TextView)v.findViewById(R.id.text_cur_buy);
            mTextSale = (TextView)v.findViewById(R.id.text_cur_sale);
            CurrentExchangeData curData = data.get(position);
            mTextCurrency.setText(curData.getmCurentMoney());
            mTextBuy.setText(curData.getmBuy().toString());
            mTextSale.setText(curData.getmSale().toString());

            return v;
        }
    }




}
