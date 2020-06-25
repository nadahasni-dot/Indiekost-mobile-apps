package com.example.indiekost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.indiekost.R;

public class SignUpActivity extends AppCompatActivity {
    private static final String URL_SIGNUP = "http://indiekost.mif-project.com/auth/register";
    WebView signup;
    WebSettings webSettings;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        signup = findViewById(R.id.browser_sign_up);

        webSettings = signup.getSettings();
        webSettings.setJavaScriptEnabled(true);

        signup.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        signup.loadUrl(URL_SIGNUP);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && signup.canGoBack()) {
            signup.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}