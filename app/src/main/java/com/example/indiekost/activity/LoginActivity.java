package com.example.indiekost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class LoginActivity extends AppCompatActivity {
    TextInputLayout textInputEmail;
    TextInputLayout textInputPassword;
    TextView textSignUp;
    TextView textForgot;
    SessionManager sessionManager;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        membuat new request queue
        queue = Volley.newRequestQueue(this);

//        instance session manager
        sessionManager = new SessionManager(this);

//        apabila sudah login dan belum logout maka langsung diarahkan ke activity utama
        if (sessionManager.isLogin() == true){
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }

//        mencari elemen by id
        textInputEmail = findViewById(R.id.text_email);
        textInputPassword = findViewById(R.id.text_password);
        textSignUp = findViewById(R.id.signUpText);
        textForgot = findViewById(R.id.forgotPasswordText);

//        elemen onClick
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(register);
            }
        });

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPass = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(forgotPass);
            }
        });
    }

    //    fungsi validasi email
    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Email tidak boleh kosong");
            return false;
        } else {
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
            return true;
        }
    }

    //    fungsi validasi password
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Password tidak boleh kosong");
            return false;
        } else {
            textInputPassword.setError(null);
            textInputPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void _loginProcess() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUTH_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status == "false") {
                        Snackbar.make(findViewById(R.id.loginActivity), message, Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String id = data.getString("id_pengguna");
                        String id_akses = data.getString("id_akses");
                        String email = data.getString("email_pengguna");
                        String nama = data.getString("nama_pengguna");

                        sessionManager.createSession(email, nama, id, id_akses);

                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        main.putExtra("EMAIL", email);
                        main.putExtra("NAMA", nama);
                        main.putExtra("ID", id);
                        main.putExtra("ID_AKSES", id_akses);
                        startActivity(main);
                        finish();
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.loginActivity), e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.loginActivity), error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", textInputEmail.getEditText().getText().toString().trim());
                params.put("password", textInputPassword.getEditText().getText().toString().trim());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    //    fungsi u/ menjalankan konfirmasi form
    public void confirmInputLogin(View v) {
        if (validateEmail() | validatePassword()) {
            _loginProcess();
        }
    }
}
