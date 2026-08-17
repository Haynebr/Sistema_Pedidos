package br.edu.lp2.pedidos.model;

public interface Descontavel {
    
    public default double aplicarDesconto(double valorBase) { //Fórmula de aplicar desconto comum a todos os produtos.
        return (1 - (percentual()*0.01))*valorBase;
    }
    public double percentual(); //O percentual é implementado por cada produto.

}
