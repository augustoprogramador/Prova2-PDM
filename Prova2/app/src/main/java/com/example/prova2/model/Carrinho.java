package com.example.prova2.model;


public class Carrinho {
    private String id;
    private Integer quantidade;
    private String idUsuario;
    private String idProduto;

    public Carrinho (String id, Integer quantidade, String idUsuario, String idProduto){
        this.id = id;
        this.quantidade = quantidade;
        this.idUsuario = idUsuario;
        this.idProduto = idProduto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }
}
