package br.edu.lp2.pedidos.model;

import br.edu.lp2.pedidos.exception.EstoqueInsuficienteException;

/*
 * Classe abstrata que representa qualquer item que pode aparecer no
 * cardápio (Prato, Bebida ou Sobremesa). Ela concentra tudo que é comum
 * às três categorias: código, nome, preço e controle de estoque.
 *
 * É "abstract" porque não faz sentido existir um ItemCardapio "genérico"
 * solto no sistema — todo item real pertence a uma categoria concreta,
 * que é quem decide o percentual de desconto (via Descontavel) e o texto
 * da categoria (via categoria()).
 */
public abstract class ItemCardapio implements Descontavel {
    // 'final' porque o código é a identidade do item: depois de criado,
    // ele nunca deve trocar (é usado, por exemplo, para religar os itens
    // do pedido com o cardápio na hora de ler o CSV).
    protected final String codigo;
    protected String nome;
    protected double preco;
    protected int estoque;

    public ItemCardapio(String codigo, String nome, double preco, int estoque) {
        // Validações no construtor: garantem que nunca existirá um item
        // "quebrado" (sem código/nome, ou com preço/estoque negativo).
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do item não pode ser vazio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do item não pode ser vazio.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        if (estoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public abstract String categoria(); //A categoria é implementada por cada tipo de produto. Por isso, abstract.

    public String getCodigo() {
        return this.codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do item não pode ser vazio.");
        }
        this.nome = nome;
    }

    public double getPreco() {
        return this.preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    public int getEstoque() {
        return this.estoque;
    }

    public boolean temEstoque(int quantidade) {
        // Só existe estoque suficiente se a quantidade pedida for positiva
        // e não ultrapassar o que está disponível.
        return quantidade > 0 && this.estoque >= quantidade;
    }

    public void baixarEstoque(int quantidade) {
        if (!temEstoque(quantidade)) { //Validação simples para estoque insuficiente.
            throw new EstoqueInsuficienteException("Estoque disponível insuficiente para \"" + nome + "\".");
        }
        this.estoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        if(quantidade <= 0) { //Validação simples para quantidades menores ou iguais a zero.
            throw new IllegalArgumentException("Impossível alterar estoque com base em valores menores ou iguais a zero!");
        }
        this.estoque += quantidade;
    }

    // equals/hashCode baseados só no código: dois itens de cardápio são
    // "o mesmo item" se têm o mesmo código, independente do resto. Isso é
    // usado, por exemplo, pelo RelatorioService para agrupar ItemPedido
    // por ItemCardapio (Collectors.groupingBy) na hora de achar o prato
    // mais vendido.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemCardapio)) {
            return false;
        }
        ItemCardapio outro = (ItemCardapio) o;
        return this.codigo.equals(outro.codigo);
    }

    @Override
    public int hashCode() {
        return this.codigo.hashCode();
    }
}
