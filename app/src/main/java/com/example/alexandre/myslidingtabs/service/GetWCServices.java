package com.example.alexandre.myslidingtabs.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.alexandre.myslidingtabs.activities.ByLinesActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetWCServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_ALL_BIERS = "com.example.alexandre.myslidingtabs.GET_ALL_BIERS";
    private static final String TAG = "GetWCServices";

    // TODO: Rename parameters
    /*private static final String EXTRA_PARAM1 = "orf.esiea.villa_rastel.progmobile.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "orf.esiea.villa_rastel.progmobile.extra.PARAM2";*/

    public GetWCServices() {
        super("GetWCServices");
    }

    /**
     * Starts this service to perform action GET_ALL_BIERS with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBiers(Context context) {
        Intent intent = new Intent(context, GetWCServices.class);
        intent.setAction(ACTION_GET_ALL_BIERS);
        /*intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);*/
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ALL_BIERS.equals(action)) {
                handleActionBiers();
            }
        }
    }

    /**
     * Handle action GET_ALL_BIERS in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBiers() {
        // TODO: Handle action GET_ALL_BIERS
        URL url = null;
        try {
            url = new URL("http://data.ratp.fr/explore/dataset/sanitaires-reseau-ratp/download/?format=json&timezone=Europe/Berlin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "sanisettes.json"));
                Log.d(TAG, "json download");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ByLinesActivity.WC_UPDATE));
            }
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        }*/ catch (IOException e) {
            e.printStackTrace();
        }
        //throw new UnsupportedOperationException("Not yet implemented");

    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len= inputStream.read(buf))>0) {
                out.write(buf,0,len);
            }
            out.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
