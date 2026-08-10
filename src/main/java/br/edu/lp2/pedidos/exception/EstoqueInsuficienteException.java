package br.edu.lp2.pedidos.exception;

public class EstoqueInsuficienteException extends RuntimeException { 
    
    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
