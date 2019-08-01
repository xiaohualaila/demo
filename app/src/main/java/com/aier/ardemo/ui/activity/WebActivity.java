package com.aier.ardemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import butterknife.BindView;

public class WebActivity extends BaseActivity {
    private static final String TAG = "WebActivity";
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.web_title)
    TextView tv_title;
    @BindView(R.id.web_toolbar)
    Toolbar mWebToolbar;
    @BindView(R.id.web_progressBar)
    ProgressBar mWebProgressBar;
    private String codedContent;
    private String title;
    private int  type = 1;
    @Override
    protected void initViews() {
        title = getIntent().getStringExtra("title");
        codedContent = getIntent().getStringExtra("codedContent");
        type = getIntent().getIntExtra("type", 1);
        mWebToolbar.setNavigationOnClickListener(v -> finish());
        //4.2 开启辅助功能崩溃

        initWebSettings();

           if(title.length()>15){
               tv_title.setTextSize(14f);
           }
           tv_title.setText(title);

           if(type==2){
               mWebView.loadUrl("https:"+codedContent);
           }else {
               mWebView.loadUrl(codedContent);
           }
        Log.i(TAG,codedContent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    private void initWebSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new MyWebChrome());
        mWebView.setWebViewClient(new MyWebClient());
    }

    @Override
     public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private  class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mWebProgressBar.setVisibility(View.VISIBLE);
            mWebProgressBar.setProgress(newProgress);
        }
    }

    private  class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view,String url) {
            mWebProgressBar.setVisibility(View.GONE);
        }
    }


    public static final void  startToWebAc(Context context,String title,String content,int type){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("codedContent",content);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }


}
