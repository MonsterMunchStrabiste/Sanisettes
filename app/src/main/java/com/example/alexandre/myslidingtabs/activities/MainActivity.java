package com.example.alexandre.myslidingtabs.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alexandre.myslidingtabs.R;
import com.example.alexandre.myslidingtabs.classes.NaturalOrderComparator;
import com.example.alexandre.myslidingtabs.service.GetWCServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ArrayList<String> lines;
    private JSONArray WCs;

    //public SlidingTabsColorsFragment.SampleFragmentPagerAdapter adapter;

    // Whether the Log Fragment is currently shown
    //private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setNewLines();
            }
        });

        IntentFilter intentFilter = new IntentFilter(WC_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new WCUpdate(), intentFilter);
        GetWCServices.startActionBiers(this);
        setNewLines();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("lines", lines);
            fragment.setArguments(bundle);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }


        /*adapter = new SlidingTabsColorsFragment.SampleFragmentPagerAdapter(getWCFromFile());
        rv_biers.setAdapter(adapter);*/
        /*ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);*/
        //SlidingTabsColorsFragment.SampleFragmentPagerAdapter adapter = new SlidingTabsColorsFragment.SampleFragmentPagerAdapter(getWCFromFile());
        //adapter.setNewLines();
    }

    public static final String WC_UPDATE = "WC_UPDATE";
    public class WCUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d("wc receive", intent.getAction().toString());//getIntent().getAction());
            //new SlidingTabsColorsFragment.SampleFragmentPagerAdapter.setNewLines();
        }
    }

    public JSONArray getWCFromFile() {
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"sanisettes.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            //File chacheDir = getCodeCacheDir();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public void setNewLines() {
        WCs = getWCFromFile();

        lines = new ArrayList<String>();
        for (int i = 0; i < WCs.length(); i++) {
            try {
                String fields = WCs.getJSONObject(i).getString("fields");
                JSONObject inside = new JSONObject(fields);
                lines.add(inside.getString("ligne"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Set uniqueLines = new HashSet<String>(lines);
        //this.lines = Collections.sort(uniqueLines);
        this.lines = new ArrayList<String>(uniqueLines);
        Collections.sort(this.lines, new NaturalOrderComparator());
        Log.d(TAG, "Fichier rechargé");
        //notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);*/

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }*/
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}