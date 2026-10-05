package org.example;

import org.example.logica.Batalla;
import org.example.logica.Jugador;
import org.example.logica.Maquina;

public class Main {
    static void main() {
        Jugador jugador = new Jugador("CurroGamer20", 1, 0, 0, 0);
        Maquina maquina = new Maquina("Computaneitor", 2);

        Batalla batalla = new Batalla(jugador, maquina);

        batalla.jugarAlMejorDe3();

        System.out.println(jugador);
    }
}
