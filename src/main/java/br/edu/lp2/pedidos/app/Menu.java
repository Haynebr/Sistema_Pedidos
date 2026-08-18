package br.edu.lp2.pedidos.app;

import br.edu.lp2.pedidos.exception.EstoqueInsuficienteException;
import br.edu.lp2.pedidos.exception.PedidoVazioException;
import br.edu.lp2.pedidos.exception.TransicaoStatusInvalidaException;
import br.edu.lp2.pedidos.model.Bebida;
import br.edu.lp2.pedidos.model.Cliente;
import br.edu.lp2.pedidos.model.ItemCardapio;
import br.edu.lp2.pedidos.model.ItemPedido;
import br.edu.lp2.pedidos.model.Pedido;
import br.edu.lp2.pedidos.model.Prato;
import br.edu.lp2.pedidos.model.Sobremesa;
import br.edu.lp2.pedidos.model.StatusPedido;
import br.edu.lp2.pedidos.repository.DadosCarregados;
import br.edu.lp2.pedidos.repository.PedidoRepository;
import br.edu.lp2.pedidos.repository.PedidoRepositoryCsv;
import br.edu.lp2.pedidos.service.PedidoService;
import br.edu.lp2.pedidos.service.RelatorioService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Camada de entrada do sistema: lê a escolha do usuário no terminal e
 * chama as camadas de baixo (repository/service) para executar de fato
 * cada ação. É aqui que os dados carregados do CSV ficam em memória
 * durante toda a execução do programa, e é aqui que eles voltam pro
 * disco quando o usuário escolhe sair (opção 0).
 */
public class Menu {

    // Scanner(System.in) é o objeto que lê o que o usuário digita no
    // console. Um só é criado aqui e reaproveitado em todo o menu — abrir
    // várias instâncias sobre o mesmo System.in causa comportamento
    // inconsistente de leitura.
    private final Scanner scanner = new Scanner(System.in);

    // As três camadas de baixo que o Menu usa: persistência (repository)
    // e regras de negócio (services). O Menu não sabe COMO elas fazem o
    // trabalho, só chama os métodos delas — é o mesmo princípio de
    // "separação de camadas" usado em uma API REST (Controller chamando
    // Service, sem conhecer os detalhes internos).
    private final PedidoRepository repository = new PedidoRepositoryCsv();
    private final PedidoService pedidoService = new PedidoService();
    private final RelatorioService relatorioService = new RelatorioService();

    // Listas em memória: tudo que o programa manipula durante a execução
    // fica aqui. São carregadas do CSV no início e salvas de volta no
    // final (opção 0 do menu).
    private List<ItemCardapio> cardapio;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void iniciar() {
        DadosCarregados dados = repository.carregarTodos();
        cardapio = dados.getCardapio();
        clientes = dados.getClientes();
        pedidos = dados.getPedidos();

        System.out.println("Dados carregados: " + cardapio.size() + " itens de cardápio, "
                + clientes.size() + " clientes, " + pedidos.size() + " pedidos.");

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
                    repository.salvarTodos(cardapio, clientes, pedidos);
                    System.out.println("Dados salvos. Encerrando...");
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

    // ---------------------------------------------------------------
    // 1 - Gerenciar Cardápio
    // ---------------------------------------------------------------
    private void gerenciarCardapio() {
        System.out.println();
        System.out.println("--- Gerenciar Cardápio ---");
        System.out.println("1 - Cadastrar Prato");
        System.out.println("2 - Cadastrar Bebida");
        System.out.println("3 - Cadastrar Sobremesa");
        System.out.println("4 - Listar Cardápio");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        try {
            switch (opcao) {
                case "1":
                    cadastrarPrato();
                    break;
                case "2":
                    cadastrarBebida();
                    break;
                case "3":
                    cadastrarSobremesa();
                    break;
                case "4":
                    listarCardapio();
                    break;
                default:
                    // volta pro menu principal sem fazer nada
            }
        } catch (IllegalArgumentException e) {
            // Os construtores/setters do model validam os dados (ex.:
            // preço negativo) lançando IllegalArgumentException.
            System.out.println("[ERRO DE CADASTRO] " + e.getMessage());
        }
    }

    private void cadastrarPrato() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = Double.parseDouble(scanner.nextLine());
        System.out.print("Estoque inicial: ");
        int estoque = Integer.parseInt(scanner.nextLine());
        System.out.print("Ingredientes: ");
        String ingredientes = scanner.nextLine();
        System.out.print("Tempo de preparo (minutos): ");
        int tempoPreparo = Integer.parseInt(scanner.nextLine());

        Prato prato = new Prato(codigo, nome, preco, estoque, ingredientes, tempoPreparo);
        cardapio.add(prato);
        System.out.println("Prato cadastrado com sucesso!");
    }

    private void cadastrarBebida() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = Double.parseDouble(scanner.nextLine());
        System.out.print("Estoque inicial: ");
        int estoque = Integer.parseInt(scanner.nextLine());
        System.out.print("Volume (ml): ");
        int volumeMl = Integer.parseInt(scanner.nextLine());
        System.out.print("É alcoólica? (s/n): ");
        boolean alcoolica = scanner.nextLine().trim().equalsIgnoreCase("s");

        Bebida bebida = new Bebida(codigo, nome, preco, estoque, volumeMl, alcoolica);
        cardapio.add(bebida);
        System.out.println("Bebida cadastrada com sucesso!");
    }

    private void cadastrarSobremesa() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = Double.parseDouble(scanner.nextLine());
        System.out.print("Estoque inicial: ");
        int estoque = Integer.parseInt(scanner.nextLine());
        System.out.print("É gelada? (s/n): ");
        boolean gelada = scanner.nextLine().trim().equalsIgnoreCase("s");
        System.out.print("Porções: ");
        int porcoes = Integer.parseInt(scanner.nextLine());

        Sobremesa sobremesa = new Sobremesa(codigo, nome, preco, estoque, gelada, porcoes);
        cardapio.add(sobremesa);
        System.out.println("Sobremesa cadastrada com sucesso!");
    }

    private void listarCardapio() {
        System.out.println();
        System.out.println("--- Cardápio Atual ---");
        if (cardapio.isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
            return;
        }
        for (ItemCardapio item : cardapio) {
            System.out.printf("[%s] %s - %s - R$ %.2f - Estoque: %d%n",
                    item.getCodigo(), item.categoria(), item.getNome(), item.getPreco(), item.getEstoque());
        }
    }

    // ---------------------------------------------------------------
    // 2 - Gerenciar Clientes
    // ---------------------------------------------------------------
    private void gerenciarClientes() {
        System.out.println();
        System.out.println("--- Gerenciar Clientes ---");
        System.out.println("1 - Cadastrar Cliente");
        System.out.println("2 - Listar Clientes");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                cadastrarCliente();
                break;
            case "2":
                listarClientes();
                break;
            default:
                // volta pro menu principal
        }
    }

    private void cadastrarCliente() {
        System.out.print("Id do cliente: ");
        String id = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        clientes.add(new Cliente(id, nome, telefone));
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void listarClientes() {
        System.out.println();
        System.out.println("--- Clientes Cadastrados ---");
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.printf("[%s] %s - %s%n", cliente.getId(), cliente.getNome(), cliente.getTelefone());
        }
    }

    // ---------------------------------------------------------------
    // 3 - Criar Novo Pedido
    // ---------------------------------------------------------------
    private void criarPedido() {
        System.out.print("Id do cliente: ");
        String idCliente = scanner.nextLine();
        Cliente cliente = buscarClientePorId(idCliente);

        if (cliente == null) {
            System.out.println("[ERRO] Cliente não encontrado.");
            return;
        }

        Pedido pedido = pedidoService.criarPedido(cliente);
        pedidos.add(pedido);
        System.out.println("Pedido criado com sucesso! Id do pedido: " + pedido.getId());
    }

    // ---------------------------------------------------------------
    // 4 - Adicionar Item ao Pedido
    // ---------------------------------------------------------------
    private void adicionarItemAoPedido() {
        System.out.print("Id do pedido: ");
        String idPedido = scanner.nextLine();
        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            System.out.println("[ERRO] Pedido não encontrado.");
            return;
        }

        System.out.print("Código do item do cardápio: ");
        String codigoItem = scanner.nextLine();
        ItemCardapio item = buscarItemPorCodigo(codigoItem);

        if (item == null) {
            System.out.println("[ERRO] Item de cardápio não encontrado.");
            return;
        }

        System.out.print("Quantidade: ");
        int quantidade;
        try {
            quantidade = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Quantidade inválida.");
            return;
        }

        // Bloco try-catch conforme especificado no manual: cada exceção
        // customizada tem sua própria mensagem de erro amigável, e
        // qualquer outra exceção inesperada cai no catch genérico.
        try {
            pedidoService.adicionarItem(pedido, item, quantidade);
            System.out.println("Item adicionado ao pedido com sucesso!");
        } catch (EstoqueInsuficienteException e) {
            System.out.println("[ERRO DE ESTOQUE] " + e.getMessage());
        } catch (PedidoVazioException e) {
            System.out.println("[ERRO DE PEDIDO] " + e.getMessage());
        } catch (TransicaoStatusInvalidaException e) {
            System.out.println("[ERRO DE STATUS] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERRO INESPERADO] " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 5 - Fechar Pedido / Alterar Status
    // ---------------------------------------------------------------
    private void fecharOuAlterarStatus() {
        System.out.print("Id do pedido: ");
        String idPedido = scanner.nextLine();
        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            System.out.println("[ERRO] Pedido não encontrado.");
            return;
        }

        System.out.println("Status atual: " + pedido.getStatus());
        System.out.println("1 - Fechar Pedido (RECEBIDO -> EM_PREPARO)");
        System.out.println("2 - Avançar Status");
        System.out.println("3 - Cancelar Pedido");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();

        try {
            switch (opcao) {
                case "1":
                    pedidoService.fecharPedido(pedido);
                    System.out.println("Pedido fechado! Novo status: " + pedido.getStatus());
                    break;
                case "2":
                    // O argumento passado aqui só importa para diferenciar
                    // CANCELADO das demais opções (regra de alterarStatus
                    // no PedidoService) — para avançar, qualquer status
                    // diferente de CANCELADO serve.
                    pedidoService.alterarStatus(pedido, StatusPedido.EM_PREPARO);
                    System.out.println("Status avançado! Novo status: " + pedido.getStatus());
                    break;
                case "3":
                    pedidoService.alterarStatus(pedido, StatusPedido.CANCELADO);
                    System.out.println("Pedido cancelado!");
                    break;
                default:
                    return;
            }
            // Emite o comprovante sempre que uma transição de status dá
            // certo, para o usuário acompanhar a situação do pedido.
            emitirComprovante(pedido);
        } catch (EstoqueInsuficienteException e) {
            System.out.println("[ERRO DE ESTOQUE] " + e.getMessage());
        } catch (PedidoVazioException e) {
            System.out.println("[ERRO DE PEDIDO] " + e.getMessage());
        } catch (TransicaoStatusInvalidaException e) {
            System.out.println("[ERRO DE STATUS] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERRO INESPERADO] " + e.getMessage());
        }
    }

    // Monta e imprime o comprovante do pedido no console: cabeçalho com
    // id/data/cliente, uma linha por item (com desconto aplicado) e o
    // total geral formatado como moeda brasileira.
    private void emitirComprovante(Pedido pedido) {
        Cliente donoDoPedido = buscarClienteDoPedido(pedido);

        System.out.println();
        System.out.println("========== COMPROVANTE ==========");
        System.out.println("Pedido: " + pedido.getId());
        System.out.println("Data/Hora: " + pedido.getDataHora().format(FORMATO_DATA));
        if (donoDoPedido != null) {
            System.out.println("Cliente: " + donoDoPedido.getNome() + " (" + donoDoPedido.getTelefone() + ")");
        }
        System.out.println("Status: " + pedido.getStatus());
        System.out.println("----------------------------------");

        for (ItemPedido itemPedido : pedido.getItens()) {
            ItemCardapio item = itemPedido.getItem();
            double percentualDesconto = item.percentual();
            System.out.printf("%2dx %-20s R$ %8.2f (un.) - desconto %.0f%% - subtotal R$ %8.2f%n",
                    itemPedido.getQuantidade(), item.getNome(), item.getPreco(),
                    percentualDesconto, itemPedido.subtotal());
        }

        System.out.println("----------------------------------");
        System.out.printf("TOTAL DO PEDIDO: R$ %.2f%n", pedido.total());
        System.out.println("==================================");
    }

    // ---------------------------------------------------------------
    // 6 - Exibir Relatórios Gerenciais
    // ---------------------------------------------------------------
    private void exibirRelatorios() {
        System.out.println();
        System.out.println("--- Relatórios Gerenciais ---");

        double faturamentoHoje = relatorioService.faturamentoDoDia(pedidos, LocalDate.now());
        System.out.printf("Faturamento de hoje: R$ %.2f%n", faturamentoHoje);

        double ticketMedio = relatorioService.ticketMedio(pedidos);
        System.out.printf("Ticket médio: R$ %.2f%n", ticketMedio);

        ItemCardapio maisVendido = relatorioService.pratoMaisVendido(pedidos);
        if (maisVendido != null) {
            System.out.println("Item mais vendido: " + maisVendido.getNome());
        } else {
            System.out.println("Item mais vendido: nenhum pedido registrado ainda.");
        }

        System.out.println("Ranking de clientes (do que mais gastou para o que menos gastou):");
        List<Cliente> ranking = relatorioService.rankingClientes(clientes);
        int posicao = 1;
        for (Cliente cliente : ranking) {
            System.out.println("  " + posicao + "º - " + cliente.getNome());
            posicao++;
        }
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares de busca (usados em várias opções do menu)
    // ---------------------------------------------------------------
    private Cliente buscarClientePorId(String id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    private Pedido buscarPedidoPorId(String id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id)) {
                return pedido;
            }
        }
        return null;
    }

    private ItemCardapio buscarItemPorCodigo(String codigo) {
        for (ItemCardapio item : cardapio) {
            if (item.getCodigo().equals(codigo)) {
                return item;
            }
        }
        return null;
    }

    private Cliente buscarClienteDoPedido(Pedido pedido) {
        for (Cliente cliente : clientes) {
            if (cliente.getPedidos().contains(pedido)) {
                return cliente;
            }
        }
        return null;
    }
}
