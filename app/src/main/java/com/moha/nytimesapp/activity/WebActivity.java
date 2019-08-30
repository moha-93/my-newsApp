package com.moha.nytimesapp.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.moha.nytimesapp.R;

public class WebActivity extends AppCompatActivity {
    public WebView webView;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Toolbar toolbar = findViewById(R.id.toolbar_2nd);
        toolbar.setTitle("News Page");
        setSupportActionBar(toolbar);
        webView = findViewById(R.id.web_view);
        dialog = new ProgressDialog(WebActivity.this);

        String url = getIntent().getStringExtra("url");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setMessage("Loading..Please wait.");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        webView.loadUrl(url);

    }



    public static class WebViewClient extends android.webkit.WebViewClient {
    }


}
