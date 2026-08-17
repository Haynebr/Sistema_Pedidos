package br.edu.lp2.pedidos.model;

public class Sobremesa extends ItemCardapio {
    private boolean gelada;
    private int porcoes;

    @Override
    public String categoria() {
        return "Sobremesa";
    }

    @Override
    public double percentual() {
        return 15.0;
    }

}

