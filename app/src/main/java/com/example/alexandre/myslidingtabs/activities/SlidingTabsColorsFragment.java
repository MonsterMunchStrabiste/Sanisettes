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


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexandre.myslidingtabs.R;
import com.example.alexandre.myslidingtabs.classes.NaturalOrderComparator;
import com.example.alexandre.myslidingtabs.view.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import SlidingTabLayout;

/**
 * A basic sample which shows how to use {@link SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsColorsFragment extends Fragment {

    /**
     * This class represents a tab to be displayed by {@link ViewPager} and it's associated
     * {@link SlidingTabLayout}.
     */
    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        /**
         * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
         */
        Fragment createFragment() {
            return ContentFragment.newInstance(mTitle, mIndicatorColor, mDividerColor);
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {
            return mTitle;
        }

        /**
         * @return the color to be used for indicator on the {@link SlidingTabLayout}
         */
        int getIndicatorColor() {
            return mIndicatorColor;
        }

        /**
         * @return the color to be used for right divider on the {@link SlidingTabLayout}
         */
        int getDividerColor() {
            return mDividerColor;
        }
    }

    static final String LOG_TAG = "SlidingTabsColorsFragment";

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * List of {@link SamplePagerItem} which represent this sample's tabs.
     */
    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> lines;
        int color;
        lines = (ArrayList) this.getArguments().getSerializable("lines");
        // BEGIN_INCLUDE (populate_tabs)
        /**
         * Populate our tab list with tabs. Each item contains a title, indicator color and divider
         * color, which are used by {@link SlidingTabLayout}.
         */

        for ( int i = 0; i < lines.size() ; i++ ) {
            switch (lines.get(i)) {
                case "1":
                    color = 0xFFF2C931;
                    break;
                case "2":
                    color = 0xFF216eb4;
                    break;
                case "3":
                    color = 0xFF9a9940;
                    break;
                case "4":
                    color = 0xFFbb4d98;
                    break;
                case "5":
                    color = 0xFFde8b53;
                    break;
                case "6":
                    color = 0xFF79bb92;
                    break;
                case "7":
                    color = 0xFFdf9ab1;
                    break;
                case "8":
                    color = 0xFFc5a3ca;
                    break;
                case "9":
                    color = 0xFFcec92b;
                    break;
                case "10":
                    color = 0xFFdfb039;
                    break;
                case "11":
                    color = 0xFF8e6538;
                    break;
                case "12":
                    color = 0xFF328e5b;
                    break;
                case "13":
                    color = 0xFF89c7d6;
                    break;
                case "14":
                    color = 0xFF67328e;
                    break;
                case "A":
                    color = 0xFFD1302F;
                    break;
                case "B":
                    color = 0xFF427DBD;
                    break;
                case "C":
                    color = 0xFFFCD946;
                    break;
                case "D":
                    color = 0xFF5E9620;
                    break;
                case "E":
                    color = 0xFFBD76A1;
                    break;
                default:
                    color = Color.GRAY;
            }
            mTabs.add(new SamplePagerItem(
                    "ligne " + lines.get(i), // Title
                    color, // Indicator color
                    Color.GRAY // Divider color
            ));
        }
        // END_INCLUDE (populate_tabs)
    }

    private static final String TAG = Thread.currentThread().getName();

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of
     * {@link SampleFragmentPagerAdapter}. The {@link SlidingTabLayout} is then given the
     * {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // BEGIN_INCLUDE (tab_colorizer)
        // Set a TabColorizer to customize the indicator and divider colors. Here we just retrieve
        // the tab at the position, and return it's set color
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });
        // END_INCLUDE (tab_colorizer)
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link FragmentPagerAdapter} used to display pages in this sample. The individual pages
     * are instances of {@link ContentFragment} which just display three lines of text. Each page is
     * created by the relevant {@link SamplePagerItem} for the requested position.
     * <p>
     * The important section of this class is the {@link #getPageTitle(int)} method which controls
     * what is displayed in the {@link SlidingTabLayout}.
     */
    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        /*public SampleFragmentPagerAdapter(JSONArray biers) {
            this.WCs = biers;
        }*/


        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the {@link android.support.v4.app.Fragment} to be displayed at {@code position}.
         * <p>
         * Here we return the value returned from {@link SamplePagerItem#createFragment()}.
         */
        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we return the value returned from {@link SamplePagerItem#getTitle()}.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /*public void setNewLines() {
            WCs = new MainActivity().getWCFromFile();

            lines = new ArrayList();
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
            this.lines = new ArrayList(uniqueLines);
            Collections.sort(this.lines, new NaturalOrderComparator());
            mTabs = new ArrayList<SamplePagerItem>();
            for ( int i = 0; i < lines.size() ; i++ ) {
                mTabs.add(new SamplePagerItem(
                        lines.get(i), // Title
                        Color.YELLOW, // Indicator color
                        Color.GRAY // Divider color
                ));
            }
            Log.d(TAG, "Fichier rechargé");
            notifyDataSetChanged();
        }*/


    }

}