package com.example.tjgaming.finalproject.View.Home.Forum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.tjgaming.finalproject.R;

/**
 * Created by TJ on 10/5/2018.
 */

public class ForumFragment extends Fragment {

    private WebView mWebView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        mWebView = (WebView) view.findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://stackoverflow.com/");
        mWebView.setWebViewClient(new WebViewClient());
        return view;
    }

    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            onBackPressed();
        }
    }
}
