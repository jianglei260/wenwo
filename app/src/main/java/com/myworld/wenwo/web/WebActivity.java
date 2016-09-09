package com.myworld.wenwo.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.myworld.wenwo.R;
import com.myworld.wenwo.view.widget.TitleBar;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private TitleBar titleBar;
    private ProgressBar progressBar;
    private String url, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        webView = (WebView) findViewById(R.id.web);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (TextUtils.isEmpty(title))
                    titleBar.getTextView().setText(view.getTitle());
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://www.wenwobei.com/")) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl(url);
        titleBar.getTextView().setText(title);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void startWebActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
