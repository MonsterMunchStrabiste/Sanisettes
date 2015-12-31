/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.alexandre.myslidingtabs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexandre.myslidingtabs.R;
import com.example.alexandre.myslidingtabs.classes.LineAdapter;
import com.example.alexandre.myslidingtabs.classes.NaturalOrderComparator;
import com.example.alexandre.myslidingtabs.listeners.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class ContentFragment extends Fragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";
    private JSONArray biers;
    private RecyclerView mRecyclerView;
    private LineAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    /**
     * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static ContentFragment newInstance(CharSequence title, int indicatorColor,
            int dividerColor) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pager_rv_fromline, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_biers);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), FullDescription.class);
                        try {
                            intent.putExtra("description", biers.getJSONObject(position).toString());//"line", position
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                })
        );
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        biers = getWCFromFile();

        ArrayList<JSONObject> fromLines = new ArrayList<>();
        for (int i = 0; i < biers.length(); i++) {
            try {
                String fields = biers.getJSONObject(i).getString("fields");
                JSONObject inside = new JSONObject(fields);
                if(inside.getString("ligne").equals(args.getCharSequence(KEY_TITLE))) {
                    fromLines.add(biers.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(fromLines, new NaturalOrderComparator());
        biers = new JSONArray(fromLines);
        Log.d("Content", "JSON adapté");


        mAdapter = new LineAdapter(biers);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /*RecyclerView rv_biers = (RecyclerView) RecyclerView.findViewById(R.id.rv_biers);
        adapter = new ByLinesActivity().getWCFromFile();
        rv_biers.setAdapter(adapter);*/

        /*Bundle args = getArguments();

        if (args != null) {
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText("Title: " + args.getCharSequence(KEY_TITLE));

            int indicatorColor = args.getInt(KEY_INDICATOR_COLOR);
            TextView indicatorColorView = (TextView) view.findViewById(R.id.item_indicator_color);
            indicatorColorView.setText("Indicator: #" + Integer.toHexString(indicatorColor));
            indicatorColorView.setTextColor(indicatorColor);

            int dividerColor = args.getInt(KEY_DIVIDER_COLOR);
            TextView dividerColorView = (TextView) view.findViewById(R.id.item_divider_color);
            dividerColorView.setText("Divider: #" + Integer.toHexString(dividerColor));
            dividerColorView.setTextColor(dividerColor);
        }*/

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

    public JSONArray getWCFromFile() {
        try {
            InputStream is = new FileInputStream(getContext().getCacheDir()+"/"+"sanisettes.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            Log.d("Content", "JSON chargé");
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

}
