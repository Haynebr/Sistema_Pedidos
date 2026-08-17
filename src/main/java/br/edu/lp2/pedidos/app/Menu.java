package br.edu.lp2.pedidos.app;

import java.util.Scanner;

/**
 * Camada de entrada do sistema: lê a escolha do usuário no terminal e
 * decide qual ação disparar. Nesta fase, cada opção só imprime um aviso —
 * a ligação de verdade com PedidoService/RelatorioService entra na Fase 5,
 * quando o model já estiver pronto.
 */
public class Menu {

    // Scanner(System.in) é o objeto que lê o que o usuário digita no
    // console. Um só é criado aqui e reaproveitado em todo o menu — abrir
    // várias instâncias sobre o mesmo System.in causa comportamento
    // inconsistente de leitura.
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        String opcao = "";

        // Loop principal: repete até o usuário digitar "0".
        // Compare com Python: aqui não existe "while True: ... break",
        // a condição de parada fica explícita na própria linha do while.
        while (!opcao.equals("0")) {
            exibirOpcoes();
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    gerenciarCardapio();
                    break;
                case "2":
                    gerenciarClientes();
                    break;
                case "3":
                    criarPedido();
                    break;
                case "4":
                    adicionarItemAoPedido();
                    break;
                case "5":
                    fecharOuAlterarStatus();
                    break;
                case "6":
                    exibirRelatorios();
                    break;
                case "0":
                    System.out.println("Salvando e saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirOpcoes() {
        System.out.println();
        System.out.println("=== Sistema de Pedidos ===");
        System.out.println("1 - Gerenciar Cardápio (Cadastrar/Listar)");
        System.out.println("2 - Gerenciar Clientes (Cadastrar/Listar)");
        System.out.println("3 - Criar Novo Pedido");
        System.out.println("4 - Adicionar Item ao Pedido");
        System.out.println("5 - Fechar Pedido / Alterar Status");
        System.out.println("6 - Exibir Relatórios Gerenciais");
        System.out.println("0 - Sair e Salvar");
        System.out.print("Escolha uma opção: ");
    }

    // Cada opção do menu vira um método próprio — mesmo vazio por
    // enquanto. Isso mantém iniciar() legível (o "roteiro" do menu) e dá
    // exatamente o lugar onde o código real vai entrar na Fase 5, sem
    // precisar reestruturar o switch depois.
    private void gerenciarCardapio() {
        System.out.println("[EM CONSTRUÇÃO] Gerenciar Cardápio");
    }

    private void gerenciarClientes() {
        System.out.println("[EM CONSTRUÇÃO] Gerenciar Clientes");
    }

    private void criarPedido() {
        System.out.println("[EM CONSTRUÇÃO] Criar Novo Pedido");
    }

    private void adicionarItemAoPedido() {
        System.out.println("[EM CONSTRUÇÃO] Adicionar Item ao Pedido");
    }

    private void fecharOuAlterarStatus() {
        System.out.println("[EM CONSTRUÇÃO] Fechar Pedido / Alterar Status");
    }

    private void exibirRelatorios() {
        System.out.println("[EM CONSTRUÇÃO] Exibir Relatórios Gerenciais");
    }
}
