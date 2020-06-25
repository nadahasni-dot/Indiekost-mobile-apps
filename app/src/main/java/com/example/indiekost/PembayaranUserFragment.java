package com.example.indiekost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.indiekost.activity.MainActivity;
import com.example.indiekost.api.ApiUrl;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PembayaranUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PembayaranUserFragment extends Fragment {
    private static final String ID_PENGGUNA = "ID_PENGGUNA";
    private static final String BATAS_BAYAR = "BATAS_BAYAR";
    private static final String ALERT = "ALERT";
    private static final String NOMOR_KAMAR = "NOMOR_KAMAR";
    private static final String NAMA = "NAMA";
    private static final String DENDA = "DENDA";
    private static final String HARGA_TOTAL = "HARGA_TOTAL";
    private static final String TANGGAL_BAYAR = "TANGGAL_BAYAR";
    private final int IMG_REQUEST = 1;

    private String mId;
    private String mBatasBayar;
    private String mAlert;
    private String mNomorKamar;
    private String mNama;
    private String mDenda;
    private String mHargaTotal;
    private String mTanggalBayar;
    private String mBitmapName;
    private String mKeterangan;

    TextView batasBayar, alert, nomorKamar, nama, denda, hargaTotal, tanggalBayar, buktiBayar;
    TextInputLayout txtKeteranganBayar;
    Button btnBayar, btnGambar;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    CardView cardView;

    RequestQueue queue;

    public PembayaranUserFragment() {
        // Required empty public constructor
    }

    public static PembayaranUserFragment newInstance(String id, String batas, String alert, String nomorKamar, String nama, String denda, String hargaTotal, String tangalBayar) {
        PembayaranUserFragment fragment = new PembayaranUserFragment();
        Bundle args = new Bundle();
        args.putString(ID_PENGGUNA, id);
        args.putString(BATAS_BAYAR, batas);
        args.putString(ALERT, alert);
        args.putString(NOMOR_KAMAR, nomorKamar);
        args.putString(NAMA, nama);
        args.putString(DENDA, denda);
        args.putString(HARGA_TOTAL, hargaTotal);
        args.putString(TANGGAL_BAYAR, tangalBayar);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ID_PENGGUNA);
            mBatasBayar = getArguments().getString(BATAS_BAYAR);
            mAlert = getArguments().getString(ALERT);
            mNomorKamar = getArguments().getString(NOMOR_KAMAR);
            mNama = getArguments().getString(NAMA);
            mDenda = getArguments().getString(DENDA);
            mHargaTotal = getArguments().getString(HARGA_TOTAL);
            mTanggalBayar = getArguments().getString(TANGGAL_BAYAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pembayaran_user, container, false);

        batasBayar = v.findViewById(R.id.textView23);
        alert = v.findViewById(R.id.text_alert_pembayaran);
        nomorKamar = v.findViewById(R.id.txt_pembayaran_no_kamar);
        nama = v.findViewById(R.id.txt_nama_pembayar);
        denda = v.findViewById(R.id.txt_pembayaran_denda);
        hargaTotal = v.findViewById(R.id.txt_total_harus_bayar);
        tanggalBayar = v.findViewById(R.id.txt_tanggal_pembayaran);
        buktiBayar = v.findViewById(R.id.text_bukti_bayar);
        cardView = v.findViewById(R.id.cardView4);

        txtKeteranganBayar = v.findViewById(R.id.text_keterangan_bayar);

        btnBayar = v.findViewById(R.id.button_bayar_kamar);
        btnGambar = v.findViewById(R.id.btn_bukti_bayar);

        batasBayar.setText(mBatasBayar);
        alert.setText(mAlert);
        nomorKamar.setText(mNomorKamar);
        nama.setText(mNama);
        denda.setText(mDenda);
        hargaTotal.setText(mHargaTotal);
        tanggalBayar.setText(mTanggalBayar);

        //        membuat new request queue
        queue = Volley.newRequestQueue(getActivity());

//        membuat new progress dialog
        progressDialog = new ProgressDialog(getActivity());

        mBitmapName = buktiBayar.getText().toString();

//        jika gambar belum diupload
        if (mBitmapName.equals("tidak ada gambar terpilih")) {
            btnBayar.setEnabled(false);
            btnBayar.setBackgroundResource(R.drawable.button_disabled);
        }

//        button onclick
        btnGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    uploadPembayaran();
                    Toast.makeText(getActivity(), "Telah memenuhi syarat", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Belum memenuhi syarat", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    //    fungsi untuk memilih gambar dari galery
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    //    konversi gambar menjadi string
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //    fungsi validasi input pembayaran
    private boolean validateInput() {
        String keterangan = txtKeteranganBayar.getEditText().getText().toString().trim();

        if (keterangan.isEmpty()) {
            txtKeteranganBayar.setError("Keterangan Tidak Boleh Kosong!");
            return false;
        } else {
            txtKeteranganBayar.setError(null);
            txtKeteranganBayar.setErrorEnabled(false);
            return true;
        }
    }

    //    fungsi untuk volley upload image
    private void uploadPembayaran() {
        progressDialog.setMessage("Memproses Pembayaran...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PEMBAYARAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

//                    restart activity
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", mId);
                params.put("tanggal_pembayaran", mTanggalBayar);
                params.put("nilai_pembayaran", mHargaTotal);
                params.put("bukti_pembayaran", imageToString(bitmap));
                params.put("keterangan", txtKeteranganBayar.getEditText().getText().toString().trim());
                params.put("id_status", "2");

                return params;
            }
        };

        queue.add(stringRequest);
    }

    //    untuk menangkap gambar yang dipilih dari galery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
//            mengambil alamat file gambar
            Uri path = data.getData();

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(path);
                mBitmapName = path.getPath();
                bitmap = BitmapFactory.decodeStream(inputStream);

                buktiBayar.setText(mBitmapName);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.success));

//                mengaktifkan button bayar
                btnBayar.setEnabled(true);
                btnBayar.setBackgroundResource(R.drawable.button_login);
            } catch (FileNotFoundException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
