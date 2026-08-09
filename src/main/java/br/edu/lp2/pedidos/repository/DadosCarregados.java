package br.edu.lp2.pedidos.repository;

import br.edu.lp2.pedidos.model.Cliente;
import br.edu.lp2.pedidos.model.ItemCardapio;
import br.edu.lp2.pedidos.model.Pedido;

import java.util.List;

/**
 * DTO (Data Transfer Object): não tem lógica, só empacota as três listas
 * reconstruídas dos arquivos CSV para devolver ao Menu de uma vez só.
 *
 * Sem essa classe, carregarTodos() precisaria retornar 3 valores separados,
 * e Java não permite múltiplos retornos em um método.
 */
public class DadosCarregados {

    // 'final' aqui significa: uma vez atribuído no construtor, a referência
    // não pode ser trocada por outra lista. Isso não é opcional por estilo —
    // é o que torna esse objeto imutável, que é a intenção de um DTO:
    // ele representa uma "foto" fixa do que foi lido do disco.
    private final List<ItemCardapio> cardapio;
    private final List<Cliente> clientes;
    private final List<Pedido> pedidos;

    public DadosCarregados(List<ItemCardapio> cardapio, List<Cliente> clientes, List<Pedido> pedidos) {
        this.cardapio = cardapio;
        this.clientes = clientes;
        this.pedidos = pedidos;
    }

    public List<ItemCardapio> getCardapio() {
        return cardapio;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
