package com.example.prova2.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prova2.CadastrarEditarProduto;
import com.example.prova2.Home;
import com.example.prova2.R;
import com.example.prova2.adapter.ProdutoAdapter;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.model.Produto;

import java.util.ArrayList;

public class CardapioFragment extends Fragment {
    RecyclerView rvlista;
    ProdutoAdapter adapter;
    String idUsuario=null;

    BDProduto db;
    ArrayList<Produto> produtos = new ArrayList<>();

    public CardapioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        idUsuario = ((Home)getActivity()).getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cardapio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = BDProduto.getInstance(view.getContext());

        produtos = db.findAll();

        rvlista = (RecyclerView) view.findViewById(R.id.rvLista_cardapio);
        rvlista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        createAdapter();

        rvlista.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.add_produto, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_button) {
            Intent i = new Intent(getActivity(), CadastrarEditarProduto.class);
            i.putExtra("ID_USUARIO", idUsuario);
            myActivityResultLauncher.launch(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ExcluirProduto(int position, String id) {
        try {
            db.excluiproduto(id);

            produtos.remove(position);
            adapter.notifyItemRemoved(position);

            Toast.makeText(getContext(), "Exluido com sucesso!", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        produtos.clear();
                        produtos = db.findAll();

                        createAdapter();
                        rvlista.setAdapter(adapter);
                    }
                }
            });

    public void createAdapter(){
        adapter = new ProdutoAdapter(getContext(), produtos, new ProdutoAdapter.ItemClickListener() {

            @Override
            public void onItemClick(Produto produto) {

            }

            @Override
            public void itemDeleteClick(int position, Produto produto) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Remover produto")
                        .setMessage("Tem certeza que deseja remover?")
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setPositiveButton("Sim", (dialog, whichButton) -> ExcluirProduto(position, produto.getId()))
                        .setNegativeButton("Não", null).show();
            }

            @Override
            public void itemEditClick(Produto produto) {
                Intent myIntent = new Intent(getContext(), CadastrarEditarProduto.class);
                myIntent.putExtra("ID_PRODUTO", produto.getId());
                myIntent.putExtra("ID_USUARIO", idUsuario);

                myActivityResultLauncher.launch(myIntent);
            }
        });
    }
}