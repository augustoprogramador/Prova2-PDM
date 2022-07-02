package com.example.prova2.bd;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.example.prova2.R;
import com.example.prova2.model.Produto;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BDProduto extends SQLiteOpenHelper {
    public final static String NOME_BANCO = "prova2";
    public final static int VERSAO_BANCO = 10;

    private static BDProduto instance;

    private static SQLiteDatabase db_write;

    private BDProduto(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    public static synchronized BDProduto getInstance(Context context) {
        if (instance == null) {
            instance = new BDProduto(context);
            db_write = instance.getWritableDatabase();

            db_write.execSQL("CREATE TABLE if NOT EXISTS tb_produto (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "descricao TEXT, " +
                    "preco REAL, " +
                    "desconto INTEGER, " +
                    "imagem BLOB);");
        }
        return instance;
    }

    public Produto insereProduto(Produto produto) throws SQLiteException {
        byte[] byte_img = this.getBytes(produto.getImg());

        ContentValues new_values = new ContentValues();
        new_values.put("nome", produto.getNome());
        new_values.put("descricao", produto.getDescricao());
        new_values.put("preco", produto.getPreco());
        new_values.put("desconto", produto.getDesconto());
        new_values.put("imagem", byte_img);

        long new_id = db_write.insert( "tb_produto", null, new_values );

        return findByID(String.valueOf(new_id));
    }

    public void Popula(Produto produto) throws SQLiteException {
        byte[] byte_img = this.getBytes(produto.getImg());

        ContentValues new_values = new ContentValues();
        new_values.put("nome", produto.getNome());
        new_values.put("descricao", produto.getDescricao());
        new_values.put("preco", produto.getPreco());
        new_values.put("desconto", produto.getDesconto());
        new_values.put("imagem", byte_img);

        db_write.insert( "tb_produto", null, new_values );
    }


    public void atualizaProduto(Produto produto) throws SQLiteException{
        byte[] byte_img = this.getBytes(produto.getImg());

        ContentValues content = new ContentValues();
        content.put("nome", produto.getNome());
        content.put("descricao", produto.getDescricao());
        content.put("preco", produto.getPreco());
        content.put("desconto", produto.getDesconto());
        content.put("imagem", byte_img);

        db_write.update( "tb_produto", content, "_id=?", new String[]{produto.getId()} );
    }

    public Produto findByID(String query_id) throws SQLiteException{
        Produto produto = null;
        Cursor cursor = db_write.query("tb_produto", new String[]{"_id", "nome, descricao, preco, desconto, imagem"}, "_id=?", new String[]{query_id}, null, null, null);

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));
            Integer desconto = cursor.getInt(cursor.getColumnIndexOrThrow("desconto"));

            byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow("imagem"));
            Bitmap imagem = getImage(img);

            produto = new Produto(id, nome, descricao, preco, desconto, imagem);
        }
        cursor.close();

        return produto;
    }

    public ArrayList<Produto> findProdutosComDesconto(){
        ArrayList<Produto> produtos = new ArrayList<Produto>();

        Cursor cursor = db_write.query("tb_produto", new String[]{"_id", "nome, descricao, preco, desconto, imagem"}, "desconto>?", new String[]{"0"}, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));
                Integer desconto = cursor.getInt(cursor.getColumnIndexOrThrow("desconto"));

                byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow("imagem"));
                Bitmap imagem = getImage(img);

                produtos.add(new Produto(id, nome, descricao, preco, desconto, imagem));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return produtos;
    }

    public ArrayList<Produto> findProdutosSemDesconto(){
        ArrayList<Produto> produtos = new ArrayList<Produto>();

        Cursor cursor = db_write.query("tb_produto", new String[]{"_id", "nome, descricao, preco, desconto, imagem"}, "desconto=?", new String[]{"0"}, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));
                Integer desconto = cursor.getInt(cursor.getColumnIndexOrThrow("desconto"));

                byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow("imagem"));
                Bitmap imagem = getImage(img);

                produtos.add(new Produto(id, nome, descricao, preco, desconto, imagem));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return produtos;
    }

    public ArrayList<Produto> findAll(){
        ArrayList<Produto> produtos = new ArrayList<Produto>();

        Cursor cursor = db_write.query("tb_produto", new String[]{"_id", "nome, descricao, preco, desconto, imagem"}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));
                Integer desconto = cursor.getInt(cursor.getColumnIndexOrThrow("desconto"));

                byte[] img = cursor.getBlob(cursor.getColumnIndexOrThrow("imagem"));
                Bitmap imagem = getImage(img);

                produtos.add(new Produto(id, nome, descricao, preco, desconto, imagem));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return produtos;
    }


    public ArrayList<String> getIDs(){
        ArrayList<String> IDs = new ArrayList<String>();

        Cursor cursor = db_write.query("tb_produto", new String[]{"_id", "nome"}, "desconto>?", new String[]{"0"}, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                IDs.add(id);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return IDs;
    }

    public void excluiproduto(String id) throws SQLiteException{
        db_write.delete("tb_produto","_id=?", new String[]{id});
    }

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void removeAll(){
        db_write.execSQL("DELETE FROM tb_produto");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tb_produto (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, descricao TEXT, preco REAL, desconto INTEGER, imagem BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tb_produto");
        onCreate(sqLiteDatabase);
    }

    @Override
    public synchronized void close() {
        if (instance != null)
            db_write.close();
    }
}
