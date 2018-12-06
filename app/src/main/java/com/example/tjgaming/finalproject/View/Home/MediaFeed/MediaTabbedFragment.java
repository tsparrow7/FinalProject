package com.example.tjgaming.finalproject.View.Home.MediaFeed;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.Utils.CustomStrings;
import com.example.tjgaming.finalproject.View.Home.HomeActivity;
import com.example.tjgaming.finalproject.View.Home.Movies.MoviesFragment;
import com.example.tjgaming.finalproject.View.Home.TVShows.TVShowsFragment;

public class MediaTabbedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MediaViewPagerAdapter adapter;

    private MoviesFragment moviesFragment = new MoviesFragment();
    private TVShowsFragment tvShowsFragment = new TVShowsFragment();

    public MediaTabbedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_tabbed, container, false);

        tabLayout = view.findViewById(R.id.media_tab_layout);
        viewPager = view.findViewById(R.id.media_view_pager);
        adapter = new MediaViewPagerAdapter(getChildFragmentManager());
        Bundle bundle = getArguments();


        if (getArguments().getString(HomeActivity.sTypeOfBundle).equals(HomeActivity.sNavDrawer) ||
                getArguments().getString(HomeActivity.sTypeOfBundle).equals(HomeActivity.sLogin)) {
            //if the nav drawer is what inflates this fragment
            adapter.addFragment(tvShowsFragment, CustomStrings.TV_SHOWS_FRAGMENT);
            adapter.addFragment(moviesFragment, CustomStrings.MOVIE_FRAGMENT);

        }  else if (getArguments().getString(HomeActivity.sTypeOfBundle).equals(HomeActivity.sFilter)) {
            //if the filter dialog is what inflates this fragment
            if (getArguments().getString(HomeActivity.sFragmentBeingFiltered).equals(CustomStrings.TV_SHOWS_FRAGMENT)){
                tvShowsFragment.setArguments(getArguments());
            } else if (getArguments().getString(HomeActivity.sFragmentBeingFiltered).equals(CustomStrings.MOVIE_FRAGMENT)){
                moviesFragment.setArguments(getArguments());
            }
            adapter.addFragment(tvShowsFragment, CustomStrings.TV_SHOWS_FRAGMENT);
            adapter.addFragment(moviesFragment, CustomStrings.MOVIE_FRAGMENT);

        } else if (getArguments().getString(HomeActivity.sTypeOfBundle).equals(HomeActivity.sSearch)) {
            //if the search dialog is what inflates this fragment

            if (getArguments().getString(HomeActivity.sFragmentBeingFiltered).equals(CustomStrings.TV_SHOWS_FRAGMENT)){
                tvShowsFragment.setArguments(getArguments());
            } else if (getArguments().getString(HomeActivity.sFragmentBeingFiltered).equals(CustomStrings.MOVIE_FRAGMENT)){
                moviesFragment.setArguments(getArguments());
            }

            adapter.addFragment(tvShowsFragment, CustomStrings.TV_SHOWS_FRAGMENT);
            adapter.addFragment(moviesFragment, CustomStrings.MOVIE_FRAGMENT);
        }

        viewPager.setAdapter(adapter);

        Log.i("MediaTabbedFragment", bundle.toString());

        //If we are filtering data we want to go to sorted page
        if (getArguments().getString(HomeActivity.sTypeOfBundle).equals(HomeActivity.sFilter) ||
                getArguments().getString(HomeActivity.sTypeOfBundle).equals(HomeActivity.sSearch)) {
            if (getArguments().getString(HomeActivity.sFragmentBeingFiltered).equals(CustomStrings.MOVIE_FRAGMENT)) {
                viewPager.setCurrentItem(1,true);
            } else if (getArguments().getString(HomeActivity.sFragmentBeingFiltered).equals(CustomStrings.TV_SHOWS_FRAGMENT)){
                viewPager.setCurrentItem(0);
            }
        }

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
