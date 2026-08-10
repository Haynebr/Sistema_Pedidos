package br.edu.lp2.pedidos.model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import br.edu.lp2.pedidos.exception.TransicaoStatusInvalidaException;

public class Pedido {
    private String id;
    private LocalDateTime datahora;
    private StatusPedido status = StatusPedido.RECEBIDO; //Estado inicial de todos os pedidos.
    private List<ItemPedido> itens = new ArrayList<>();

    public double total() {
        double somador = 0;
        for(ItemPedido item : itens) {
            somador += item.subtotal();
        }
        return somador;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public void avancarStatus() {
        if(this.status == StatusPedido.RECEBIDO) {
            status = StatusPedido.EM_PREPARO;
        }
        else if (this.status == StatusPedido.EM_PREPARO) {
            status = StatusPedido.PRONTO;
        }
        else if(this.status == StatusPedido.PRONTO) {
            status = StatusPedido.ENTREGUE;
        }
        else {
            throw new TransicaoStatusInvalidaException("Erro. Não há mais status disponíveis para transição.");
        }
    }

    public void cancelar() {
        status = StatusPedido.CANCELADO;
    }

    public boolean isEmpty() { //Classe para verificar se o pedido está vazio.
        return itens.isEmpty();
    }

}