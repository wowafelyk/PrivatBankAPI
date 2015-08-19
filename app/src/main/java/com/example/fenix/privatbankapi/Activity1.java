package com.example.fenix.privatbankapi;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment newFragment;
        FragmentTransaction transaction;
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_fragment1:

                newFragment = new Activity1Fragment();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentcontainer1, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.action_fragment2:

                newFragment = new ExchangeFragment();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentcontainer1, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.action_fragment3:
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
