package com.example.prova2.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.example.prova2.Home;
import com.example.prova2.R;
import com.example.prova2.adapter.DetalhesProduto;
import com.example.prova2.adapter.PedidoAdapterHorizontal;
import com.example.prova2.adapter.PedidoAdapterVertical;
import com.example.prova2.bd.BDCarrinho;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Produto;

import java.util.ArrayList;


public class NovoPedidoFragment extends Fragment {
    RecyclerView rvlistaVertical;
    RecyclerView rvlistaHoriziontal;

    BDProduto db;
    BDCarrinho bdCarrinho;

    ArrayList<Produto> produtosComDesconto = new ArrayList<>();
    ArrayList<Produto> produtosSemDesconto = new ArrayList<>();

    PedidoAdapterVertical adapterVertical;
    PedidoAdapterHorizontal adapterHorizontal;

    String idUsuario;

    public NovoPedidoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idUsuario = ((Home)getActivity()).getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_novo_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = BDProduto.getInstance(view.getContext());
        bdCarrinho = BDCarrinho.getInstance(view.getContext());

        produtosComDesconto = db.findProdutosComDesconto();
        produtosSemDesconto = db.findProdutosSemDesconto();

        rvlistaVertical = (RecyclerView) view.findViewById(R.id.rvLista_pedido_v);
        rvlistaVertical.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        createAdapterVertical();

        rvlistaVertical.setAdapter(adapterVertical);

        rvlistaHoriziontal = (RecyclerView) view.findViewById(R.id.rvLista_pedido_h);
        rvlistaHoriziontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        createAdapterHorizontal();

        rvlistaHoriziontal.setAdapter(adapterHorizontal);
    }

    public void createAdapterVertical(){
        adapterVertical = new PedidoAdapterVertical(getContext(), produtosSemDesconto, new PedidoAdapterVertical.ItemClickListener() {

            @Override
            public void onItemClick(Produto produto) {
                Intent i = new Intent(getContext(), DetalhesProduto.class);
                i.putExtra("ID_USUARIO", idUsuario);
                i.putExtra("ID_PRODUTO", produto.getId());
                startActivity(i);
            }

            @Override
            public void itemAddClick(int position, Boolean adicionar, Produto produto) {
                Carrinho carrinho = bdCarrinho.findByProductID(produto.getId(), idUsuario);

                if(carrinho != null)
                    bdCarrinho.excluiCarrinho(carrinho.getId());
                else{
                    Carrinho newCarrinho = new Carrinho("0", 1, idUsuario, produto.getId());
                    bdCarrinho.insereCarrinho(newCarrinho);
                }

                ((Home)getActivity()).updateCarrinhoIcon();
            }
        }, idUsuario);
    }

    public void createAdapterHorizontal(){
        adapterHorizontal = new PedidoAdapterHorizontal(getContext(), produtosComDesconto, new PedidoAdapterHorizontal.ItemClickListener() {

            @Override
            public void onItemClick(Produto produto) {
                Intent i = new Intent(getContext(), DetalhesProduto.class);
                i.putExtra("ID_USUARIO", idUsuario);
                i.putExtra("ID_PRODUTO", produto.getId());
                startActivity(i);
            }

            @Override
            public void itemAddClick(int position, Boolean adicionar, Produto produto) {
                Carrinho carrinho = bdCarrinho.findByProductID(produto.getId(), idUsuario);

                if(carrinho != null)
                    bdCarrinho.excluiCarrinho(carrinho.getId());
                else{
                    Carrinho newCarrinho = new Carrinho("0", 1, idUsuario, produto.getId());
                    bdCarrinho.insereCarrinho(newCarrinho);
                }

                ((Home)getActivity()).updateCarrinhoIcon();
            }
        }, idUsuario);
    }
}