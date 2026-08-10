package br.edu.lp2.pedidos.exception;

public class TransicaoStatusInvalidaException extends RuntimeException { 
    
    public TransicaoStatusInvalidaException(String mensagem) {
        super(mensagem);
    }
}
