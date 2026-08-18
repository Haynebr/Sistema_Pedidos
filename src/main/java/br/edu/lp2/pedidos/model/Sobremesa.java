package br.edu.lp2.pedidos.model;

/*
 * Item de cardápio do tipo "sobremesa" (ex.: pudim, sorvete). Guarda se é
 * servida gelada e em quantas porções vem.
 */
public class Sobremesa extends ItemCardapio {
    private boolean gelada;
    private int porcoes;

    public Sobremesa(String codigo, String nome, double preco, int estoque,
                      boolean gelada, int porcoes) {
        super(codigo, nome, preco, estoque);
        if (porcoes <= 0) {
            throw new IllegalArgumentException("Número de porções deve ser maior que zero.");
        }
        this.gelada = gelada;
        this.porcoes = porcoes;
    }

    @Override
    public String categoria() {
        return "Sobremesa";
    }

    @Override
    public double percentual() {
        return 15.0; // Sobremesa tem o maior desconto do cardápio: 15%.
    }

    public boolean isGelada() {
        return this.gelada;
    }

    public void setGelada(boolean gelada) {
        this.gelada = gelada;
    }

    public int getPorcoes() {
        return this.porcoes;
    }

    public void setPorcoes(int porcoes) {
        if (porcoes <= 0) {
            throw new IllegalArgumentException("Número de porções deve ser maior que zero.");
        }
        this.porcoes = porcoes;
    }
}
