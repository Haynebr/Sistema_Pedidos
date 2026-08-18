package br.edu.lp2.pedidos.model;

/*
 * Item de cardápio do tipo "bebida" (ex.: refrigerante, cerveja). Além dos
 * campos comuns de ItemCardapio, guarda o volume em mililitros e se é
 * uma bebida alcoólica ou não.
 */
public class Bebida extends ItemCardapio {
    private int volumeMl;
    private boolean alcoolica;

    public Bebida(String codigo, String nome, double preco, int estoque,
                  int volumeMl, boolean alcoolica) {
        super(codigo, nome, preco, estoque);
        this.volumeMl = volumeMl;
        this.alcoolica = alcoolica;
    }

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
        // Bebida não-alcoólica tem 5% de desconto; alcoólica não tem
        // desconto (0%) — regra vinda do manual do projeto.
        if(!alcoolica) {
            return 5.0;
        }
        else {
            return 0.0;
        }
    }

    public int getVolumeMl() {
        return this.volumeMl;
    }

    public void setVolumeMl(int volumeMl) {
        this.volumeMl = volumeMl;
    }

    public boolean isAlcoolica() {
        return this.alcoolica;
    }

    public void setAlcoolica(boolean alcoolica) {
        this.alcoolica = alcoolica;
    }
}
