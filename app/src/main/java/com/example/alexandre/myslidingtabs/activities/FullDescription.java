package com.example.alexandre.myslidingtabs.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class FullDescription extends AppCompatActivity {

    JSONObject sanisette;
    JSONObject inside;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_description);
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

        try {
            new LoadImage().execute("http://metrorama.free.fr/2005-08-PorteDeVersailles12P.jpg");
            //ImageView header = (ImageView) findViewById(R.id.header);
            TextView title = (TextView) findViewById(R.id.name);
            TextView line = (TextView) findViewById(R.id.line);
            TextView description = (TextView) findViewById(R.id.description);
            //header.setImageResource(R.mipmap.ic_launcher);
            //header.setImageBitmap(downloadBitmap("http://metrorama.free.fr/2005-08-PorteDeVersailles12P.jpg"));
            setTitle(inside.getString("station"));
            title.setText(inside.getString("station") );
            line.setText(inside.getString("ligne"));
            description.setText(inside.getString("localisation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
