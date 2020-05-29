package com.example.indiekost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indiekost.R;
import com.example.indiekost.helper.SessionManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    TextView textEmail;
    Button logoutButton;
    SessionManager sessionManager;

    String email, nama, id, id_akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        Intent intent = getIntent();

        if(intent.hasExtra("EMAIL")){
            email = getIntent().getStringExtra("EMAIL");
            nama = getIntent().getStringExtra("NAMA");
            id = getIntent().getStringExtra("ID");
            id_akses = getIntent().getStringExtra("ID_AKSES");
        }else{
            email = sessionManager.getEMAIL();
        }

        textEmail = findViewById(R.id.testEmail);
        textEmail.setText(email);

        logoutButton = findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _logout();
            }
        });
    }

    private void _logout(){
        sessionManager.clearPreferences();

        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}
