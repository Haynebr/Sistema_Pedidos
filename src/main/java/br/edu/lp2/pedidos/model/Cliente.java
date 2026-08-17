package br.edu.lp2.pedidos.model;
import java.util.List;
import br.edu.lp2.pedidos.exception.PedidoVazioException;
import java.util.ArrayList;

public class Cliente {
    private String id;
    private String nome;
    private String telefone;
    private List<Pedido> pedidos = new ArrayList<>();

    public void adicionarPedido(Pedido p) {
        if(p.isEmpty()) { //Verificação simples para pedido vazio.
            throw new PedidoVazioException("Erro. Pedido vazio!");
        }
        pedidos.add(p);
    }

    public List<Pedido> getPedidos() {
        return List.copyOf(pedidos); //Aqui proíbe que faça getPedidos().clear ou getPedidos().add
    }
}
