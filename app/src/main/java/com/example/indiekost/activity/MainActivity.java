
package com.example.indiekost.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.indiekost.KamarUserFragment;
import com.example.indiekost.LainnyaUserFragment;
import com.example.indiekost.PembayaranDetailFragment;
import com.example.indiekost.PembayaranUserFragment;
import com.example.indiekost.R;
import com.example.indiekost.api.ApiUrl;
import com.example.indiekost.helper.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;
    BottomNavigationView bottomNavigationView;
    ProgressDialog progressDialog;

    private RequestQueue queue;

    String email, nama, id, id_akses;

    //parameter fragment home
    String mFotoPengguna;
    String mNama;
    String mNomorKamar;
    String mNik;
    String mJenisKelamin;
    String mTanggalLahir;
    String mAlamat;
    String mProvinsi;
    String mKota;
    String mEmail;
    String mTelepon;


//    parameter fragment kamar
    String mFotoKamar, mJenisKamar, mDeskripsiKamar, mTanggalMasuk, mLantai, mKapasitas, mLuas, mHargaBulanan, mHargaLayanan, mHargaTotal, mDendaKamar;

//    parameter fragment lainnya
    String mFotoKost, mNamaKost, mJenisKost, mDeskripsiKost;

//    parameter fragment pembayaran
    String mIdPembayaran, mBatasBayar, mAlertBayar, mTanggalBayar, mStatusBayar, mKeteranganBayar, mNominalPembayaran;

    boolean doubleBackToExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav_user);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //        membuat new request queue
        queue = Volley.newRequestQueue(this);

        //        instance session manager
        sessionManager = new SessionManager(this);

        //        Instance progress dialog
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();

        if (intent.hasExtra("EMAIL")) {
            email = getIntent().getStringExtra("EMAIL");
            nama = getIntent().getStringExtra("NAMA");
            id = getIntent().getStringExtra("ID");
            id_akses = getIntent().getStringExtra("ID_AKSES");

            if (id_akses.equals("2")) {
                _loadHome();
            } else {
                Toast.makeText(this, "Hanya Penghuni Yang dapat mengakses aplikasi", Toast.LENGTH_LONG).show();
                sessionManager.logout();
            }
        } else {
            id_akses = sessionManager.getIdAkses();
            id = sessionManager.getIdPengguna();

            if (id_akses.equals("2")) {
                _loadHome();
            } else {
                Toast.makeText(this, "Hanya Penghuni Yang dapat mengakses aplikasi", Toast.LENGTH_LONG).show();
                sessionManager.logout();
            }
        }
    }

//    fungsi get data home fragment
    private void _loadHome() {
        progressDialog.setMessage("Sedang Memperbarui Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.USER+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status == "false") {
                        Snackbar.make(findViewById(R.id.main_activity), "Terjadi Kesalahan", Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject dataUser = jsonObject.getJSONObject("user");
                        JSONObject dataKamar = jsonObject.getJSONObject("kamar");


                        mFotoPengguna = dataUser.getString("foto_pengguna");
                        mNama = dataUser.getString("nama_pengguna");
                        mNomorKamar = dataKamar.getString("nomor_kamar");
                        mNik = dataUser.getString("no_ktp_pengguna");
                        mJenisKelamin = dataUser.getString("kelamin_pengguna");
                        mTanggalLahir = dataUser.getString("tanggal_lahir_pengguna");
                        mAlamat = dataUser.getString("alamat_pengguna");
                        mProvinsi = dataUser.getString("provinsi_pengguna");
                        mKota = dataUser.getString("kota_pengguna");
                        mEmail = dataUser.getString("email_pengguna");
                        mTelepon = dataUser.getString("telepon_pengguna");

                        Fragment fragment = HomeFragment.newInstance(mFotoPengguna, mNama, mNomorKamar, mNik, mJenisKelamin, mTanggalLahir, mAlamat, mProvinsi, mKota, mEmail, mTelepon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.main_activity), e.toString(), Snackbar.LENGTH_LONG).show();
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
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError){
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError){
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Fragment errorFragment = new ErrorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, errorFragment).commit();

                Snackbar.make(findViewById(R.id.main_activity), message, Snackbar.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

//    fungsi get kamar gragment
    private void _loadKamar() {
        progressDialog.setMessage("Sedang Memperbarui Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.USER+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status == "false") {
                        Snackbar.make(findViewById(R.id.main_activity), "Terjadi Kesalahan", Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject dataKamar = jsonObject.getJSONObject("kamar");

                        mFotoKamar = dataKamar.getString("foto_kamar");
                        mNomorKamar = dataKamar.getString("nomor_kamar");
                        mJenisKamar = dataKamar.getString("nama_tipe");
                        mDeskripsiKamar = dataKamar.getString("deskripsi_kamar");
                        mTanggalMasuk = dataKamar.getString("tanggal_masuk");
                        mLantai = dataKamar.getString("lantai_kamar");
                        mKapasitas = dataKamar.getString("kapasitas_kamar");
                        mLuas = dataKamar.getString("luas_kamar");
                        mHargaBulanan = dataKamar.getString("harga_kamar_bulanan");
                        mHargaLayanan = dataKamar.getString("harga_layanan")+" ("+dataKamar.getString("nama_layanan")+")";
                        mHargaTotal = dataKamar.getString("total_harga");
                        mDendaKamar = dataKamar.getString("denda");

                        Fragment fragment = KamarUserFragment.newInstance(mFotoKamar, mNomorKamar, mJenisKamar, mDeskripsiKamar, mTanggalMasuk, mLantai, mKapasitas, mLuas, mHargaBulanan, mHargaLayanan, mHargaTotal, mDendaKamar);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.main_activity), e.toString(), Snackbar.LENGTH_LONG).show();
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
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError){
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError){
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Fragment errorFragment = new ErrorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, errorFragment).commit();

                Snackbar.make(findViewById(R.id.main_activity), message, Snackbar.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    //    fungsi get lainnya fragment
    private void _loadInfo() {
        progressDialog.setMessage("Sedang Memperbarui Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.USER+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status == "false") {
                        Snackbar.make(findViewById(R.id.main_activity), "Terjadi Kesalahan", Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject infoKost = jsonObject.getJSONObject("info_kost");
                        mFotoKost = infoKost.getString("foto_kost");
                        mNamaKost = infoKost.getString("nama_kost");
                        mJenisKost = infoKost.getString("jenis_kost");
                        mDeskripsiKost = infoKost.getString("deskripsi_kost");

                        Fragment fragment = LainnyaUserFragment.newInstance(mFotoKost, mNamaKost, mJenisKost, mDeskripsiKost);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.main_activity), e.toString(), Snackbar.LENGTH_LONG).show();
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
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError){
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError){
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Fragment errorFragment = new ErrorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, errorFragment).commit();

                Snackbar.make(findViewById(R.id.main_activity), message, Snackbar.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


    //    fungsi get pembayaran fragment
    private void _loadPembayaran() {
        progressDialog.setMessage("Sedang Memperbarui Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiUrl.USER+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status == "false") {
                        Snackbar.make(findViewById(R.id.main_activity), "Terjadi Kesalahan", Snackbar.LENGTH_LONG).show();
                    } else {
                        JSONObject pembayaran = jsonObject.getJSONObject("pembayaran");
                        JSONObject hargaKamar = jsonObject.getJSONObject("harga_kamar");

                        mIdPembayaran = pembayaran.getString("id_pembayaran");

                        if (mIdPembayaran.equals("null")){
                            id = hargaKamar.getString("id_pengguna");
                            mBatasBayar = "Anda belum melakukan pembayaran kamar no. "+hargaKamar.getString("nomor_kamar")+" bulan "+hargaKamar.getString("bulan");
                            mAlertBayar = "Segera lakukan pembayaran sebelum tanggal 10 "+hargaKamar.getString("bulan");
                            mNomorKamar = hargaKamar.getString("nomor_kamar");
                            mNama = hargaKamar.getString("nama_pengguna");
                            mDendaKamar = jsonObject.getString("denda");

                            int denda = Integer.parseInt(jsonObject.getString("denda"));
                            int harga = Integer.parseInt(hargaKamar.getString("harga_total"));
                            int total = denda + harga;

//                            mHargaTotal = hargaKamar.getString("harga_total");
                            mHargaTotal = String.valueOf(total);
                            mTanggalBayar = hargaKamar.getString("tanggal_sekarang");

                            Fragment fragment = PembayaranUserFragment.newInstance(id, mBatasBayar, mAlertBayar, mNomorKamar, mNama, mDendaKamar, mHargaTotal, mTanggalBayar);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
                        } else {
                            mBatasBayar = "Anda telah melakukan pembayaran kamar no. "+hargaKamar.getString("nomor_kamar")+" bulan "+hargaKamar.getString("bulan");
                            mStatusBayar = pembayaran.getString("nama_status_pembayaran");

                            if (mStatusBayar.equals("belum dikonfirmasi")){
                                mAlertBayar = "Harap menunggu konfirmasi pembayaran dari admin";
                            } else {
                                mAlertBayar = "Pembayaran telah dikonfirmasi, anda bisa mencetak bukti pembayaran";
                            }


                            mTanggalBayar = pembayaran.getString("tanggal_pembayaran");
                            mNomorKamar = pembayaran.getString("nomor_kamar");
                            mNama = pembayaran.getString("nama_pengguna");
                            mKeteranganBayar = pembayaran.getString("keterangan");
                            mNominalPembayaran = pembayaran.getString("nilai_pembayaran");
                            mStatusBayar = mStatusBayar.toUpperCase();

                            Fragment fragment = PembayaranDetailFragment.newInstance(mIdPembayaran, mBatasBayar, mAlertBayar, mTanggalBayar, mNomorKamar, mNama, mKeteranganBayar, mNominalPembayaran, mStatusBayar);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
                        }
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.main_activity), e.toString(), Snackbar.LENGTH_LONG).show();
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
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof ClientError) {
                    message = "Gagal login. Harap periksa email dan password anda.";
                } else if (error instanceof NoConnectionError){
                    message = "Tidak ada koneksi internet. Harap periksa koneksi anda.";
                } else if (error instanceof TimeoutError){
                    message = "Connection Time Out. Harap periksa koneksi anda.";
                }

                Fragment errorFragment = new ErrorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, errorFragment).commit();

                Snackbar.make(findViewById(R.id.main_activity), message, Snackbar.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExit = true;
        Snackbar.make(findViewById(R.id.main_activity), "Tekan kembali sekali lagi untuk keluar", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit=false;
            }
        }, 2000);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;

                    switch (item.getItemId()) {
                        case R.id.home_user:
                            _loadHome();
                            break;
                        case R.id.kamar_user:
                            _loadKamar();
                            break;
                        case R.id.pembayaran_user:
                            _loadPembayaran();
                            break;
                        case R.id.more_user:
                            _loadInfo();
                            break;
                    }

                    return true;
                }
            };
}
