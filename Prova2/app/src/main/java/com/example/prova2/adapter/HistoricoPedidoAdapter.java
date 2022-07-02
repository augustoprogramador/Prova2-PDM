package com.example.prova2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prova2.R;
import com.example.prova2.model.Pedido;

import java.util.ArrayList;

public class HistoricoPedidoAdapter extends RecyclerView.Adapter<HistoricoPedidoAdapter.ViewHolder>{
    private final ArrayList<Pedido> lista;
    private final Context context;

    public HistoricoPedidoAdapter(Context c, ArrayList<Pedido> l){
        context = c;
        lista = l;
    }

    @NonNull
    @Override
    public HistoricoPedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.historico_pedido_vertical, parent, false);
        return new HistoricoPedidoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoPedidoAdapter.ViewHolder holder, int position) {
        holder.getNomes().setText(lista.get(position).getNomesProdutos());
        holder.getValorFinal().setText(lista.get(position).getValorFinal());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomes;
        private final TextView valorFinal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomes = (TextView) itemView.findViewById(R.id.txt_historico_nomes);
            valorFinal = (TextView) itemView.findViewById(R.id.txt_historico_valor_final);
        }

        public TextView getNomes() {
            return nomes;
        }

        public TextView getValorFinal() {
            return valorFinal;
        }
    }
}
