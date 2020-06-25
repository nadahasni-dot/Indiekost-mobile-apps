package com.example.indiekost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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
import com.example.indiekost.helper.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RubahPasswordActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    Button btnRubahPassword;
    TextInputLayout passwordLama, passwordBaru, passwordBaruAgain;

    String mIdPengguna, mPasswordLama, mPasswordBaru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubah_password);

        getSupportActionBar().setTitle("Rubah Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        mIdPengguna = sessionManager.getIdPengguna();

        btnRubahPassword = findViewById(R.id.btn_rubah_password_execute);
        passwordLama = findViewById(R.id.text_password_lama);
        passwordBaru = findViewById(R.id.text_password_new);
        passwordBaruAgain = findViewById(R.id.text_password_new_again);

        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        btnRubahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePasswordLama() & validatePasswordBaru() & validateConfirmPassword() & validateEqualPassword()){
                    mPasswordLama = passwordLama.getEditText().getText().toString().trim();
                    mPasswordBaru = passwordBaruAgain.getEditText().getText().toString().trim();
                    rubahPassword();
                }
            }
        });
    }

    private boolean validatePasswordLama() {
        String password_lama = passwordLama.getEditText().getText().toString().trim();

        if (password_lama.isEmpty()) {
            passwordLama.setError("Password lama tidak boleh kosong!");
            return false;
        } else {
            passwordLama.setError(null);
            mPasswordLama = password_lama;
            return true;
        }
    }

    private boolean validatePasswordBaru() {
        String password_baru = passwordBaru.getEditText().getText().toString().trim();

        if (password_baru.isEmpty()) {
            passwordBaru.setError("Password baru tidak boleh kosong!");
            return false;
        } else {
            passwordBaru.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password_baru_again = passwordBaruAgain.getEditText().getText().toString().trim();

        if (password_baru_again.isEmpty()) {
            passwordBaruAgain.setError("Password baru tidak boleh kosong!");
            return false;
        } else {
            passwordBaruAgain.setError(null);
            mPasswordBaru = password_baru_again;
            return true;
        }
    }

    private boolean validateEqualPassword() {
        String password_baru = passwordBaru.getEditText().getText().toString().trim();
        String password_baru_again = passwordBaruAgain.getEditText().getText().toString().trim();

        if (!password_baru.equals(password_baru_again)) {
            passwordBaruAgain.setError("Konfirmasi password tidak sama!");
            return false;
        } else {
            passwordBaruAgain.setError(null);
            mPasswordBaru = password_baru_again;
            return true;
        }
    }

    private void rubahPassword() {
        progressDialog.setMessage("Sedang Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest rubahPasswordRequest = new StringRequest(Request.Method.PUT, ApiUrl.RUBAH_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("true")) {
                        Snackbar.make(findViewById(R.id.activity_rubah_password), message, Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(findViewById(R.id.activity_rubah_password), message, Snackbar.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.activity_rubah_password), e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "Terjadi error. Coba beberapa saat lagi.";

                if (error instanceof NetworkError){
                    message = "Tidak dapat terhubung ke internet. Harap periksa koneksi anda.";
                } else if (error instanceof AuthFailureError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError){
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError){
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Snackbar.make(findViewById(R.id.activity_rubah_password), message, Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pengguna", mIdPengguna);
                params.put("password_lama", mPasswordLama);
                params.put("password_new", mPasswordBaru);

                return params;
            }
        };

        requestQueue.add(rubahPasswordRequest);
    }
}
