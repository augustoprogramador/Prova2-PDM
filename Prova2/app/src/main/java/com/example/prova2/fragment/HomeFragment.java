package com.example.prova2.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prova2.Home;
import com.example.prova2.R;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.bd.BDUsuario;
import com.example.prova2.model.Produto;
import com.example.prova2.model.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;

public class HomeFragment extends Fragment {
    TextView txt_nome_home;
    TextView txt_em_promocao;
    ImageView img_produto_home;
    Button btn_iniciar_pedido;

    BDUsuario bdUsuario;
    BDProduto bdProduto;

    Usuario usuario;
    Produto produto;
    String idUsuario;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bdUsuario = BDUsuario.getInstance(getContext());
        bdProduto = BDProduto.getInstance(getContext());

        idUsuario = ((Home)getActivity()).getUserId();

        if(idUsuario != null && !idUsuario.equals(""))
            usuario = bdUsuario.findByID(idUsuario);

        ArrayList<String> produtosIDs = bdProduto.getIDs();

        if(produtosIDs.size() > 0){
            Random rand = new Random();
            int n = rand.nextInt(produtosIDs.size());

            produto = bdProduto.findByID(produtosIDs.get(n));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_nome_home = view.findViewById(R.id.txt_nome_home);
        txt_em_promocao = view.findViewById(R.id.txt_em_promocao);
        img_produto_home = view.findViewById(R.id.img_produto_home);

        btn_iniciar_pedido = view.findViewById(R.id.btn_iniciar_pedido);
        btn_iniciar_pedido.setOnClickListener(view1 -> {
            Home activity = (Home) getActivity();

            BottomNavigationView mBottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
            FragmentContainerView fragmentContainerView = activity.findViewById(R.id.fragmentContainerView2);

            NavController navController = Navigation.findNavController(fragmentContainerView);

            NavigationUI.onNavDestinationSelected(mBottomNavigationView.getMenu().findItem(R.id.novoPedidoFragment), navController);
        });

        if(usuario != null)
            txt_nome_home.setText(usuario.getNome());

        if(produto != null)
            img_produto_home.setImageBitmap(produto.getImg());
        else{
            txt_em_promocao.setText("Por hora não temos promoção");
        }
    }
}