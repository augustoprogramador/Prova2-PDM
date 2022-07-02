package com.example.prova2.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.prova2.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BDUsuario extends SQLiteOpenHelper {

    public final static String NOME_BANCO = "prova2";
    public final static int VERSAO_BANCO = 10;

    private static BDUsuario instance;

    private static SQLiteDatabase db_write;

    private BDUsuario(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    public static synchronized BDUsuario getInstance(Context context) {
        if (instance == null) {
            instance = new BDUsuario(context);
            db_write = instance.getWritableDatabase();

            db_write.execSQL("CREATE TABLE if NOT EXISTS tb_usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "email TEXT, " +
                    "usuario TEXT, " +
                    "senha TEXT);");
        }
        return instance;
    }

    public Usuario insereUsuario(Usuario usuario) throws SQLiteException {
        ContentValues new_values = new ContentValues();

        new_values.put("nome", usuario.getNome());
        new_values.put("email", usuario.getEmail());
        new_values.put("usuario", usuario.getUsuario());
        new_values.put("senha", usuario.getSenha());

        long new_id = db_write.insert( "tb_usuario", null, new_values );

        return findByID(String.valueOf(new_id));
    }

    public Usuario findByID(String query_id){
        Usuario qUsuario = null;
        Cursor cursor = db_write.query("tb_usuario", new String[]{"_id", "nome, email, usuario, senha"}, "_id=?", new String[]{query_id}, null, null, null);

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));

            qUsuario = new Usuario(id, nome, email, usuario, senha);
        }
        cursor.close();

        return qUsuario;
    }

    public Usuario Login(String login_usuario, String login_senha){
        Usuario qUsuario = null;
        Cursor cursor = db_write.query("tb_usuario", new String[]{"_id", "nome, email, usuario, senha"}, "usuario=? AND senha=?", new String[]{login_usuario, login_senha}, null, null, null);

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));

            qUsuario = new Usuario(id, nome, email, usuario, senha);
        }
        cursor.close();

        return qUsuario;
    }

    public ArrayList<Usuario> findAll(){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        Cursor cursor = db_write.query("tb_usuario", new String[]{"_id", "nome, email, usuario, senha"}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
                String senha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));

                usuarios.add(new Usuario(id, nome, email, usuario, senha));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return usuarios;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void removeAll(){
        db_write.execSQL("DELETE FROM tb_usuario");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tb_usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, email TEXT, usuario TEXT, senha TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tb_usuario");
        onCreate(sqLiteDatabase);
    }

    @Override
    public synchronized void close() {
        if (instance != null)
            db_write.close();
    }
}
