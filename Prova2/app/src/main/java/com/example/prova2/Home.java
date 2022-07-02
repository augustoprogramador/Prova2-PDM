package com.example.prova2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prova2.bd.BDCarrinho;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.bd.BDUsuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;


public class Home extends AppCompatActivity {
    FragmentContainerView fragmentContainerView;
    BottomNavigationView bottomNav;
    TextView tv;
    BDCarrinho bdCarrinho;

    private static String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav= findViewById(R.id.bottomNavigationView);
        fragmentContainerView = findViewById(R.id.fragmentContainerView2);

        if(idUsuario == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idUsuario=null;
            } else {
                idUsuario = extras.getString("ID_USUARIO");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    protected void onStart() {
        super.onStart();

        NavController navController = Navigation.findNavController(fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        bdCarrinho = BDCarrinho.getInstance(this);

        MenuItem item = menu.findItem(R.id.badge);
        MenuItem comp = item.setActionView(R.layout.cart_icon);
        RelativeLayout notifCount = (RelativeLayout)  comp.getActionView();

        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);

        ImageView icon = (ImageView) notifCount.findViewById(R.id.cart_icon_imageview);
        icon.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), CarrinhoActivity.class);
            i.putExtra("ID_USUARIO", idUsuario);
            startActivity(i);
        });

        updateCarrinhoIcon();

        return super.onCreateOptionsMenu(menu);
    }

    public void updateCarrinhoIcon(){
        if(idUsuario!=null)
            tv.setText(String.valueOf(bdCarrinho.getQuantidadeCarrinho(idUsuario)));
    }

    public String getUserId(){
        return idUsuario;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_sair) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}