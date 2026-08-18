package br.edu.lp2.pedidos.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import br.edu.lp2.pedidos.exception.TransicaoStatusInvalidaException;

/*
 * Representa um pedido feito por um cliente: tem um id, o momento em que
 * foi criado, um status que evolui ao longo do tempo (ciclo de vida) e a
 * lista de itens pedidos (composição com ItemPedido).
 */
public class Pedido {
    private String id;
    private LocalDateTime dataHora;
    private StatusPedido status;
    private List<ItemPedido> itens;

    // Construtor "normal": usado quando um pedido novo é criado durante a
    // execução do programa. A data/hora é sempre "agora" e o status
    // sempre começa em RECEBIDO.
    public Pedido(String id) {
        this.id = id;
        this.dataHora = LocalDateTime.now();
        this.status = StatusPedido.RECEBIDO;
        this.itens = new ArrayList<>();
    }

    // Construtor "de reconstrução": usado só pelo repository, ao ler um
    // pedido de volta do CSV. Nesse caso a data/hora e o status já
    // existiam antes (foram salvos), então eles vêm como parâmetro em vez
    // de serem recalculados.
    public Pedido(String id, LocalDateTime dataHora, StatusPedido status) {
        this.id = id;
        this.dataHora = dataHora;
        this.status = status;
        this.itens = new ArrayList<>();
    }

    public double total() {
        double somador = 0;
        for(ItemPedido item : itens) {
            somador += item.subtotal();
        }
        return somador;
    }

    // Adiciona um item do cardápio ao pedido. Se o mesmo item (mesmo
    // código) já estiver na lista, apenas soma a quantidade em vez de
    // criar uma linha duplicada — assim "2x Coca-Cola" fica numa única
    // linha do pedido, e não em duas.
    public void adicionarItem(ItemCardapio item, int quantidade) {
        for (ItemPedido itemPedido : itens) {
            if (itemPedido.getItem().getCodigo().equals(item.getCodigo())) {
                itemPedido.setQuantidade(itemPedido.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new ItemPedido(item, quantidade));
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
        // Só é permitido cancelar um pedido que ainda não começou a ser
        // preparado ou que já foi recebido — depois de PRONTO/ENTREGUE
        // não faz sentido cancelar.
        if (status == StatusPedido.RECEBIDO || status == StatusPedido.EM_PREPARO) {
            status = StatusPedido.CANCELADO;
        } else {
            throw new TransicaoStatusInvalidaException(
                "Não é possível cancelar um pedido com status " + status + ".");
        }
    }

    public boolean isEmpty() { //Verifica se o pedido está vazio (sem itens).
        return itens.isEmpty();
    }

    public String getId() {
        return this.id;
    }

    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    public StatusPedido getStatus() {
        return this.status;
    }

    public List<ItemPedido> getItens() {
        return this.itens;
    }
}
