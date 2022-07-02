package com.example.prova2.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prova2.R;
import com.example.prova2.bd.BDUsuario;
import com.example.prova2.model.Usuario;


public class RegisterFragment extends Fragment{
    Button btn_registrar;
    EditText txt_nome;
    EditText txt_email;
    EditText txt_nome_usuario;
    EditText txt_login_senha;
    EditText txt_confirmar_senha;

    BDUsuario db;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_nome = view.findViewById(R.id.txt_nome);
        txt_email = view.findViewById(R.id.txt_email);
        txt_nome_usuario = view.findViewById(R.id.txt_nome_usuario);
        txt_login_senha = view.findViewById(R.id.txt_login_senha);
        txt_confirmar_senha = view.findViewById(R.id.txt_confirmar_senha);

        db = BDUsuario.getInstance(view.getContext());
//        db.removeAll();

        btn_registrar = view.findViewById(R.id.btn_registrar);
        btn_registrar.setOnClickListener(view1 -> {
            String nome = txt_nome.getText().toString();
            String email = txt_email.getText().toString();
            String usuario = txt_nome_usuario.getText().toString();
            String senha = txt_login_senha.getText().toString();
            String confirmar_senha = txt_confirmar_senha.getText().toString();

            if(nome.equals("") || email.equals("") || usuario.equals("") || senha.equals("") || confirmar_senha.equals("")){
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if(senha.equals(confirmar_senha)){
                    Usuario newUsuario = new Usuario(
                            "0",
                            txt_nome.getText().toString(),
                            txt_email.getText().toString(),
                            txt_nome_usuario.getText().toString(),
                            txt_login_senha.getText().toString()
                    );

                    db.insereUsuario(newUsuario);

                    Navigation.findNavController(view1).navigate(R.id.register_to_login);
                }else{
                    Toast.makeText(getContext(), "Os campos de senhas devem ser iguais", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}