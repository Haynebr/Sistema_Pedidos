package br.edu.lp2.pedidos.model;

public class Bebida extends ItemCardapio {
    private int volumeMl;
    private boolean alcoolica;

    @Override
    public String categoria() {
        if(alcoolica) {
            return "Bebida alcoólica";
        }
        else {
            return "Bebida não-alcoólica";
        }
    }

    @Override
    public double percentual() {
        if(!alcoolica) {
            return 5.0;
        }
        else {
            return 0.0;
        }

    }

}
