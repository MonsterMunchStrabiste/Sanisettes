package com.example.alexandre.myslidingtabs.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexandre.myslidingtabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FullDescription extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    JSONObject sanisette;
    JSONObject inside;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities);

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.content_full_description, null);

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.app_bar);
        insertPoint.addView(v, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        //lineIndex = extras.getInt("line");
        try {
            sanisette = new JSONObject(extras.getString("description"));
            String fields = sanisette.getString("fields");
            inside = new JSONObject(fields);
        } catch (JSONException e ) {
            e.printStackTrace();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_directions_walk));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + inside.getString("coord_x") + "," + inside.getString("coord_y") + "&mode=w");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            new LoadImage().execute("http://metrorama.free.fr/2005-08-PorteDeVersailles12P.jpg");
            //ImageView header = (ImageView) findViewById(R.id.header);
            //TextView title = (TextView) findViewById(R.id.name);
            TextView line = (TextView) findViewById(R.id.line);
            TextView description = (TextView) findViewById(R.id.description);
            TextView working = (TextView) findViewById(R.id.working);
            TextView price = (TextView) findViewById(R.id.price);
            TextView access = (TextView) findViewById(R.id.access);
            TextView pmr = (TextView) findViewById(R.id.pmr);
            //TextView line = (TextView) findViewById(R.id.line);
            //header.setImageResource(R.mipmap.ic_launcher);
            //header.setImageBitmap(downloadBitmap("http://metrorama.free.fr/2005-08-PorteDeVersailles12P.jpg"));
            setTitle(inside.getString("station"));
            //title.setText(inside.getString("station"));
            line.setText(inside.getString("ligne"));
            description.setText(inside.getString("localisation"));
            working.setText(inside.getString("actif_o_n"));
            price.setText(inside.getString("tarif_gratuit_payant"));
            access.setText(inside.getString("acces_bouton_poussoir"));
            pmr.setText(inside.getString("accessible_pmr_oui_non"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    class LoadImage extends AsyncTask<String, Void, Bitmap> {


        protected Bitmap doInBackground(String... urls) {
            try {
                return downloadBitmap(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Bitmap bitmap) {
            // TODO: check this.exception
            // TODO: do something with the feed
            ImageView header = (ImageView) findViewById(R.id.header);
            header.setImageBitmap(bitmap);
        }
    }

    private Bitmap downloadBitmap(String lien) throws MalformedURLException {
        URL url = new URL(lien);
        try {
            //url = new URL("http://data.ratp.fr/explore/dataset/sanitaires-reseau-ratp/download/?format=json&timezone=Europe/Berlin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                final Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                Log.d("bitmap", "json download");
                return bitmap;
            }
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        }*/ catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
