package com.example.prova2.model;


public class Pedido {
    private String id;
    private String valorFinal;
    private String idUsuario;
    private String nomesProdutos;
    private String formaPagamento;
    private String enderecoEntrega;

    public Pedido (String id, String valorFinal, String idUsuario, String nomesProdutos, String formaPagamento, String enderecoEntrega){
        this.id = id;
        this.valorFinal = valorFinal;
        this.idUsuario = idUsuario;
        this.nomesProdutos = nomesProdutos;
        this.formaPagamento = formaPagamento;
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getNomesProdutos() {
        return nomesProdutos;
    }

    public void setNomesProdutos(String nomesProdutos) {
        this.nomesProdutos = nomesProdutos;
    }
}
