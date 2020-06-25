package com.example.indiekost.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.indiekost.ErrorFragment;
import com.example.indiekost.HomeFragment;
import com.example.indiekost.R;
import com.example.indiekost.api.ApiUrl;
import com.example.indiekost.helper.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfilActivity extends AppCompatActivity {
    CheckBox checkBox;
    CardView cardView;
    AutoCompleteTextView jenisKelaminDropdown;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    RequestQueue queue;

    String[] JK = new String[]{"Wanita", "Pria"};

    ArrayAdapter<String> adapter;

    private final int IMG_REQUEST = 1;

    String mIdPengguna;
    String mNik;
    String mNama;
    String mTanggal;
    String mJenisKelamin;
    String mAlamat;
    String mProvinsi;
    String mKota;
    String mTelepon;
    String mEmail;
    String mFoto;

    boolean statusImage = false;

    Button btnUpdateProfil, btnPilihFoto;
    TextInputLayout txtNik, txtNama, txtTanggal, txtJenisKelamin, txtAlamat, txtProvinsi, txtKota, txtTelepon, txtEmail;
    TextView txtPathGambar;
    Bitmap bitmapProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        getSupportActionBar().setTitle("Edit Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, JK);

        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);
        queue = Volley.newRequestQueue(this);

        mIdPengguna = sessionManager.getIdPengguna();

        initWidgetId();

        jenisKelaminDropdown.setAdapter(adapter);
        btnUpdateProfil.setEnabled(false);

//        load data dari server
        loadProfil();

//        item on click
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    cardView.setVisibility(View.VISIBLE);
                } else {
                    cardView.setVisibility(View.GONE);
                }
            }
        });

        btnPilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnUpdateProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
//                    proses dengan rubah gambar
                    if (validateTextInput(txtNik, "Nik harus diisi!") &
                            validateTextInput(txtNama, "Nama harus diisi!") &
                            validateTextInput(txtTanggal, "Tanggal lahir harus diisi!") &
                            validateTextInput(txtJenisKelamin, "Jenis kelamin harus diisi! (pilih sesuai yang tertera)") &
                            validateTextInput(txtAlamat, "Alamat harus diisi!") &
                            validateTextInput(txtProvinsi, "Provinsi harus diisi!") &
                            validateTextInput(txtKota, "Kota harus diisi!") &
                            validateTextInput(txtTelepon, "Telepon harus diisi!") &
                            validateTextInput(txtEmail, "Email harus diisi!") & statusImage) {

                        updateProfil();

                    } else {
                        Snackbar.make(findViewById(R.id.activity_edit_profil), "Data diri belum terpenuhi", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
//                    proses tanpa rubah gambar
                    if (validateTextInput(txtNik, "Nik harus diisi!") &
                            validateTextInput(txtNama, "Nama harus diisi!") &
                            validateTextInput(txtTanggal, "Tanggal lahir harus diisi!") &
                            validateTextInput(txtJenisKelamin, "Jenis kelamin harus diisi! (pilih sesuai yang tertera)") &
                            validateTextInput(txtAlamat, "Alamat harus diisi!") &
                            validateTextInput(txtProvinsi, "Provinsi harus diisi!") &
                            validateTextInput(txtKota, "Kota harus diisi!") &
                            validateTextInput(txtTelepon, "Telepon harus diisi!") &
                            validateTextInput(txtEmail, "Email harus diisi!")) {

                        updateProfil();

                    } else {
                        Snackbar.make(findViewById(R.id.activity_edit_profil), "Data diri belum terpenuhi", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //    fungsi untuk memilih gambar dari galery
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    private void initWidgetId() {
        checkBox = findViewById(R.id.check_image_update);
        cardView = findViewById(R.id.card_image);
        jenisKelaminDropdown = findViewById(R.id.text_dropdown_jenis_kelamin);
        btnUpdateProfil = findViewById(R.id.btn_update_profile);
        btnPilihFoto = findViewById(R.id.btn_edit_profil);
        txtPathGambar = findViewById(R.id.text_path_gambar);
        txtNik = findViewById(R.id.text_nik);
        txtNama = findViewById(R.id.text_nama);
        txtTanggal = findViewById(R.id.text_tanggal_lahir);
        txtJenisKelamin = findViewById(R.id.text_jenis_kelamin);
        txtAlamat = findViewById(R.id.text_alamat);
        txtProvinsi = findViewById(R.id.text_provinsi);
        txtKota = findViewById(R.id.text_kota);
        txtTelepon = findViewById(R.id.text_telepon);
        txtEmail = findViewById(R.id.text_email);
    }

    private boolean validateTextInput(TextInputLayout textInputLayout, String errorMessage) {
        String input = textInputLayout.getEditText().getText().toString().trim();

        if (input.isEmpty()) {
            textInputLayout.setError(errorMessage);
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    //    konversi gambar menjadi string
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateProfil() {
        progressDialog.setMessage("Sedang Memperbarui Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.USER_PROFILE;

        StringRequest updateRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    Snackbar.make(findViewById(R.id.activity_edit_profil), message, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String message = "Terjadi error. Coba beberapa saat lagi.";

                if (error instanceof NetworkError) {
                    message = "Tidak dapat terhubung ke internet. Harap periksa koneksi anda.";
                } else if (error instanceof AuthFailureError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError) {
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError) {
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Snackbar.make(findViewById(R.id.activity_edit_profil), message, Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (checkBox.isChecked()) {
//                    parameter dengan mengubah foto
                    params.put("id_pengguna", mIdPengguna);
                    params.put("nama_pengguna", txtNama.getEditText().getText().toString().trim());
                    params.put("alamat_pengguna", txtAlamat.getEditText().getText().toString().trim());
                    params.put("provinsi_pengguna", txtProvinsi.getEditText().getText().toString().trim());
                    params.put("kota_pengguna", txtKota.getEditText().getText().toString().trim());
                    params.put("telepon_pengguna", txtTelepon.getEditText().getText().toString().trim());
                    params.put("email_pengguna", txtEmail.getEditText().getText().toString().trim());
                    params.put("kelamin_pengguna", txtJenisKelamin.getEditText().getText().toString().trim());
                    params.put("tanggal_lahir_pengguna", txtTanggal.getEditText().getText().toString().trim());
                    params.put("no_ktp_pengguna", txtNik.getEditText().getText().toString().trim());
                    params.put("no_ktp_pengguna", txtNik.getEditText().getText().toString().trim());
                    params.put("foto_pengguna", imageToString(bitmapProfil));

                    return params;
                } else {
//                    parameter tanpa mengubah foto
                    params.put("id_pengguna", mIdPengguna);
                    params.put("nama_pengguna", txtNama.getEditText().getText().toString().trim());
                    params.put("alamat_pengguna", txtAlamat.getEditText().getText().toString().trim());
                    params.put("provinsi_pengguna", txtProvinsi.getEditText().getText().toString().trim());
                    params.put("kota_pengguna", txtKota.getEditText().getText().toString().trim());
                    params.put("telepon_pengguna", txtTelepon.getEditText().getText().toString().trim());
                    params.put("email_pengguna", txtEmail.getEditText().getText().toString().trim());
                    params.put("kelamin_pengguna", txtJenisKelamin.getEditText().getText().toString().trim());
                    params.put("tanggal_lahir_pengguna", txtTanggal.getEditText().getText().toString().trim());
                    params.put("no_ktp_pengguna", txtNik.getEditText().getText().toString().trim());
                    params.put("no_ktp_pengguna", txtNik.getEditText().getText().toString().trim());

                    return params;
                }
            }
        };

        queue.add(updateRequest);
    }

    private void loadProfil() {
        progressDialog.setMessage("Sedang Memperbarui Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.USER + mIdPengguna;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("false")) {
                        String message = jsonObject.getString("message");
                        Snackbar.make(findViewById(R.id.activity_edit_profil), message, Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject dataUser = jsonObject.getJSONObject("user");

                        mNik = dataUser.getString("no_ktp_pengguna");
                        mNama = dataUser.getString("nama_pengguna");
                        mTanggal = dataUser.getString("tanggal_lahir_pengguna");
                        mJenisKelamin = dataUser.getString("kelamin_pengguna");
                        mAlamat = dataUser.getString("alamat_pengguna");
                        mProvinsi = dataUser.getString("provinsi_pengguna");
                        mKota = dataUser.getString("kota_pengguna");
                        mTelepon = dataUser.getString("telepon_pengguna");
                        mEmail = dataUser.getString("email_pengguna");
                        mFoto = dataUser.getString("foto_pengguna");

                        txtNik.getEditText().setText(mNik);
                        txtNama.getEditText().setText(mNama);
                        txtTanggal.getEditText().setText(mTanggal);
                        txtJenisKelamin.getEditText().setText(mJenisKelamin);
                        txtAlamat.getEditText().setText(mAlamat);
                        txtProvinsi.getEditText().setText(mProvinsi);
                        txtKota.getEditText().setText(mKota);
                        txtTelepon.getEditText().setText(mTelepon);
                        txtEmail.getEditText().setText(mEmail);

                        btnUpdateProfil.setEnabled(true);
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.activity_edit_profil), e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String message = "Terjadi error. Coba beberapa saat lagi.";

                if (error instanceof NetworkError) {
                    message = "Tidak dapat terhubung ke internet. Harap periksa koneksi anda.";
                } else if (error instanceof AuthFailureError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError) {
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError) {
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Snackbar.make(findViewById(R.id.activity_edit_profil), message, Snackbar.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
//            mengambil alamat file gambar
            Uri path = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                String pathGambar = path.getPath();
                bitmapProfil = BitmapFactory.decodeStream(inputStream);

                txtPathGambar.setText(pathGambar);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.success));

                statusImage = true;

//                mengaktifkan button bayar
//                btnBayar.setEnabled(true);
//                btnBayar.setBackgroundResource(R.drawable.button_login);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
