package br.edu.lp2.pedidos.service;

import br.edu.lp2.pedidos.model.Pedido;
import br.edu.lp2.pedidos.model.ItemCardapio;
import br.edu.lp2.pedidos.model.Cliente;
import br.edu.lp2.pedidos.model.StatusPedido;
import br.edu.lp2.pedidos.exception.PedidoVazioException;
import br.edu.lp2.pedidos.exception.TransicaoStatusInvalidaException;

public class PedidoService {

    public Pedido criarPedido(Cliente cliente) {
        String id = "P" + System.currentTimeMillis(); // ou UUID.randomUUID().toString()
        Pedido pedido = new Pedido(id);
        cliente.adicionarPedido(pedido);
        return pedido;
    }

    public void adicionarItem(Pedido pedido, ItemCardapio item, int quantidade) {
        if (pedido.getStatus() != StatusPedido.RECEBIDO) {
            throw new TransicaoStatusInvalidaException(
                "Só é possível adicionar itens a pedidos com status RECEBIDO.");
        }
        item.baixarEstoque(quantidade); // já lança EstoqueInsuficienteException se faltar
        pedido.adicionarItem(item, quantidade);
    }

    public void fecharPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new PedidoVazioException(
                "Não é possível fechar um pedido sem itens.");
        }
        pedido.avancarStatus(); // RECEBIDO -> EM_PREPARO
    }

    public void alterarStatus(Pedido pedido, StatusPedido novoStatus) {
        if (novoStatus == StatusPedido.CANCELADO) {
            pedido.cancelar();
        } else {
            pedido.avancarStatus();
        }
    }
}
