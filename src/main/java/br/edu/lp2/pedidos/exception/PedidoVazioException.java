package br.edu.lp2.pedidos.exception;

public class PedidoVazioException extends RuntimeException { 
    
    public PedidoVazioException(String mensagem) {
        super(mensagem);
    }
}