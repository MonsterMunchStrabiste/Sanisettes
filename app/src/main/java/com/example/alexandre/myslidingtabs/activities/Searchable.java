package com.example.alexandre.myslidingtabs.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexandre.myslidingtabs.R;
import com.example.alexandre.myslidingtabs.classes.NaturalOrderComparator;
import com.example.alexandre.myslidingtabs.listeners.RecyclerItemClickListener;
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

public class Searchable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "ByLinesActivity";

    private ArrayList<String> lines;
    private JSONArray biers;
    private RecyclerView mRecyclerView;
    private LineAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.pager_rv_fromline);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_biers);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getBaseContext(), FullDescription.class);
                        try {
                            intent.putExtra("description", biers.getJSONObject(position).toString());//"line", position
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                })
        );


        IntentFilter intentFilter = new IntentFilter(WC_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new WCUpdate(), intentFilter);
        GetWCServices.startActionBiers(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setNewLines(query);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Le fichier vient d'être mis à jour", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                GetWCServices.startActionBiers(getApplicationContext());
                setNewLines();
            }
        });*/

    }


    private class LineAdapter extends RecyclerView.Adapter<LineAdapter.BiersHolder> {

        @Override
        public LineAdapter.BiersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_wc_element, parent, false);
            BiersHolder bh = new BiersHolder(v);
            return bh;
        }

        @Override
        public void onBindViewHolder(LineAdapter.BiersHolder holder, int position) {
            try {
                //holder.name.setText(biers.getJSONObject(position).getString("datasetid"));
                //holder.name.setText((String) lines.get(lineIndex));
                //holder.name.setText(lineName);
                String fields = biers.getJSONObject(position).getString("fields");
                JSONObject inside = new JSONObject(fields);
                holder.name.setText(inside.getString("station"));
                holder.description.setText(inside.getString("localisation"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {

            return biers.length();
        }


        public class BiersHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView description;
            public BiersHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_wc_element_name);
                description = (TextView) itemView.findViewById(R.id.rv_wc_element_description);
            }
        }

        public LineAdapter(JSONArray biers) {
            biers = biers;
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

        } else if (id == R.id.nav_send) {

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

    public void setNewLines(String query) {
        biers = getWCFromFile();


        ArrayList<JSONObject> fromLines = new ArrayList<>();
        for (int i = 0; i < biers.length(); i++) {
            try {
                String fields = biers.getJSONObject(i).getString("fields");
                JSONObject inside = new JSONObject(fields);
                if(inside.getString("ligne").equals(query)) {
                    fromLines.add(biers.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(fromLines, new NaturalOrderComparator());
        biers = new JSONArray(fromLines);
        Log.d(TAG, "Fichier seeked");
        //notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bylines, menu);
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
