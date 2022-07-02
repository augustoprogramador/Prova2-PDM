package com.example.prova2.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.prova2.Home;
import com.example.prova2.MainActivity;
import com.example.prova2.R;
import com.example.prova2.bd.BDUsuario;
import com.example.prova2.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EdicaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdicaoFragment extends Fragment {

    BDUsuario bdUsuario;
    String idUsuario;
    Usuario u;

    public EdicaoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        idUsuario = ((Home) getActivity()).getUserId();




        return inflater.inflate(R.layout.fragment_edicao_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (idUsuario != null && !idUsuario.equals("")) {
            bdUsuario = BDUsuario.getInstance(getContext());
            u = bdUsuario.findByID(idUsuario);

            EditText txt_nome_home = view.findViewById(R.id.txt_nome_edicao);
            EditText txt
            txt_nome_home.setText(u.getNome());

        }

    }
}