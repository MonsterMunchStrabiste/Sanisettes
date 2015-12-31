package com.example.alexandre.myslidingtabs.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

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

public class ByLinesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "ByLinesActivity";

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Le fichier vient d'être mis à jour", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                GetWCServices.startActionBiers(getApplicationContext());
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

    }

    public static final String WC_UPDATE = "WC_UPDATE";

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.filterbylines) {
            Intent intent = new Intent(this, ByLinesActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Je viens de découvrir l'application des Alexandre, elle est vraiment superbe. Ils auront 20!");
            startActivity(Intent.createChooser(intent, "Partager le message"));
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_TEXT, "Merci beaucoup pour cette superbe application, je vous mets 20 ! :)");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Merci");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "villa@et.esiea.fr" , "rastel@et.esiea.fr" });
            startActivity(Intent.createChooser(intent, "Envoyer un mail de remerciement"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
        getMenuInflater().inflate(R.menu.bylines, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        /*SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default*/

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
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}