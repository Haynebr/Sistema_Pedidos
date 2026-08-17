package br.edu.lp2.pedidos.model;

public class Prato extends ItemCardapio {
    private String ingredientes;
    private int tempoPreparo;

    @Override
    public String categoria() {
        return "Prato";
    }

    @Override
    public double percentual() {
        return 10.0;
    }

}
