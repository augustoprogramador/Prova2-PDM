package com.example.prova2.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prova2.R;
import com.example.prova2.bd.BDCarrinho;
import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Produto;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PedidoAdapterHorizontal extends RecyclerView.Adapter<PedidoAdapterHorizontal.ViewHolder>{
    private final ArrayList<Produto> lista;
    private final Context context;
    private final PedidoAdapterHorizontal.ItemClickListener mItemListener;
    private final BDCarrinho bdCarrinho;
    private final String idUsuario;

    public PedidoAdapterHorizontal(Context c, ArrayList<Produto> l, PedidoAdapterHorizontal.ItemClickListener itemClickListener, String idUsuario){
        this.context = c;
        this.lista = l;
        this.mItemListener = itemClickListener;
        this.bdCarrinho = BDCarrinho.getInstance(c);
        this.idUsuario = idUsuario;
    }

    public interface ItemClickListener{
        void onItemClick(Produto produto);
        void itemAddClick(int position, Boolean adicionar,Produto produto);
    }

    @NonNull
    @Override
    public PedidoAdapterHorizontal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pedido_horizontal, parent, false);
        return new PedidoAdapterHorizontal.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapterHorizontal.ViewHolder holder, int position) {
        Carrinho carrinho = bdCarrinho.findByProductID(lista.get(position).getId(), idUsuario);
        double desconto = lista.get(position).getDesconto();
        double  precoAtt = lista.get(position).getPreco();

        if (desconto != 0){
            precoAtt = lista.get(position).getPreco();
            precoAtt = precoAtt - (precoAtt * (desconto/100));
        }

        DecimalFormat frmt = new DecimalFormat();
        frmt.setMaximumFractionDigits(2);

        holder.getImg().setImageBitmap(lista.get(position).getImg());
        holder.getNome().setText(lista.get(position).getNome());
        holder.getPreco().setText("R$ "+ String.valueOf(frmt.format(precoAtt)));

        holder.itemView.setOnClickListener(view -> mItemListener.onItemClick(lista.get(position)));

        if(carrinho !=null){
            holder.setActive(false);
            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_remove_24, null);

            holder.getAdd().setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private final TextView nome;
        private final TextView preco;
        private final ImageView add;

        private Boolean active = true;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img_produto_pedido_h);
            nome = (TextView) itemView.findViewById(R.id.txt_nome_pedido_h);
            preco = (TextView) itemView.findViewById(R.id.txt_preco_pedido_h);

            add = (ImageView) itemView.findViewById(R.id.btn_add_pedido_h);

            add.setOnClickListener(view -> {
                int position = getAdapterPosition();

                if(active){
                    Drawable drawable = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_remove_24, null);
                    add.setImageDrawable(drawable);

                    active = false;
                }else{
                    Drawable drawable = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_add_24, null);
                    add.setImageDrawable(drawable);

                    active = true;
                }

                mItemListener.itemAddClick(position, active, lista.get(position));
            });
        }

        public ImageView getImg() {
            return img;
        }

        public ImageView getAdd() { return add; }

        public TextView getNome() {
            return nome;
        }

        public TextView getPreco() {
            return preco;
        }

        public Boolean getActive() { return active; }

        public void setActive(Boolean active) { this.active = active; }
    }
}