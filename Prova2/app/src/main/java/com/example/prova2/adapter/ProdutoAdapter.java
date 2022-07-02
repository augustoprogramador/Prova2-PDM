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
import com.example.prova2.model.Produto;

import java.util.ArrayList;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder>{
    private final ArrayList<Produto> lista;
    private final Context context;
    private final ItemClickListener mItemListener;

    public ProdutoAdapter(Context c, ArrayList<Produto> l, ItemClickListener itemClickListener){
        context = c;
        lista = l;
        mItemListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(Produto produto);
        void itemDeleteClick(int position, Produto produto);
        void itemEditClick(Produto produto);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_produto_vertical, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getImg().setImageBitmap(lista.get(position).getImg());

        if(Integer.valueOf(lista.get(position).getDesconto()) > 0){
            holder.getNome().setText("*"+lista.get(position).getNome());
        }else{
            holder.getNome().setText(lista.get(position).getNome());
        }

        holder.itemView.setOnClickListener(view -> mItemListener.onItemClick(lista.get(position)));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private final TextView nome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img_produto_pedido_v);
            nome = (TextView) itemView.findViewById(R.id.txt_nome_produto_pedido_v);

            ImageView deleteButton = (ImageView) itemView.findViewById(R.id.btn_add_pedido_h);
            ImageView editButton = (ImageView) itemView.findViewById(R.id.btn_edit);

            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                mItemListener.itemDeleteClick(position, lista.get(position));
            });

            editButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                mItemListener.itemEditClick(lista.get(position));
            });
        }

        public ImageView getImg() {
            return img;
        }

        public TextView getNome() {
            return nome;
        }
    }
}
