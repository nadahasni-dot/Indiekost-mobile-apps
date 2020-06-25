package com.example.indiekost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.indiekost.api.ApiUrl;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PembayaranDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PembayaranDetailFragment extends Fragment {
    private static final String ID_PEMBAYARAN = "ID_PEMBAYARAN";
    private static final String BATAS_BAYAR = "BATAS_BAYAR";
    private static final String ALERT = "ALERT";
    private static final String TANGGAL_BAYAR = "TANGGAL_BAYAR";
    private static final String NOMOR_KAMAR = "NOMOR_KAMAR";
    private static final String NAMA = "NAMA";
    private static final String KETERANGAN = "KETERANGAN";
    private static final String NOMINAL = "NOMINAL";
    private static final String STATUS = "STATUS";

    private String mIdPembayaran, mBatasBayar, mAlert, mTanggalBayar, mNomorKamar, mNama, mKeterangan, mNominal, mStatus, urlBukti;

    TextView batasBayar, alert, tanggalBayar, nomorKamar, nama, keterangan, nominal, status;

    Button cetakPembayaran;

    public PembayaranDetailFragment() {
        // Required empty public constructor
    }

    public static PembayaranDetailFragment newInstance(String id, String batas, String alert, String tanggal, String nomor, String nama, String keterangan, String nominal, String status) {
        PembayaranDetailFragment fragment = new PembayaranDetailFragment();
        Bundle args = new Bundle();
        args.putString(ID_PEMBAYARAN, id);
        args.putString(BATAS_BAYAR, batas);
        args.putString(ALERT, alert);
        args.putString(TANGGAL_BAYAR, tanggal);
        args.putString(NOMOR_KAMAR, nomor);
        args.putString(NAMA, nama);
        args.putString(KETERANGAN, keterangan);
        args.putString(NOMINAL, nominal);
        args.putString(STATUS, status);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdPembayaran = getArguments().getString(ID_PEMBAYARAN);
            mBatasBayar = getArguments().getString(BATAS_BAYAR);
            mAlert = getArguments().getString(ALERT);
            mTanggalBayar = getArguments().getString(TANGGAL_BAYAR);
            mNomorKamar = getArguments().getString(NOMOR_KAMAR);
            mNama = getArguments().getString(NAMA);
            mKeterangan = getArguments().getString(KETERANGAN);
            mNominal = getArguments().getString(NOMINAL);
            mStatus = getArguments().getString(STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pembayaran_detail, container, false);

        urlBukti = mIdPembayaran;

        batasBayar = v.findViewById(R.id.textView23);
        alert = v.findViewById(R.id.txt_alert_pembayaran);
        tanggalBayar = v.findViewById(R.id.txt_detail_tanggal_pembayaran);
        nomorKamar = v.findViewById(R.id.txt_detail_no_kamar);
        nama = v.findViewById(R.id.txt_detail_nama_pembayar);
        keterangan = v.findViewById(R.id.txt_detail_keterangan_pembayaran);
        nominal = v.findViewById(R.id.txt_detail_nominal_pembayaran);
        status = v.findViewById(R.id.txt_detail_status_pembayaran);

        cetakPembayaran = v.findViewById(R.id.button_cetak_pembayaran);

        if (mStatus.equals("BELUM DIKONFIRMASI")) {
//            cetakPembayaran.setBackgroundResource(R.drawable.button_disabled);
            cetakPembayaran.setBackgroundColor(getResources().getColor(R.color.colorLight));
            cetakPembayaran.setEnabled(false);
        }

        batasBayar.setText(mBatasBayar);
        alert.setText(mAlert);
        tanggalBayar.setText(mTanggalBayar);
        nomorKamar.setText(mNomorKamar);
        nama.setText(mNama);
        keterangan.setText(mKeterangan);
        nominal.setText(mNominal);
        status.setText(mStatus);

        cetakPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(ApiUrl.BUKTI_PEMBAYARAN+mIdPembayaran+"?id_akses=2"));
                startActivity(browserIntent);
            }
        });

        return v;
    }
}
