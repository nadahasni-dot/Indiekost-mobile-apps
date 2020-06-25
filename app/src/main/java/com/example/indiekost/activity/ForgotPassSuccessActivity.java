package com.example.indiekost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.indiekost.R;
import com.google.android.material.button.MaterialButton;

public class ForgotPassSuccessActivity extends AppCompatActivity {
    TextView messageSuccess;
    MaterialButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_success);

        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");

        String message = "Harap cek email anda. Tautan untuk mereset password anda telah kami kirim ke: "+ email +". Anda dapat mereset password melalui email anda selama token valid.";

        messageSuccess = findViewById(R.id.textView40);
        backButton = findViewById(R.id.btnSuccessKeLogin);

        messageSuccess.setText(message);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassSuccessActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
