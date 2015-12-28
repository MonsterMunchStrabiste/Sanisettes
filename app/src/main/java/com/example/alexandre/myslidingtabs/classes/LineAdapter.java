package com.example.alexandre.myslidingtabs.classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexandre.myslidingtabs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alexandre on 27/12/15.
 */
public class LineAdapter extends RecyclerView.Adapter<LineAdapter.BiersHolder> {

    private JSONArray biers;

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
        this.biers = biers;
    }

}

