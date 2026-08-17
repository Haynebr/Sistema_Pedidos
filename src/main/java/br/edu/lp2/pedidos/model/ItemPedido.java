package br.edu.lp2.pedidos.model;

public class ItemPedido {
    private int quantidade;
    private ItemCardapio item; // Armazena uma referência para o ItemCardapio que foi adicionado ao pedido.
    //Guarda qual item do cardápio está sendo pedido.

    public double subtotal() {
        return item.getPreco() * quantidade; //Faz a multiplicação do preço do item do cardápio pela quantidade para obter o subtotal.
    }

}
