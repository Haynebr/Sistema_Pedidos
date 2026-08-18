package br.edu.lp2.pedidos.model;

import java.util.List;
import java.util.ArrayList;

/*
 * Representa um cliente do restaurante. Guarda os dados de cadastro e a
 * lista de todos os pedidos já feitos por ele (histórico), usada pelos
 * relatórios (ex.: ranking de clientes).
 */
public class Cliente {
    private String id;
    private String nome;
    private String telefone;
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente(String id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public void adicionarPedido(Pedido p) {
        // Aqui só registramos o pedido no histórico do cliente. A
        // validação de "pedido não pode ficar vazio" é responsabilidade
        // do PedidoService (no momento de FECHAR o pedido), não daqui —
        // um pedido recém-criado é vazio por natureza (ainda não teve
        // itens adicionados) e precisa poder ser associado ao cliente
        // desde a criação.
        pedidos.add(p);
    }

    public List<Pedido> getPedidos() {
        return List.copyOf(pedidos); //Aqui proíbe que faça getPedidos().clear ou getPedidos().add
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // equals/hashCode baseados no id: dois clientes são "o mesmo cliente"
    // se têm o mesmo id, o que facilita buscas (ex.: localizar cliente
    // pelo id digitado no menu).
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        Cliente outro = (Cliente) o;
        return this.id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
