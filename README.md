# Sistema de Pedidos

Aplicação de console em Java para gestão de pedidos de um restaurante/delivery.
Projeto da Etapa 2 — Linguagem de Programação II.

## Equipe
- Andrey Basilio — camada `model` + `exception`
- Arthur Ronald — camada `service`
- Hayne Rene — camada `repository` + `app`

## Como rodar
Requer JDK 26 e Maven.

    mvn clean compile
    mvn exec:java -Dexec.mainClass="br.edu.lp2.pedidos.Main"

Ou rode a classe `Main` diretamente pela IDE.

## Estrutura
    app/         menu de console (CLI)
    model/       entidades do domínio
    service/     regras de negócio
    repository/  persistência em CSV
    exception/   exceções próprias