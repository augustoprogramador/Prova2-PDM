package com.example.prova2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prova2.adapter.CarrinhoAdapter;
import com.example.prova2.bd.BDCarrinho;
import com.example.prova2.bd.BDPedido;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Pedido;
import com.example.prova2.model.Produto;

import java.util.ArrayList;

public class CarrinhoActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    BDCarrinho bdCarrinho;
    BDProduto bdProduto;
    BDPedido bdPedido;

    CarrinhoAdapter adapter;
    RecyclerView rvlista;

    ArrayList<Carrinho> carrinhos = new ArrayList<>();

    Button btn_fechar_pedido;
    TextView txt_carrinho_valor_total;
    TextView txt_endereco_entrega;
    RadioGroup radio_group;

    String formaPagamento="";

    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idUsuario=null;
        } else {
            idUsuario = extras.getString("ID_USUARIO");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bdCarrinho = BDCarrinho.getInstance(this);
        bdProduto = BDProduto.getInstance(this);
        bdPedido = BDPedido.getInstance(this);

        carrinhos = bdCarrinho.findAll(idUsuario);

        rvlista = (RecyclerView) findViewById(R.id.rvListaCarrinho);
        rvlista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        createAdapter();

        rvlista.setAdapter(adapter);

        btn_fechar_pedido = findViewById(R.id.btn_fechar_pedido);
        btn_fechar_pedido.setOnClickListener(this);

        txt_carrinho_valor_total = findViewById(R.id.txt_carrinho_valor_total);
        txt_endereco_entrega = findViewById(R.id.txt_endereco_entrega);

        radio_group = findViewById(R.id.radio_group);
        radio_group.setOnCheckedChangeListener(this);

        AtualizaValorTotal();
    }

    public void createAdapter(){
        adapter = new CarrinhoAdapter(this, carrinhos, new CarrinhoAdapter.ItemClickListener() {

            @Override
            public void onItemClick(Carrinho carrinho) {

            }

            @Override
            public void itemAddClick(int position, Carrinho carrinho) {
                Integer novaQuantidade = carrinho.getQuantidade()+1;
                carrinho.setQuantidade(novaQuantidade);

                bdCarrinho.atualizaCarrinho(carrinho);

                carrinhos.set(position, carrinho);
                adapter.notifyItemChanged(position);

                AtualizaValorTotal();
            }

            @Override
            public void itemRemoveClick(int position, Carrinho carrinho) {
                if(carrinho.getQuantidade()==1){
                    bdCarrinho.excluiCarrinho(carrinho.getId());

                    carrinhos.remove(position);
                    adapter.notifyItemRemoved(position);
                }else{
                    Integer novaQuantidade = carrinho.getQuantidade()-1;
                    carrinho.setQuantidade(novaQuantidade);

                    bdCarrinho.atualizaCarrinho(carrinho);

                    carrinhos.set(position, carrinho);
                    adapter.notifyItemChanged(position);
                }

                AtualizaValorTotal();
            }
        });
    }

    public void AtualizaValorTotal(){
        Double valorTotal=0.0;

        for (int i = 0; i < carrinhos.size(); i++) {
            Produto produto = bdProduto.findByID(carrinhos.get(i).getIdProduto());

            Integer quantidade = carrinhos.get(i).getQuantidade();
            Double preco = produto.getPreco();

            valorTotal += preco*quantidade;
        }

        String valor = String.format("%.2f",valorTotal);

        txt_carrinho_valor_total.setText("Total: R$"+ valor);
    }

    public String PegaNomesProdutos(){
        String nomes="";

        for (int i = 0; i < carrinhos.size(); i++) {
            Produto produto = bdProduto.findByID(carrinhos.get(i).getIdProduto());

            if(i==carrinhos.size()-1)
                nomes += produto.getNome()+"("+carrinhos.get(i).getQuantidade()+")";
            else
                nomes += produto.getNome()+"("+carrinhos.get(i).getQuantidade()+"), ";
        }

        return nomes;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_fechar_pedido){
            if(txt_endereco_entrega.getText().equals("") ||formaPagamento.equals("") || carrinhos.size() == 0){
                Toast.makeText(this, "Preencha todos os campos e tenha itens adicionados", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Pedido newPedido = new Pedido("0", txt_carrinho_valor_total.getText().toString(), idUsuario, PegaNomesProdutos(), formaPagamento, txt_endereco_entrega.getText().toString());
                bdPedido.inserePedido(newPedido);

                Toast.makeText(this, "Pedido criado com sucesso!", Toast.LENGTH_SHORT).show();

                bdCarrinho.esvaziaCarrinho();
                carrinhos = new ArrayList<>();

                createAdapter();
                rvlista.setAdapter(adapter);

                txt_carrinho_valor_total.setText("Total: R$00,00");

                radio_group.clearCheck();
                radio_group.setOnCheckedChangeListener(this);

                formaPagamento = "";

                txt_endereco_entrega.setText("");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch(checkedId){
            case R.id.btn_dinheiro:
                formaPagamento = "Dinheiro";
                break;
            case R.id.btn_credito:
                formaPagamento = "Cartão de Crédito";
                break;
            case R.id.btn_debito:
                formaPagamento = "Cartão de Débito";
                break;
        }
    }
}