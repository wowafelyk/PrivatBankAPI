package com.example.fenix.privatbankapi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;


import com.example.fenix.privatbankapi.data.Currency;
import com.example.fenix.privatbankapi.data.CurrentExchangeTask;
import com.example.fenix.privatbankapi.data.ExchangePerDate;
import com.example.fenix.privatbankapi.data.ExchangeTask;


import java.util.Calendar;
import java.util.LinkedList;


/**
 * Build by Fenix 18.08.2015
 */
public class ExchangeFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ExchangePerDate> {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private CurrentExchangeTask loader;
    static final int LOADER_ID = 2;
    private final static int REQUEST_CODE = 101;
    private int mYear, mMonth, mDay;
    private TextView mDateView;
    private final static String TEST = "fragment2";



    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        Log.d(TEST,mDay+"."+mMonth+"."+mYear);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        mDateView = (TextView)view.findViewById(R.id.text_date);
        mDateView.setText(mDay+"."+mMonth+"."+mYear);

        Bundle bndl = new Bundle();
        getLoaderManager().initLoader(LOADER_ID, bndl,this).forceLoad();

        return view;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("Set date")) {


            DialogFragment newFragment = new DatePickerFragment();
            newFragment.setTargetFragment(this, REQUEST_CODE);
            newFragment.show(getFragmentManager(), "datePicker");

            getLoaderManager().restartLoader(LOADER_ID, new Bundle(),this).forceLoad();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Set date");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

            Bundle b=data.getBundleExtra("bundle");
            mYear=b.getInt("year");
            mMonth=b.getInt("monthOfYear")+1;
            mDay=b.getInt("dayOfMonth");
            Log.d(TEST,mDay+"."+mMonth+"."+mYear);
            getLoaderManager().restartLoader(LOADER_ID, new Bundle(),this).forceLoad();

        }
    }

    @Override
    public Loader<ExchangePerDate> onCreateLoader(int id, Bundle args) {
        Loader<ExchangePerDate> loader = null;
        if (id == LOADER_ID) {
            Log.d(TEST,mDay+"."+mMonth+"."+mYear);
            loader = new ExchangeTask(getActivity(),mDay+"."+mMonth+"."+mYear);

        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ExchangePerDate> loader, ExchangePerDate data) {
        if(data==null)        Log.d(TEST,"Loader error");
        ListAdapter adapter = new ListAdapter(getActivity(),data.getList());
        mListView.setAdapter(adapter);
        mDateView.setText(data.getDate());
    }

    @Override
    public void onLoaderReset(Loader<ExchangePerDate> loader) {

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putInt("year", year);
            b.putInt("monthOfYear", monthOfYear);
            b.putInt("dayOfMonth", dayOfMonth);
            intent.putExtra("bundle",b);
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(), REQUEST_CODE, intent);

        }
    }

    public class ListAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        LinkedList<Currency> data;
        private TextView mTextCurrency, mTextBuy, mTextSale, mTextBuyNB, mTextSaleNB;

        ListAdapter(Context context, LinkedList<Currency> objects) {
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
                v = lInflater.inflate(R.layout.full_layout, parent, false);
            }
            mTextCurrency = (TextView)v.findViewById(R.id.text_cur_name);
            mTextBuy = (TextView)v.findViewById(R.id.text_cur_buy);
            mTextSale = (TextView)v.findViewById(R.id.text_cur_sale);
            //mTextBuyNB = (TextView)v.findViewById(R.id.text_cur_buy1);
            //mTextSaleNB = (TextView)v.findViewById(R.id.text_cur_sale1);

            Currency curData = data.get(position);
            mTextCurrency.setText(curData.getCurrency());
            //mTextBuy.setText(curData.getPurchaseRate().toString());
            //mTextSale.setText(curData.getSaleRate().toString());
            mTextBuy.setText(curData.getPurchaseRateNB().toString());
            mTextSale.setText(curData.getSaleRateNB().toString());

            return v;
        }
    }
}
