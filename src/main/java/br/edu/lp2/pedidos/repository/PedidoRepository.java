package br.edu.lp2.pedidos.repository;

import br.edu.lp2.pedidos.model.Cliente;
import br.edu.lp2.pedidos.model.ItemCardapio;
import br.edu.lp2.pedidos.model.Pedido;

import java.util.List;

/**
 * Contrato de persistência: define O QUE o sistema precisa (salvar e
 * carregar todos os dados), sem dizer COMO isso é feito.
 *
 * PedidoRepositoryCsv vai implementar essa interface usando arquivos CSV.
 * Se um dia o professor pedir para trocar por banco de dados, cria-se uma
 * nova classe (ex.: PedidoRepositoryJdbc implements PedidoRepository) e o
 * resto do sistema (Menu, Service) não muda uma linha — porque eles
 * dependem desta interface, nunca da implementação concreta.
 * É o mesmo princípio por trás de JpaRepository no Spring Data.
 */
public interface PedidoRepository {

    void salvarTodos(List<ItemCardapio> cardapio, List<Cliente> clientes, List<Pedido> pedidos);

    DadosCarregados carregarTodos();
}
