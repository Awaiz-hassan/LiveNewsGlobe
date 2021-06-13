package com.cc.globenewslive.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.cc.globenewslive.MainActivity;
import com.cc.globenewslive.R;
import com.cc.globenewslive.Utils.ExtractUrl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewNews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewNews extends Fragment {

    private static final String GET_URL = "URL";
    private String URL;
    private Toolbar toolbar;
    private ImageView imageView;
    android.webkit.WebView webView;
    private ConstraintLayout lottieAnimationView;


    public WebViewNews() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WebViewNews newInstance(String URL) {
        WebViewNews fragment = new WebViewNews();
        Bundle args = new Bundle();
        args.putString(GET_URL, URL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            URL = getArguments().getString(GET_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_web_view_news, container, false);
        webView=view.findViewById(R.id.WebView1);
        toolbar=view.findViewById(R.id.toolBar);
        lottieAnimationView=view.findViewById(R.id.animationView);
        imageView=view.findViewById(R.id.backbutton);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);
        webView.setVisibility(View.GONE);
        ExtractUrl.CheckConnection(getActivity());
        webView.setWebViewClient(new WebViewClient() {

            private View mCustomView;
            private WebChromeClient.CustomViewCallback mCustomViewCallback;
            protected FrameLayout mFullscreenContainer;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;

            public Bitmap getDefaultVideoPoster()
            {
                if (mCustomView == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), 2130837573);
            }

            public void onHideCustomView()
            {
                ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
                this.mCustomView = null;
                getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                getActivity().setRequestedOrientation(this.mOriginalOrientation);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
            {
                if (this.mCustomView != null)
                {
                    onHideCustomView();
                    return;
                }
                this.mCustomView = paramView;
                this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
                this.mOriginalOrientation = getActivity().getRequestedOrientation();
                this.mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                getActivity().getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            @Override
            public void onLoadResource(android.webkit.WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('td-header-template-wrap')[0].style.display='none'; " +
                        "})()");
                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('td-footer-template-wrap')[0].style.display='none'; " +
                        "})()");

                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {


                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                view.loadUrl(url); // load the url
                return true;
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {

                if(newProgress==100){
                    webView.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack()){
                    webView.goBack();
                }
                else{
                    MainActivity.mainRelativelayout.setVisibility(View.VISIBLE);
                    MainActivity.webviewFragment.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }
}