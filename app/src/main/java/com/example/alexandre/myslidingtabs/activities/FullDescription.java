package com.example.alexandre.myslidingtabs.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.alexandre.myslidingtabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + inside.getString("coord_x") + "," + inside.getString("coord_y") + "&mode=w" );
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            TextView title = (TextView) findViewById(R.id.name);
            TextView line = (TextView) findViewById(R.id.line);
            TextView description = (TextView) findViewById(R.id.description);
            title.setText(inside.getString("station") );
            line.setText(inside.getString("ligne"));
            description.setText(inside.getString("localisation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
