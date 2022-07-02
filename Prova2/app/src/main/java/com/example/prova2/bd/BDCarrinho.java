package com.example.prova2.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Produto;

import java.util.ArrayList;

public class BDCarrinho extends SQLiteOpenHelper {
    public final static String NOME_BANCO = "prova2";
    public final static int VERSAO_BANCO = 10;

    private static BDCarrinho instance;

    private static SQLiteDatabase db_write;

    private BDCarrinho(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    public static synchronized BDCarrinho getInstance(Context context) {
        if (instance == null) {
            instance = new BDCarrinho(context);
            db_write = instance.getWritableDatabase();

            db_write.execSQL("CREATE TABLE if NOT EXISTS tb_carrinho (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "quantidade INTEGER, " +
                    "idUsuario TEXT, " +
                    "idProduto TEXT " +
                    ");"
            );
        }
        return instance;
    }

    public Carrinho insereCarrinho(Carrinho carrinho) throws SQLiteException {

        ContentValues new_values = new ContentValues();
        new_values.put("quantidade", carrinho.getQuantidade());
        new_values.put("idUsuario", carrinho.getIdUsuario());
        new_values.put("idProduto", carrinho.getIdProduto());

        long new_id = db_write.insert( "tb_carrinho", null, new_values );

        return findByID(String.valueOf(new_id));
    }

    public void atualizaCarrinho(Carrinho carrinho) throws SQLiteException{

        ContentValues content = new ContentValues();
        content.put("quantidade", carrinho.getQuantidade());
        content.put("idUsuario", carrinho.getIdUsuario());
        content.put("idProduto", carrinho.getIdProduto());

        db_write.update( "tb_carrinho", content, "_id=?", new String[]{carrinho.getId()} );
    }

    public Integer getQuantidadeCarrinho(String idQuery) throws SQLiteException{
        ArrayList<Carrinho> carrinhos = findAll(idQuery);
        Integer count = 0;

        for (int i = 0; i < carrinhos.size(); i++) {
            count += carrinhos.get(i).getQuantidade();
        }

        return count;
    }

    public Carrinho findByProductID(String query_id, String idUsuario){
        Carrinho carrinho = null;
        Cursor cursor = db_write.query("tb_carrinho", new String[]{"_id", "quantidade, idUsuario, idProduto"}, "idProduto=? AND idUsuario=?", new String[]{query_id, idUsuario}, null, null, null);

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            Integer quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
            String fk_usuario = cursor.getString(cursor.getColumnIndexOrThrow("idUsuario"));
            String fk_produto = cursor.getString(cursor.getColumnIndexOrThrow("idProduto"));

            carrinho = new Carrinho(id, quantidade, fk_usuario, fk_produto);
        }
        cursor.close();

        return carrinho;
    }

    public Carrinho findByID(String query_id){
        Carrinho carrinho = null;
        Cursor cursor = db_write.query("tb_carrinho", new String[]{"_id", "quantidade, idUsuario, idProduto"}, "_id=?", new String[]{query_id}, null, null, null);

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            Integer quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
            String fk_usuario = cursor.getString(cursor.getColumnIndexOrThrow("idUsuario"));
            String fk_produto = cursor.getString(cursor.getColumnIndexOrThrow("idProduto"));

            carrinho = new Carrinho(id, quantidade, fk_usuario, fk_produto);
        }
        cursor.close();

        return carrinho;
    }


    public void excluiCarrinho(String id) throws SQLiteException{
        db_write.delete("tb_carrinho","_id=?", new String[]{id});
    }

    public void esvaziaCarrinho() throws SQLiteException{
        db_write.delete("tb_carrinho",null, null);
    }

    public ArrayList<Carrinho> findAll(String idQuery){
        ArrayList<Carrinho> carrinhos = new ArrayList<>();

        Cursor cursor = db_write.query("tb_carrinho", new String[]{"_id", "quantidade, idUsuario, idProduto"}, "idUsuario=?", new String[]{idQuery}, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                Integer quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
                String fk_usuario = cursor.getString(cursor.getColumnIndexOrThrow("idUsuario"));
                String fk_produto = cursor.getString(cursor.getColumnIndexOrThrow("idProduto"));


                carrinhos.add(new Carrinho(id, quantidade, fk_usuario, fk_produto));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return carrinhos;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tb_carrinho (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quantidade INTEGER, " +
                "idUsuario TEXT, " +
                "idProduto TEXT " +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tb_carrinho");
        onCreate(sqLiteDatabase);
    }
}
