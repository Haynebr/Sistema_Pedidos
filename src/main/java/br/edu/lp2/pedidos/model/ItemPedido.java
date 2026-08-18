package br.edu.lp2.pedidos.model;

/*
 * Representa uma "linha" dentro de um Pedido: qual item do cardápio foi
 * escolhido e em qual quantidade. Um Pedido guarda uma lista de
 * ItemPedido — é a composição Pedido -> ItemPedido -> ItemCardapio.
 */
public class ItemPedido {
    private ItemCardapio item; // Referência para o ItemCardapio que foi adicionado ao pedido.
    private int quantidade;

    public ItemPedido(ItemCardapio item, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        this.item = item;
        this.quantidade = quantidade;
    }

    public double subtotal() {
        // Aplica o desconto da categoria do item (via interface
        // Descontavel) sobre o preço unitário, e só depois multiplica
        // pela quantidade — assim cada categoria (Prato, Bebida,
        // Sobremesa) contribui com seu próprio percentual de desconto.
        return item.aplicarDesconto(item.getPreco()) * quantidade;
    }

    public ItemCardapio getItem() {
        return this.item;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }
}
