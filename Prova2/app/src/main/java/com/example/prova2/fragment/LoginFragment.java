package com.example.prova2.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prova2.Home;
import com.example.prova2.R;
import com.example.prova2.bd.BDCarrinho;
import com.example.prova2.bd.BDPedido;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.bd.BDUsuario;
import com.example.prova2.model.Produto;
import com.example.prova2.model.Usuario;

public class LoginFragment extends Fragment implements View.OnClickListener{
    Button btn_nav_registrar;
    Button btn_login;

    EditText txt_login_usuario;
    EditText txt_login_senha;
    TextView btn_popular;

    BDUsuario bdUsuario;
    BDProduto bdProduto;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bdUsuario = BDUsuario.getInstance(view.getContext());
        bdProduto = BDProduto.getInstance(view.getContext());

        txt_login_usuario = view.findViewById(R.id.txt_login_usuario);
        txt_login_senha = view.findViewById(R.id.txt_login_senha);

        btn_popular = view.findViewById(R.id.btn_popular);
        btn_popular.setOnClickListener(this);

        btn_nav_registrar = view.findViewById(R.id.btn_nav_registrar);
        btn_nav_registrar.setOnClickListener(view1 ->
                Navigation.findNavController(view1).navigate(R.id.login_to_register)
        );

        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view1 ->{
            Usuario usuario = bdUsuario.Login(txt_login_usuario.getText().toString(), txt_login_senha.getText().toString());

            if(usuario != null){
                Intent i = new Intent(getActivity(), Home.class);
                i.putExtra("ID_USUARIO", usuario.getId());
                startActivity(i);
                getActivity().finish();
            }else{
                Toast.makeText(getContext(), "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_popular){
            try{

                Drawable drawableChicked = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.chicken_duplo, null);
                Bitmap iconChicked = ((BitmapDrawable)drawableChicked).getBitmap();
                Produto newProdutoChicked = new Produto("0", "Chicken Duplo", "É de dar água na boca!",10.90, 10, iconChicked);
                bdProduto.Popula(newProdutoChicked);

                Drawable drawableMega = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.mega_stacker, null);
                Bitmap iconMega = ((BitmapDrawable)drawableMega).getBitmap();
                Produto newProdutoMega = new Produto("0", "Mega Stacker", "Duas deliciosas carnes grelhadas no fogo como churrasco, queijo derretido, bacon e molho Stacker.",39.90, 0, iconMega);
                bdProduto.Popula(newProdutoMega);

                Drawable drawableCheddar = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.cheddar, null);
                Bitmap iconCheddar = ((BitmapDrawable)drawableCheddar).getBitmap();
                Produto newProdutoCheddar = new Produto("0", "Cheddar", "Tudo na medida perfeita da sua fome.",19.90, 5, iconCheddar);
                bdProduto.Popula(newProdutoCheddar);

                Drawable drawableRodeio = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.rodeio, null);
                Bitmap iconRodeio = ((BitmapDrawable)drawableRodeio).getBitmap();
                Produto newProdutoRodeio = new Produto("0", "Rodeio", "Hambúrguer grelhado no fogo como churrasco, queijo derretido, onion rings e molho barbecue.",19.90, 0, iconRodeio);
                bdProduto.Popula(newProdutoRodeio);

                Toast.makeText(getContext(), "Produtos criados", Toast.LENGTH_SHORT).show();
            }catch(SQLiteException err){
                Log.e("ERROPOPULAR", err.getMessage());
            }
        }
    }
}