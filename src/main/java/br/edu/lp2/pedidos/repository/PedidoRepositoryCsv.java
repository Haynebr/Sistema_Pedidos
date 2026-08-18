package br.edu.lp2.pedidos.repository;

import br.edu.lp2.pedidos.model.Bebida;
import br.edu.lp2.pedidos.model.Cliente;
import br.edu.lp2.pedidos.model.ItemCardapio;
import br.edu.lp2.pedidos.model.ItemPedido;
import br.edu.lp2.pedidos.model.Pedido;
import br.edu.lp2.pedidos.model.Prato;
import br.edu.lp2.pedidos.model.Sobremesa;
import br.edu.lp2.pedidos.model.StatusPedido;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Implementação de PedidoRepository que grava e lê os dados em três
 * arquivos de texto simples (CSV, separados por ';'), usando apenas
 * BufferedReader/BufferedWriter — sem nenhuma biblioteca externa, como
 * pede o manual do projeto.
 *
 * Formatos usados (um registro por linha):
 *   cardapio.csv:
 *     PRATO;codigo;nome;preco;estoque;ingredientes;tempoPreparo
 *     BEBIDA;codigo;nome;preco;estoque;volumeMl;alcoolica
 *     SOBREMESA;codigo;nome;preco;estoque;gelada;porcoes
 *   clientes.csv:
 *     id;nome;telefone
 *   pedidos.csv:
 *     idPedido;idCliente;dataHora;status;codigo1:qtd1,codigo2:qtd2,...
 */
public class PedidoRepositoryCsv implements PedidoRepository {

    // Pasta e nomes dos arquivos ficam centralizados aqui em cima: se um
    // dia precisar mudar o local dos dados, só se mexe nesta linha.
    private static final String PASTA_DADOS = "data";
    private static final String ARQUIVO_CARDAPIO = PASTA_DADOS + File.separator + "cardapio.csv";
    private static final String ARQUIVO_CLIENTES = PASTA_DADOS + File.separator + "clientes.csv";
    private static final String ARQUIVO_PEDIDOS = PASTA_DADOS + File.separator + "pedidos.csv";

    private static final String SEP = ";";

    @Override
    public void salvarTodos(List<ItemCardapio> cardapio, List<Cliente> clientes, List<Pedido> pedidos) {
        // Garante que a pasta "data" existe antes de tentar escrever nela
        // (por exemplo, na primeiríssima execução do programa).
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        salvarCardapio(cardapio);
        salvarClientes(clientes);
        salvarPedidos(clientes);
    }

    private void salvarCardapio(List<ItemCardapio> cardapio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CARDAPIO))) {
            for (ItemCardapio item : cardapio) {
                StringBuilder linha = new StringBuilder();

                // instanceof decide qual "formato" de linha usar, porque
                // cada categoria tem colunas extras diferentes.
                if (item instanceof Prato) {
                    Prato prato = (Prato) item;
                    linha.append("PRATO").append(SEP)
                         .append(prato.getCodigo()).append(SEP)
                         .append(prato.getNome()).append(SEP)
                         .append(prato.getPreco()).append(SEP)
                         .append(prato.getEstoque()).append(SEP)
                         .append(prato.getIngredientes()).append(SEP)
                         .append(prato.getTempoPreparo());
                } else if (item instanceof Bebida) {
                    Bebida bebida = (Bebida) item;
                    linha.append("BEBIDA").append(SEP)
                         .append(bebida.getCodigo()).append(SEP)
                         .append(bebida.getNome()).append(SEP)
                         .append(bebida.getPreco()).append(SEP)
                         .append(bebida.getEstoque()).append(SEP)
                         .append(bebida.getVolumeMl()).append(SEP)
                         .append(bebida.isAlcoolica());
                } else if (item instanceof Sobremesa) {
                    Sobremesa sobremesa = (Sobremesa) item;
                    linha.append("SOBREMESA").append(SEP)
                         .append(sobremesa.getCodigo()).append(SEP)
                         .append(sobremesa.getNome()).append(SEP)
                         .append(sobremesa.getPreco()).append(SEP)
                         .append(sobremesa.getEstoque()).append(SEP)
                         .append(sobremesa.isGelada()).append(SEP)
                         .append(sobremesa.getPorcoes());
                }

                writer.write(linha.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar cardapio.csv: " + e.getMessage(), e);
        }
    }

    private void salvarClientes(List<Cliente> clientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CLIENTES))) {
            for (Cliente cliente : clientes) {
                writer.write(cliente.getId() + SEP + cliente.getNome() + SEP + cliente.getTelefone());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar clientes.csv: " + e.getMessage(), e);
        }
    }

    private void salvarPedidos(List<Cliente> clientes) {
        // Os pedidos não guardam uma referência direta para o Cliente
        // (o Manual não prevê esse atributo em Pedido), então o jeito de
        // descobrir "de quem é cada pedido" é percorrer a lista de
        // pedidos de cada cliente.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PEDIDOS))) {
            for (Cliente cliente : clientes) {
                for (Pedido pedido : cliente.getPedidos()) {
                    StringBuilder itensTexto = new StringBuilder();
                    List<ItemPedido> itens = pedido.getItens();
                    for (int i = 0; i < itens.size(); i++) {
                        ItemPedido itemPedido = itens.get(i);
                        itensTexto.append(itemPedido.getItem().getCodigo())
                                  .append(":")
                                  .append(itemPedido.getQuantidade());
                        if (i < itens.size() - 1) {
                            itensTexto.append(",");
                        }
                    }

                    String linha = pedido.getId() + SEP
                            + cliente.getId() + SEP
                            + pedido.getDataHora() + SEP
                            + pedido.getStatus() + SEP
                            + itensTexto;
                    writer.write(linha);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar pedidos.csv: " + e.getMessage(), e);
        }
    }

    @Override
    public DadosCarregados carregarTodos() {
        // Ordem de leitura importa: pedidos.csv referencia clientes e
        // itens de cardápio pelo código/id, então cardápio e clientes
        // precisam já estar carregados em memória antes de reconstruir
        // os pedidos.
        List<ItemCardapio> cardapio = carregarCardapio();
        List<Cliente> clientes = carregarClientes();
        List<Pedido> pedidos = carregarPedidos(cardapio, clientes);

        return new DadosCarregados(cardapio, clientes, pedidos);
    }

    private List<ItemCardapio> carregarCardapio() {
        List<ItemCardapio> cardapio = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CARDAPIO);
        if (!arquivo.exists()) {
            // Primeira execução do programa: ainda não existe arquivo
            // salvo. Devolve lista vazia em vez de dar erro.
            return cardapio;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(SEP, -1);
                String tipo = campos[0];
                String codigo = campos[1];
                String nome = campos[2];
                double preco = Double.parseDouble(campos[3]);
                int estoque = Integer.parseInt(campos[4]);

                if (tipo.equals("PRATO")) {
                    String ingredientes = campos[5];
                    int tempoPreparo = Integer.parseInt(campos[6]);
                    cardapio.add(new Prato(codigo, nome, preco, estoque, ingredientes, tempoPreparo));
                } else if (tipo.equals("BEBIDA")) {
                    int volumeMl = Integer.parseInt(campos[5]);
                    boolean alcoolica = Boolean.parseBoolean(campos[6]);
                    cardapio.add(new Bebida(codigo, nome, preco, estoque, volumeMl, alcoolica));
                } else if (tipo.equals("SOBREMESA")) {
                    boolean gelada = Boolean.parseBoolean(campos[5]);
                    int porcoes = Integer.parseInt(campos[6]);
                    cardapio.add(new Sobremesa(codigo, nome, preco, estoque, gelada, porcoes));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler cardapio.csv: " + e.getMessage(), e);
        }

        return cardapio;
    }

    private List<Cliente> carregarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CLIENTES);
        if (!arquivo.exists()) {
            return clientes;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(SEP, -1);
                String id = campos[0];
                String nome = campos[1];
                String telefone = campos[2];
                clientes.add(new Cliente(id, nome, telefone));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler clientes.csv: " + e.getMessage(), e);
        }

        return clientes;
    }

    private List<Pedido> carregarPedidos(List<ItemCardapio> cardapio, List<Cliente> clientes) {
        List<Pedido> pedidos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PEDIDOS);
        if (!arquivo.exists()) {
            return pedidos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                // limit -1 preserva o último campo mesmo se ele vier
                // vazio (pedido sem nenhum item, caso raro, mas possível
                // se o pedido tiver sido cancelado ainda vazio).
                String[] campos = linha.split(SEP, -1);
                String idPedido = campos[0];
                String idCliente = campos[1];
                LocalDateTime dataHora = LocalDateTime.parse(campos[2]);
                StatusPedido status = StatusPedido.valueOf(campos[3]);
                String itensTexto = campos.length > 4 ? campos[4] : "";

                Pedido pedido = new Pedido(idPedido, dataHora, status);

                if (!itensTexto.trim().isEmpty()) {
                    String[] pares = itensTexto.split(",");
                    for (String par : pares) {
                        String[] partes = par.split(":");
                        String codigoItem = partes[0];
                        int quantidade = Integer.parseInt(partes[1]);

                        ItemCardapio itemEncontrado = buscarPorCodigo(cardapio, codigoItem);
                        if (itemEncontrado != null) {
                            pedido.adicionarItem(itemEncontrado, quantidade);
                        }
                    }
                }

                // Encontra o cliente dono do pedido e faz a ligação nos
                // dois sentidos: cliente -> pedido (histórico) e
                // pedido -> lista global usada pelos relatórios.
                Cliente clienteDoPedido = buscarClientePorId(clientes, idCliente);
                if (clienteDoPedido != null) {
                    clienteDoPedido.adicionarPedido(pedido);
                }
                pedidos.add(pedido);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler pedidos.csv: " + e.getMessage(), e);
        }

        return pedidos;
    }

    private ItemCardapio buscarPorCodigo(List<ItemCardapio> cardapio, String codigo) {
        for (ItemCardapio item : cardapio) {
            if (item.getCodigo().equals(codigo)) {
                return item;
            }
        }
        return null;
    }

    private Cliente buscarClientePorId(List<Cliente> clientes, String id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }
}
