package com.example.prova2.adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prova2.R;
import com.example.prova2.bd.BDCarrinho;
import com.example.prova2.bd.BDProduto;
import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Produto;

public class DetalhesProduto extends AppCompatActivity implements View.OnClickListener{
    TextView txt_detalhes_nome;
    TextView txt_detalhes_descricao;
    TextView txt_detalhes_quantidade;

    ImageView img_detalhes_imagem;
    ImageView btn_detalhes_add;
    ImageView btn_detalhes_remove;

    Button btn_adicionar_carrinho;

    String idProduto;
    String idUsuario;

    BDProduto bdProduto;
    BDCarrinho bdCarrinho;

    Produto produto;
    Carrinho carrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idProduto= null;
            idUsuario=null;
        } else {
            idProduto= extras.getString("ID_PRODUTO");
            idUsuario = extras.getString("ID_USUARIO");
        }

        bdProduto = BDProduto.getInstance(this);
        bdCarrinho = BDCarrinho.getInstance(this);

        txt_detalhes_nome = findViewById(R.id.txt_detalhes_nome);
        txt_detalhes_descricao = findViewById(R.id.txt_detalhes_descricao);
        txt_detalhes_quantidade = findViewById(R.id.txt_detalhes_quantidade);

        img_detalhes_imagem = findViewById(R.id.img_detalhes_imagem);
        btn_detalhes_add = findViewById(R.id.btn_detalhes_add);
        btn_detalhes_remove = findViewById(R.id.btn_detalhes_remove);

        btn_adicionar_carrinho = findViewById(R.id.btn_adicionar_carrinho);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(idProduto != null){
            produto = bdProduto.findByID(idProduto);
        }

        if(produto != null && idUsuario != null){
            carrinho = bdCarrinho.findByProductID(produto.getId(), idUsuario);
        }

        if(produto != null){
            img_detalhes_imagem.setImageBitmap(produto.getImg());
            txt_detalhes_nome.setText(produto.getNome());
            txt_detalhes_descricao.setText(produto.getDescricao());

            txt_detalhes_quantidade.setText("1");
        }

        if(carrinho != null){
            txt_detalhes_quantidade.setText(carrinho.getQuantidade().toString());
        }

        btn_detalhes_add.setOnClickListener(this);
        btn_detalhes_remove.setOnClickListener(this);
        btn_adicionar_carrinho.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_detalhes_add:
                Integer quantidadeAdd = Integer.valueOf(txt_detalhes_quantidade.getText().toString());
                quantidadeAdd +=1;

                txt_detalhes_quantidade.setText(String.valueOf(quantidadeAdd));

                break;
            case R.id.btn_detalhes_remove:
                Integer quantidadeRemove = Integer.valueOf(txt_detalhes_quantidade.getText().toString());

                if(quantidadeRemove!=1){
                    quantidadeRemove -=1;

                    txt_detalhes_quantidade.setText(String.valueOf(quantidadeRemove));
                }
                break;
            case R.id.btn_adicionar_carrinho:
                Integer adicionarQuantidade = Integer.valueOf(txt_detalhes_quantidade.getText().toString());

                if(carrinho!=null){
                    carrinho.setQuantidade(adicionarQuantidade);
                    bdCarrinho.atualizaCarrinho(carrinho);

                    Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Carrinho newCarrinho = new Carrinho("0", adicionarQuantidade, idUsuario, produto.getId());
                    bdCarrinho.insereCarrinho(newCarrinho);

                    Toast.makeText(this, "Inserido com sucesso", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}