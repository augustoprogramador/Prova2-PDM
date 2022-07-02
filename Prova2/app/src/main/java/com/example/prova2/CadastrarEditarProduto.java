package com.example.prova2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prova2.bd.BDProduto;
import com.example.prova2.model.Produto;

public class CadastrarEditarProduto extends AppCompatActivity implements View.OnClickListener{
    EditText txt_nome_produto;
    EditText txt_descricao;
    EditText txt_preco;
    EditText txt_desconto;
    ImageView img_produto_novo;
    Boolean haveImage = false;

    Button btn_cadastrar;

    BDProduto db;

    String idProduto;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_editar_produto);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idProduto= null;
            idUsuario=null;
        } else {
            idProduto= extras.getString("ID_PRODUTO");
            idUsuario = extras.getString("ID_USUARIO");
        }

        db = BDProduto.getInstance(this);

        txt_nome_produto = findViewById(R.id.txt_nome_produto);
        txt_descricao = findViewById(R.id.txt_descricao);
        txt_preco = findViewById(R.id.txt_preco);
        txt_desconto = findViewById(R.id.txt_desconto);

        img_produto_novo = findViewById(R.id.img_produto_novo);
        img_produto_novo.setOnClickListener(this);

        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_cadastrar.setOnClickListener(this);

        Produto produto = null;

        if(idProduto != null){
            produto = db.findByID(idProduto);
            btn_cadastrar.setText("Salvar");
        }

        if(produto != null){
            setFields(produto);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_cadastrar) {
            String novo_produto = txt_nome_produto.getText().toString();
            String novo_descricao = txt_descricao.getText().toString();
            String novo_preco = txt_preco.getText().toString();

            String novo_desconto = txt_desconto.getText().toString();

            img_produto_novo.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) img_produto_novo.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            if(novo_produto.equals("") || novo_descricao.equals("") || novo_preco.equals("") || novo_desconto.equals("") || !haveImage){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Double novo_preco_double = Double.valueOf(novo_preco);
                Integer novo_desconto_integer = Integer.valueOf(novo_desconto);

                if (idProduto != null){
                    Produto currentProduto = new Produto(idProduto, novo_produto, novo_descricao, novo_preco_double, novo_desconto_integer, bitmap);

                    db.atualizaProduto(currentProduto);

                    Toast.makeText(this, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("ID_USUARIO", idUsuario);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else{
                    Produto newProduto = new Produto(idProduto, novo_produto, novo_descricao, novo_preco_double, novo_desconto_integer, bitmap);

                    db.insereProduto(newProduto);

                    Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("ID_USUARIO", idUsuario);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

        }else if(view.getId() == R.id.img_produto_novo){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageActivityResultLauncher.launch(pickPhoto);
            haveImage = true;
        }
    }

    public void setFields(Produto produto){
        txt_nome_produto.setText(produto.getNome());
        txt_descricao.setText(produto.getDescricao());
        txt_preco.setText(produto.getPreco().toString());
        txt_desconto.setText(produto.getDesconto().toString());
        img_produto_novo.setImageBitmap(produto.getImg());
        haveImage = true;
    }

    ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri selectedImage = data.getData();

                    img_produto_novo.setImageURI(selectedImage);
                }
            }
        });
}