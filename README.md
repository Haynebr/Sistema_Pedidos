# Sistema de Pedidos

Aplicação de console em Java para gestão de pedidos de um restaurante/delivery
(cardápio, clientes, pedidos e relatórios gerenciais), com persistência em
arquivos CSV. Projeto da disciplina **Linguagem de Programação II**.

## Equipe e divisão do trabalho

| Frente | Pacotes | Responsável | Conteúdo |
|---|---|---|---|
| F01 | `model`, `exception` | Andrey Basilio | Entidades do domínio (`ItemCardapio`, `Prato`, `Bebida`, `Sobremesa`, `Cliente`, `Pedido`, `ItemPedido`), interface `Descontavel`, enum `StatusPedido` e as 3 exceções customizadas. |
| F02 | `service` | Arthur Ronald | Regras de negócio em `PedidoService` (criar pedido, adicionar item, fechar pedido, transição de status) e relatórios com Java Streams em `RelatorioService`. |
| F03 | `repository`, `app` | Hayne Rene | Persistência em CSV (`PedidoRepositoryCsv`) e menu interativo de console (`Menu`). |

## Status do projeto

**Completo e funcional.** Todas as camadas foram integradas: o menu chama os
serviços de verdade, o repository lê e grava os três arquivos CSV, os erros
de negócio são tratados com try/catch das exceções customizadas, e o
programa roda de ponta a ponta com os dados de exemplo em `data/`.

Durante a integração final, o `model` (F01) precisou ganhar métodos que o
`service` (F02) já esperava (getters, construtores, `adicionarItem`, etc.) —
eles foram implementados seguindo exatamente as assinaturas descritas no
Manual do projeto, sem alterar as regras de negócio que o Andrey já havia
escrito. Também foi corrigido um bug em `Cliente.adicionarPedido`, que
lançava `PedidoVazioException` para todo pedido recém-criado (impedindo
`PedidoService.criarPedido` de funcionar) — essa validação de "pedido vazio"
já é feita corretamente em `PedidoService.fecharPedido`, então foi removida
da duplicata no model.

## Como rodar

Requer **JDK 26** e **Maven** (testado no IntelliJ IDEA Community, macOS).

```bash
mvn clean compile
mvn -q exec:java -Dexec.mainClass="br.edu.lp2.pedidos.Main"
```

Ou compile e rode direto com `java`:

```bash
mvn clean package -DskipTests
java -cp target/classes br.edu.lp2.pedidos.Main
```

Ou rode a classe `Main` diretamente pela IDE (o diretório de execução deve
ser a raiz do projeto — `Codigo/Sistema_Pedidos` — para que o programa
encontre a pasta `data/`).

## Estrutura de pastas

```
Sistema_Pedidos/
├── data/                     # arquivos CSV (dados persistidos)
│   ├── cardapio.csv
│   ├── clientes.csv
│   └── pedidos.csv
├── pom.xml
└── src/main/java/br/edu/lp2/pedidos/
    ├── Main.java              # ponto de entrada
    ├── app/
    │   └── Menu.java           # menu de console (CLI)
    ├── model/                  # entidades do domínio (F01)
    │   ├── Descontavel.java
    │   ├── ItemCardapio.java
    │   ├── Prato.java / Bebida.java / Sobremesa.java
    │   ├── StatusPedido.java
    │   ├── Cliente.java
    │   ├── ItemPedido.java
    │   └── Pedido.java
    ├── service/                # regras de negócio (F02)
    │   ├── PedidoService.java
    │   └── RelatorioService.java
    ├── repository/             # persistência em CSV (F03)
    │   ├── DadosCarregados.java
    │   ├── PedidoRepository.java
    │   └── PedidoRepositoryCsv.java
    └── exception/               # exceções customizadas
        ├── EstoqueInsuficienteException.java
        ├── PedidoVazioException.java
        └── TransicaoStatusInvalidaException.java
```

## Como usar o menu

Ao iniciar, o programa carrega automaticamente `data/cardapio.csv`,
`data/clientes.csv` e `data/pedidos.csv` para a memória. Todas as opções do
menu abaixo trabalham sobre essas listas em memória; nada é gravado em
disco até a opção **0**.

```
=== Sistema de Pedidos ===
1 - Gerenciar Cardápio (Cadastrar/Listar)
2 - Gerenciar Clientes (Cadastrar/Listar)
3 - Criar Novo Pedido
4 - Adicionar Item ao Pedido
5 - Fechar Pedido / Alterar Status
6 - Exibir Relatórios Gerenciais
0 - Sair e Salvar
```

- **1 - Gerenciar Cardápio**: cadastra um Prato, Bebida ou Sobremesa (pede os
  dados específicos de cada categoria), ou lista o cardápio atual com preço
  e estoque.
- **2 - Gerenciar Clientes**: cadastra cliente (id, nome, telefone) ou lista
  os clientes cadastrados.
- **3 - Criar Novo Pedido**: pede o id de um cliente já cadastrado e cria um
  pedido novo (status inicial `RECEBIDO`) vinculado a ele.
- **4 - Adicionar Item ao Pedido**: pede id do pedido, código do item do
  cardápio e quantidade; dá baixa no estoque e só funciona enquanto o
  pedido está `RECEBIDO`.
- **5 - Fechar Pedido / Alterar Status**: avança o pedido no ciclo de vida
  (`RECEBIDO → EM_PREPARO → PRONTO → ENTREGUE`) ou cancela (só permitido em
  `RECEBIDO`/`EM_PREPARO`). Depois de cada transição bem-sucedida, imprime
  o **comprovante** do pedido (itens, descontos aplicados e total).
- **6 - Exibir Relatórios Gerenciais**: faturamento do dia, ticket médio,
  item mais vendido e ranking de clientes por valor total gasto — todos
  calculados com Java Streams em `RelatorioService`.
- **0 - Sair e Salvar**: grava cardápio, clientes e pedidos de volta nos
  três arquivos CSV e encerra o programa.

Todas as chamadas ao `PedidoService` no menu são protegidas por
`try/catch` das exceções `EstoqueInsuficienteException`,
`PedidoVazioException` e `TransicaoStatusInvalidaException`, exibindo uma
mensagem de erro amigável em vez de derrubar o programa.

## Formato dos arquivos CSV

Campos separados por `;`. Sem cabeçalho, uma linha por registro.

**`cardapio.csv`** — a primeira coluna identifica o tipo do item:

```
PRATO;codigo;nome;preco;estoque;ingredientes;tempoPreparoMinutos
BEBIDA;codigo;nome;preco;estoque;volumeMl;alcoolica
SOBREMESA;codigo;nome;preco;estoque;gelada;porcoes
```

**`clientes.csv`**:

```
id;nome;telefone
```

**`pedidos.csv`** — o último campo lista os itens do pedido separados por
vírgula, cada um no formato `codigo:quantidade`:

```
idPedido;idCliente;dataHora;status;codigo1:qtd1,codigo2:qtd2,...
```

Exemplo (do arquivo `data/pedidos.csv` incluso no repositório):

```
PED001;C01;2026-08-15T12:30:00;ENTREGUE;P01:2,B01:2
```

Esse pedido é do cliente `C01`, foi feito em 15/08/2026 às 12:30, já está
`ENTREGUE` e contém 2 unidades do item `P01` e 2 do item `B01`.

## Dados de exemplo

O repositório já vem com uma massa de dados mínima em `data/` para o
programa rodar imediatamente: 2 pratos, 2 bebidas e 1 sobremesa no
cardápio, 2 clientes e 2 pedidos de exemplo (um já `ENTREGUE`, outro ainda
`RECEBIDO`, pronto para testar as opções 4 e 5 do menu).
