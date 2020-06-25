package com.example.indiekost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indiekost.activity.EditProfilActivity;
import com.example.indiekost.activity.MainActivity;
import com.example.indiekost.activity.RubahPasswordActivity;
import com.example.indiekost.helper.SessionManager;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LainnyaUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LainnyaUserFragment extends Fragment {
    Button logout_button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FOTO_KOST = "FOTO_KOST";
    private static final String NAMA_KOST = "NAMA_KOST";
    private static final String JENIS_KOST = "JENIS_KOST";
    private static final String DESKRIPSI_KOST = "DESKRIPSI_KOST";

    // TODO: Rename and change types of parameters
    private String mFotoKost;
    private String mNamaKost;
    private String mJenisKost;
    private String mDeskripsiKost;

    ImageView fotoKost;
    TextView namaKost, jenisKost, deskripsiKost;
    Button btnRubahPassword, btnEditProfil;

    public LainnyaUserFragment() {
        // Required empty public constructor
    }

    public static LainnyaUserFragment newInstance(String foto, String nama, String jenis, String deskripsi) {
        LainnyaUserFragment fragment = new LainnyaUserFragment();
        Bundle args = new Bundle();
        args.putString(FOTO_KOST, foto);
        args.putString(NAMA_KOST, nama);
        args.putString(JENIS_KOST, jenis);
        args.putString(DESKRIPSI_KOST, deskripsi);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFotoKost = "http://indiekost.mif-project.com/assets/img/" + getArguments().getString(FOTO_KOST);
            mNamaKost = getArguments().getString(NAMA_KOST);
            mJenisKost = getArguments().getString(JENIS_KOST);
            mDeskripsiKost = getArguments().getString(DESKRIPSI_KOST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lainnya_user, container, false);
        logout_button = rootView.findViewById(R.id.logout_btn);

        fotoKost = rootView.findViewById(R.id.imageView4);
        namaKost = rootView.findViewById(R.id.text_nama_kost);
        jenisKost = rootView.findViewById(R.id.text_jenis_kost);
        deskripsiKost = rootView.findViewById(R.id.text_deskripsi_kost);
        btnEditProfil = rootView.findViewById(R.id.btn_edit_profil);
        btnRubahPassword = rootView.findViewById(R.id.btn_rubah_password);

        Picasso.get().load(mFotoKost).into(fotoKost);
        namaKost.setText(mNamaKost);
        jenisKost.setText(mJenisKost);
        deskripsiKost.setText(mDeskripsiKost);


        //  onclick listener button
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getActivity());
                sessionManager.logout();
            }
        });

        btnRubahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rubahPassword = new Intent(getActivity(), RubahPasswordActivity.class);
                startActivity(rubahPassword);
            }
        });

        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfil = new Intent(getActivity(), EditProfilActivity.class);
                startActivity(editProfil);
            }
        });

        return rootView;
    }
}
