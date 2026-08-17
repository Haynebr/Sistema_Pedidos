package br.edu.lp2.pedidos.model;

import br.edu.lp2.pedidos.exception.EstoqueInsuficienteException;

public abstract class ItemCardapio implements Descontavel {
    protected String codigo;
    protected String nome;
    protected double preco;
    protected int estoque;

    public abstract String categoria(); //A categoria é implementada por cada tipo de produto. Por isso, abstract.

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public boolean temEstoque(int estoque) {
        if(estoque <= 0) { //Validação simples para valores menores ou iguais a zero.
            throw new IllegalArgumentException("Estoque informado menor ou igual a zero.");
        }
        
        if(estoque <= this.estoque) {
            return true;
        }
        else {
            return false;
        }
    }

    public void baixarEstoque(int quantidade) {
        if(quantidade > this.estoque) { //Validação simples para estoque insuficiente.
            throw new EstoqueInsuficienteException("Estoque disponível insuficiente!");
        }

        if(quantidade <= 0) { //Validação simples para quantidades menores ou iguais a zero.
            throw new IllegalArgumentException("Impossível alterar estoque com base em valores menores ou iguais a zero!");
        }

        this.estoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        if(quantidade <= 0) { //Validação simples para quantidades menores ou iguais a zero.
            throw new IllegalArgumentException("Impossível alterar estoque com base em valores menores ou iguais a zero!");
        }
        this.estoque += quantidade;
    }

}
