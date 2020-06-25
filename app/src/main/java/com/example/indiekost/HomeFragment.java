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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // key parameter arguments
    private static final String FOTO_PENGGUNA = "FOTO_PENGGUNA";
    private static final String NAMA_PENGGUNA = "NAMA_PENGGUNA";
    private static final String NO_KAMAR = "NO_KAMAR";
    private static final String NIK = "NIK";
    private static final String JENIS_KELAMIN = "JENIS_KELAMIN";
    private static final String TANGGAL_LAHIR = "TANGGAL_LAHIR";
    private static final String ALAMAT = "ALAMAT";
    private static final String PROVINSI = "PROVINSI";
    private static final String KOTA = "KOTA";
    private static final String EMAIL = "EMAIL";
    private static final String TELEPON = "TELEPON";

    //parameter
    private String mFotoPengguna;
    private String mNama;
    private String mNomorKamar;
    private String mNik;
    private String mJenisKelamin;
    private String mTanggalLahir;
    private String mAlamat;
    private String mProvinsi;
    private String mKota;
    private String mEmail;
    private String mTelepon;

    ImageView fotoProfil;
    TextView namaPengguna;
    TextView noKamar;
    TextView nik;
    TextView jenisKelamin;
    TextView tanggalLahir;
    TextView alamat;
    TextView provinsi;
    TextView kota;
    TextView email;
    TextView telepon;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String foto, String nama, String no_kamar, String nik, String jenis_kelamin, String tanggal_lahir, String alamat, String provinsi, String kota, String email, String telepon) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(FOTO_PENGGUNA, foto);
        args.putString(NAMA_PENGGUNA, nama);
        args.putString(NO_KAMAR, no_kamar);
        args.putString(NIK, nik);
        args.putString(JENIS_KELAMIN, jenis_kelamin);
        args.putString(TANGGAL_LAHIR, tanggal_lahir);
        args.putString(ALAMAT, alamat);
        args.putString(PROVINSI, provinsi);
        args.putString(KOTA, kota);
        args.putString(EMAIL, email);
        args.putString(TELEPON, telepon);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFotoPengguna = "http://indiekost.mif-project.com/assets/img/" + getArguments().getString(FOTO_PENGGUNA);
            mNama = getArguments().getString(NAMA_PENGGUNA);
            mNomorKamar = "Kamar no. "+getArguments().getString(NO_KAMAR);
            mNik = getArguments().getString(NIK);
            mJenisKelamin = getArguments().getString(JENIS_KELAMIN);
            mTanggalLahir = getArguments().getString(TANGGAL_LAHIR);
            mAlamat = getArguments().getString(ALAMAT);
            mProvinsi = getArguments().getString(PROVINSI);
            mKota = getArguments().getString(KOTA);
            mEmail = getArguments().getString(EMAIL);
            mTelepon = getArguments().getString(TELEPON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

//        Toast.makeText(getActivity(), mFotoPengguna+mNama+mTelepon, Toast.LENGTH_LONG).show();

        fotoProfil = v.findViewById(R.id.image_profil);
        namaPengguna = v.findViewById(R.id.text_nama_pengguna);
        noKamar = v.findViewById(R.id.text_no_kamar);
        nik = v.findViewById(R.id.text_nik);
        jenisKelamin = v.findViewById(R.id.text_jk);
        tanggalLahir = v.findViewById(R.id.text_tanggal_lahir);
        alamat = v.findViewById(R.id.text_alamat);
        provinsi = v.findViewById(R.id.text_provinsi);
        kota = v.findViewById(R.id.text_kota);
        email = v.findViewById(R.id.text_email);
        telepon = v.findViewById(R.id.text_telepon);

//        set widget value
        Picasso.get().load(mFotoPengguna).into(fotoProfil);
        namaPengguna.setText(mNama);
        noKamar.setText(mNomorKamar);
        nik.setText(mNik);
        jenisKelamin.setText(mJenisKelamin);
        tanggalLahir.setText(mTanggalLahir);
        alamat.setText(mAlamat);
        provinsi.setText(mProvinsi);
        kota.setText(mKota);
        email.setText(mEmail);
        telepon.setText(mTelepon);

        return v;
    }
}
