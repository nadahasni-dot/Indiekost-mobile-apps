package com.example.indiekost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KamarUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KamarUserFragment extends Fragment {
    // Key parameter arguments
    private static final String FOTO_KAMAR = "FOTO_KAMAR";
    private static final String NO_KAMAR = "NO_KAMAR";
    private static final String JENIS_KAMAR = "JENIS_KAMAR";
    private static final String DESKRIPSI = "DESKRIPSI";
    private static final String TANGGAL_MASUK = "TANGGAL_MASUK";
    private static final String LANTAI = "LANTAI";
    private static final String KAPASITAS = "KAPASITAS";
    private static final String LUAS = "LUAS";
    private static final String HARGA_BULANAN = "HARGA_BULANAN";
    private static final String HARGA_LAYANAN = "HARGA_LAYANAN";
    private static final String HARGA_TOTAL = "HARGA_TOTAL";
    private static final String DENDA = "DENDA";

    //parameter
    private String mFotoKamar;
    private String mNoKamar;
    private String mJenisKamar;
    private String mDeskripsi;
    private String mTanggalMasuk;
    private String mLantai;
    private String mKapasitas;
    private String mLuas;
    private String mHargaBulanan;
    private String mHargaLayanan;
    private String mHargaTotal;
    private String mDenda;

    ImageView foto_kamar;
    TextView no_kamar, jenis_kamar, deskripsi_kamar, tanggal_masuk, lantai, kapasitas, luas, harga_bulanan, harga_layanan, harga_total, denda;

    public KamarUserFragment() {
        // Required empty public constructor
    }

    public static KamarUserFragment newInstance(String foto, String no, String jenis, String desc, String tanggal, String lantai, String kapasitas, String luas, String hargaBulanan, String hargaLayanan, String hargaTotal, String denda) {
        KamarUserFragment fragment = new KamarUserFragment();
        Bundle args = new Bundle();
        args.putString(FOTO_KAMAR, foto);
        args.putString(NO_KAMAR, no);
        args.putString(JENIS_KAMAR, jenis);
        args.putString(DESKRIPSI, desc);
        args.putString(TANGGAL_MASUK, tanggal);
        args.putString(LANTAI, lantai);
        args.putString(KAPASITAS, kapasitas);
        args.putString(LUAS, luas);
        args.putString(HARGA_BULANAN, hargaBulanan);
        args.putString(HARGA_LAYANAN, hargaLayanan);
        args.putString(HARGA_TOTAL, hargaTotal);
        args.putString(DENDA, denda);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFotoKamar = "http://indiekost.mif-project.com/assets/img/" + getArguments().getString(FOTO_KAMAR);
            mNoKamar = getArguments().getString(NO_KAMAR);
            mJenisKamar = getArguments().getString(JENIS_KAMAR);
            mDeskripsi = getArguments().getString(DESKRIPSI);
            mTanggalMasuk = getArguments().getString(TANGGAL_MASUK);
            mLantai = getArguments().getString(LANTAI);
            mKapasitas = getArguments().getString(KAPASITAS);
            mLuas = getArguments().getString(LUAS);
            mHargaBulanan = getArguments().getString(HARGA_BULANAN);
            mHargaLayanan = getArguments().getString(HARGA_LAYANAN);
            mHargaTotal = getArguments().getString(HARGA_TOTAL);
            mDenda = getArguments().getString(DENDA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_kamar_user, container, false);

        foto_kamar = v.findViewById(R.id.imageView2);
        no_kamar = v.findViewById(R.id.text_no_kamar);
        jenis_kamar = v.findViewById(R.id.text_jenis_kamar);
        deskripsi_kamar = v.findViewById(R.id.textView13);
        tanggal_masuk = v.findViewById(R.id.txt_tanggal_masuk);
        lantai = v.findViewById(R.id.txt_letak_lantai);
        kapasitas = v.findViewById(R.id.txt_kapasitas);
        luas = v.findViewById(R.id.txt_luas_kamar);
        harga_bulanan = v.findViewById(R.id.txt_harga_bulanan);
        harga_layanan = v.findViewById(R.id.txt_harga_layanan);
        harga_total = v.findViewById(R.id.txt_harga_total);
        denda = v.findViewById(R.id.txt_denda);

//        set data
        Picasso.get().load(mFotoKamar).into(foto_kamar);
        no_kamar.setText(mNoKamar);
        jenis_kamar.setText(mJenisKamar);
        deskripsi_kamar.setText(mDeskripsi);
        tanggal_masuk.setText(mTanggalMasuk);
        lantai.setText(mLantai);
        kapasitas.setText(mKapasitas);
        luas.setText(mLuas);
        harga_bulanan.setText(mHargaBulanan);
        harga_layanan.setText(mHargaLayanan);
        harga_total.setText(mHargaTotal);
        denda.setText(mDenda);

        return v;
    }
}
