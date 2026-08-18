package br.edu.lp2.pedidos.model;

/*
 * Item de cardápio do tipo "prato" (ex.: lasanha, feijoada). Herda de
 * ItemCardapio os campos comuns (código, nome, preço, estoque) e adiciona
 * dados específicos de um prato: ingredientes e tempo de preparo.
 */
public class Prato extends ItemCardapio {
    private String ingredientes;
    private int tempoPreparo;

    public Prato(String codigo, String nome, double preco, int estoque,
                 String ingredientes, int tempoPreparo) {
        super(codigo, nome, preco, estoque); // reaproveita a validação da classe mãe
        this.ingredientes = ingredientes;
        this.tempoPreparo = tempoPreparo;
    }

    @Override
    public String categoria() {
        return "Prato";
    }

    @Override
    public double percentual() {
        // Convenção usada neste projeto: percentual() devolve o valor já
        // em "porcentagem" (10.0 = 10%), e Descontavel.aplicarDesconto()
        // é quem divide por 100. Prato dá 10% de desconto.
        return 10.0;
    }

    public String getIngredientes() {
        return this.ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getTempoPreparo() {
        return this.tempoPreparo;
    }

    public void setTempoPreparo(int tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }
}
