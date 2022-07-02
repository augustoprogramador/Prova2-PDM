package com.example.prova2.model;

import android.graphics.Bitmap;

public class Produto {
    private String id;
    private String nome;
    private String descricao;
    private Bitmap img;
    private Double preco;
    private Integer desconto;

    public Produto (String id, String nome, String descricao, Double preco, Integer desconto, Bitmap imagem){
        this.id = id;
        this.desconto = desconto;
        this.preco = preco;
        this.img = imagem;
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getDesconto() {
        return desconto;
    }

    public void setDesconto(Integer desconto) {
        this.desconto = desconto;
    }
}
