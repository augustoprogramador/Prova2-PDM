package com.example.prova2.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.prova2.model.Carrinho;
import com.example.prova2.model.Pedido;

import java.util.ArrayList;

public class BDPedido extends SQLiteOpenHelper {
    public final static String NOME_BANCO = "prova2";
    public final static int VERSAO_BANCO = 10;

    private static BDPedido instance;

    private static SQLiteDatabase db_write;

    private BDPedido(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    public static synchronized BDPedido getInstance(Context context) {
        if (instance == null) {
            instance = new BDPedido(context);
            db_write = instance.getWritableDatabase();

            db_write.execSQL("CREATE TABLE if NOT EXISTS tb_pedido (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "valorFinal TEXT, " +
                    "idUsuario TEXT, " +
                    "nomesProdutos TEXT, " +
                    "formaPagamento TEXT, " +
                    "enderecoEntrega TEXT " +
                    ");"
            );
        }
        return instance;
    }

    public Pedido findByID(String query_id){
        Pedido pedido = null;
        Cursor cursor = db_write.query("tb_pedido", new String[]{"_id", "valorFinal, idUsuario, nomesProdutos, formaPagamento, enderecoEntrega"}, "_id=?", new String[]{query_id}, null, null, null);

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String valorFinal = cursor.getString(cursor.getColumnIndexOrThrow("valorFinal"));
            String idUsuario = cursor.getString(cursor.getColumnIndexOrThrow("idUsuario"));
            String nomesProdutos = cursor.getString(cursor.getColumnIndexOrThrow("nomesProdutos"));
            String formaPagamento = cursor.getString(cursor.getColumnIndexOrThrow("formaPagamento"));
            String enderecoEntrega = cursor.getString(cursor.getColumnIndexOrThrow("enderecoEntrega"));

            pedido = new Pedido(id, valorFinal, idUsuario, nomesProdutos, formaPagamento, enderecoEntrega);
        }
        cursor.close();

        return pedido;
    }

    public Pedido inserePedido(Pedido pedido) throws SQLiteException {

        ContentValues new_values = new ContentValues();
        new_values.put("valorFinal", pedido.getValorFinal());
        new_values.put("idUsuario", pedido.getIdUsuario());
        new_values.put("nomesProdutos", pedido.getNomesProdutos());
        new_values.put("formaPagamento", pedido.getFormaPagamento());
        new_values.put("enderecoEntrega", pedido.getEnderecoEntrega());

        long new_id = db_write.insert( "tb_pedido", null, new_values );

        return findByID(String.valueOf(new_id));
    }

    public void atualizaPedido(Pedido pedido) throws SQLiteException{

        ContentValues content = new ContentValues();
        content.put("valorFinal", pedido.getValorFinal());
        content.put("idUsuario", pedido.getIdUsuario());
        content.put("nomesProdutos", pedido.getNomesProdutos());
        content.put("formaPagamento", pedido.getFormaPagamento());
        content.put("enderecoEntrega", pedido.getEnderecoEntrega());

        db_write.update( "tb_pedido", content, "_id=?", new String[]{pedido.getId()} );
    }

    public ArrayList<Pedido> findAll(String idQuery){
        ArrayList<Pedido> pedidos = new ArrayList<>();

        Cursor cursor = db_write.query("tb_pedido", new String[]{"_id", "valorFinal, idUsuario, nomesProdutos, formaPagamento, enderecoEntrega"}, "idUsuario=?", new String[]{idQuery}, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String valorFinal = cursor.getString(cursor.getColumnIndexOrThrow("valorFinal"));
                String idUsuario = cursor.getString(cursor.getColumnIndexOrThrow("idUsuario"));
                String nomesProdutos = cursor.getString(cursor.getColumnIndexOrThrow("nomesProdutos"));
                String formaPagamento = cursor.getString(cursor.getColumnIndexOrThrow("formaPagamento"));
                String enderecoEntrega = cursor.getString(cursor.getColumnIndexOrThrow("enderecoEntrega"));


                pedidos.add(new Pedido(id, valorFinal, idUsuario, nomesProdutos, formaPagamento, enderecoEntrega));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return pedidos;
    }

    public void excluiPedido(String id) throws SQLiteException {
        db_write.delete("tb_pedido","_id=?", new String[]{id});
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tb_pedido (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "valorFinal TEXT, " +
                "idUsuario TEXT, " +
                "nomesProdutos TEXT, " +
                "formaPagamento TEXT, " +
                "enderecoEntrega TEXT " +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tb_pedido");
        onCreate(sqLiteDatabase);
    }
}
