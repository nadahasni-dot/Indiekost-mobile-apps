package com.example.indiekost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.indiekost.R;
import com.example.indiekost.api.ApiUrl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    TextInputLayout textEmail;
    MaterialButton btnForgotPass, btnKembali;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        requestQueue = Volley.newRequestQueue(this);
        textEmail = findViewById(R.id.txt_forgot_password);
        btnForgotPass = findViewById(R.id.btn_forgot_pass);
        btnKembali = findViewById(R.id.btnForgetKeLogin);
        progressDialog = new ProgressDialog(this);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kembali = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(kembali);
                finish();
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput(textEmail, "harap isi email dengan benar")) {
                    resetPassword();
                }
            }
        });
    }

    private boolean validateInput(TextInputLayout textInput, String error) {
        String input = textInput.getEditText().getText().toString().trim();

        if (input.isEmpty()) {
            textInput.setErrorEnabled(true);
            textInput.setError(error);
            return false;
        } else {
            textInput.setError(null);
            textInput.setErrorEnabled(false);
            return true;
        }
    }


    private void resetPassword() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.FORGOT_PASSWORD;
        StringRequest resetPasswordRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");

                    if (status.equals("false")){
                        Snackbar.make(findViewById(R.id.loginActivity), message, Snackbar.LENGTH_LONG).show();
                    } else {
                        Intent success = new Intent(ForgotPassActivity.this, ForgotPassSuccessActivity.class);
                        success.putExtra("EMAIL", textEmail.getEditText().getText().toString().trim());
                        startActivity(success);
                        finish();
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.loginActivity), e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String message = "Terjadi error. Coba beberapa saat lagi.";

                if (error instanceof NetworkError){
                    message = "Tidak dapat terhubung ke internet. Harap periksa koneksi anda.";
                } else if (error instanceof AuthFailureError) {
                    message = "Gagal. Harap periksa email.";
                } else if (error instanceof ClientError) {
                    message = "Gagal. Harap periksa email.";
                } else if (error instanceof NoConnectionError){
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError){
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Snackbar.make(findViewById(R.id.activityForgotPass), message, Snackbar.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", textEmail.getEditText().getText().toString().trim());
                return params;
            }
        };

        requestQueue.add(resetPasswordRequest);
    }
}
