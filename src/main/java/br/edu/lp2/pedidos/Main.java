package br.edu.lp2.pedidos;

import br.edu.lp2.pedidos.app.Menu;

/**
 * Ponto de entrada do programa. Único trabalho: criar o Menu e mandar ele
 * iniciar. Toda a lógica real fica em Menu (app) e nas camadas abaixo dela.
 */
public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.iniciar();
    }
}
