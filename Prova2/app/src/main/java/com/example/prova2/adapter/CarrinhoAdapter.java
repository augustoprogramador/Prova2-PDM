package com.example.prova2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prova2.R;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Produto;

import java.util.ArrayList;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>{
    private final ArrayList<Carrinho> lista;
    private final Context context;
    private final CarrinhoAdapter.ItemClickListener mItemListener;
    private final BDProduto bdProduto;

    public CarrinhoAdapter(Context c, ArrayList<Carrinho> l, CarrinhoAdapter.ItemClickListener itemClickListener){
        context = c;
        lista = l;
        mItemListener = itemClickListener;
        bdProduto = BDProduto.getInstance(c);
    }

    public interface ItemClickListener{
        void onItemClick(Carrinho carrinho);
        void itemAddClick(int position,Carrinho carrinho);
        void itemRemoveClick(int position,Carrinho carrinho);
    }

    @NonNull
    @Override
    public CarrinhoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.carrinho_vertical, parent, false);
        return new CarrinhoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoAdapter.ViewHolder holder, int position) {
        Produto produto = bdProduto.findByID(lista.get(position).getIdProduto());

        holder.getImg().setImageBitmap(produto.getImg());
        holder.getNome().setText(produto.getNome());
        holder.getPreco().setText("R$ "+ produto.getPreco().toString());

        holder.getQuantidade().setText(lista.get(position).getQuantidade().toString());

        holder.itemView.setOnClickListener(view -> mItemListener.onItemClick(lista.get(position)));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private final TextView nome;
        private final TextView preco;
        private final TextView quantidade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_cardapio_produto);
            nome = itemView.findViewById(R.id.txt_nome_carrinho);
            preco = itemView.findViewById(R.id.txt_preco_carrinho);
            quantidade = itemView.findViewById(R.id.txt_quantidade_carrinho);

            ImageView add = itemView.findViewById(R.id.btn_add_carrinho);

            add.setOnClickListener(view -> {
                int position = getAdapterPosition();
                mItemListener.itemAddClick(position, lista.get(position));
            });

            ImageView remove = itemView.findViewById(R.id.btn_remove_carrinho);

            remove.setOnClickListener(view -> {
                int position = getAdapterPosition();
                mItemListener.itemRemoveClick(position, lista.get(position));
            });
        }

        public ImageView getImg() {
            return img;
        }

        public TextView getNome() {
            return nome;
        }

        public TextView getPreco() {
            return preco;
        }

        public TextView getQuantidade() {
            return quantidade;
        }
    }
}
