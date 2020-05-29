
package com.example.indiekost.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indiekost.HomeFragment;
import com.example.indiekost.KamarUserFragment;
import com.example.indiekost.LainnyaUserFragment;
import com.example.indiekost.PembayaranUserFragment;
import com.example.indiekost.R;
import com.example.indiekost.helper.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    BottomNavigationView bottomNavigationView;

    String email, nama, id, id_akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav_user);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_user, new HomeFragment()).commit();

        sessionManager = new SessionManager(this);

        Intent intent = getIntent();

        if (intent.hasExtra("EMAIL")) {
            email = getIntent().getStringExtra("EMAIL");
            nama = getIntent().getStringExtra("NAMA");
            id = getIntent().getStringExtra("ID");
            id_akses = getIntent().getStringExtra("ID_AKSES");
        } else {
            email = sessionManager.getEMAIL();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;

                    switch (item.getItemId()) {
                        case R.id.home_user:
                            selected = new HomeFragment();
                            break;
                        case R.id.kamar_user:
                            selected = new KamarUserFragment();
                            break;
                        case R.id.pembayaran_user:
                            selected = new PembayaranUserFragment();
                            break;
                        case R.id.more_user:
                            selected = new LainnyaUserFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.container_user, selected).commit();

                    return true;
                }
            };

}
