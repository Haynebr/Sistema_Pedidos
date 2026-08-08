package br.edu.lp2.pedidos.service;

import br.edu.lp2.pedidos.model.Cliente;
import br.edu.lp2.pedidos.model.ItemCardapio;
import br.edu.lp2.pedidos.model.ItemPedido;
import br.edu.lp2.pedidos.model.Pedido;
import br.edu.lp2.pedidos.model.StatusPedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioService {

    public double faturamentoDoDia(List<Pedido> pedidos, LocalDate data) {
        return pedidos.stream()
            .filter(p -> p.getDataHora().toLocalDate().equals(data))
            .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
            .mapToDouble(Pedido::total)
            .sum();
    }

    public double ticketMedio(List<Pedido> pedidos) {
        return pedidos.stream()
            .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
            .mapToDouble(Pedido::total)
            .average()
            .orElse(0.0);
    }

    public ItemCardapio pratoMaisVendido(List<Pedido> pedidos) {
        return pedidos.stream()
            .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
            .flatMap(p -> p.getItens().stream())
            .collect(Collectors.groupingBy(
                ItemPedido::getItem,
                Collectors.summingInt(ItemPedido::getQuantidade)))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public List<Cliente> rankingClientes(List<Cliente> clientes) {
        return clientes.stream()
            .sorted((c1, c2) -> Double.compare(
                calcularTotalCliente(c2),
                calcularTotalCliente(c1)))
            .collect(Collectors.toList());
    }

    private double calcularTotalCliente(Cliente c) {
        return c.getPedidos().stream()
            .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
            .mapToDouble(Pedido::total)
            .sum();
    }
}
