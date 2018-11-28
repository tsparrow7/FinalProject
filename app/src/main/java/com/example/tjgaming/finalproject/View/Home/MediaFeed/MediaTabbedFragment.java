package com.example.tjgaming.finalproject.View.Home.MediaFeed;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.View.Home.Movies.MoviesFragment;
import com.example.tjgaming.finalproject.View.Home.TVShows.TVShowsFragment;

public class MediaTabbedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MediaViewPagerAdapter adapter;

    public MediaTabbedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_tabbed, container, false);

        tabLayout = view.findViewById(R.id.media_tab_layout);
        viewPager = view.findViewById(R.id.media_view_pager);
        adapter = new MediaViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new TVShowsFragment(), "TV Shows");
        adapter.addFragment(new MoviesFragment(), "Movies");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
