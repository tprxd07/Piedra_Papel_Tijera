package org.example.logica;

import java.util.Random;
import java.util.Scanner;

public class Batalla {

    private Jugador jugador;
    private Maquina maquina;
    private final Random random;
    private final Scanner scanner;


    // ===== CONSTRUCTORES ===== \\
    public Batalla(Jugador jugador, Maquina maquina) {
        this.jugador = jugador;
        this.maquina = maquina;
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }


    // ===== GETTERS Y SETTERS ===== \\
    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public Maquina getMaquina() { return maquina; }
    public void setMaquina(Maquina maquina) { this.maquina = maquina; }


    // ===== MÉTODOS DE BATALLA ===== \\
    /**
     * Juega una serie al mejor de 3.
     * Al final, actualiza las estadísticas del jugador según el resultado global.
     */
    public void jugarAlMejorDe3() {
        int rondasJugador = 0;
        int rondasMaquina = 0;
        int numeroRonda = 1;

        System.out.println("===== AL MEJOR DE 3 =====");

        while (rondasJugador < 3 && rondasMaquina < 3) {
            System.out.println("\n--- Ronda " + numeroRonda + " ---");
            System.out.println("Marcador: " + jugador.getNombre() + " " + rondasJugador
                    + " - " + rondasMaquina + " " + maquina.getNombre());

            TiposEnum opcionJugador = pedirOpcionJugador();
            TiposEnum opcionMaquina = maquina.elegirOpcion(random);

            System.out.println("Tú: " + opcionJugador + " | Máquina: " + opcionMaquina);

            int resultado = comparar(opcionJugador, opcionMaquina);

            if (resultado == 1) {
                System.out.println("Ganas la ronda.");
                rondasJugador++;
            } else if (resultado == -1) {
                System.out.println("Pierdes la ronda.");
                rondasMaquina++;
            } else {
                System.out.println("Empate!!!");
            }

            numeroRonda++;
        }

        System.out.println("\n===== RESULTADO FINAL =====");
        System.out.println("Marcador: " + jugador.getNombre() + " " + rondasJugador
                + " - " + rondasMaquina + " " + maquina.getNombre());

        if (rondasJugador > rondasMaquina) {
            System.out.println("¡Has ganado!");
            jugador.victoria();
        } else {
            System.out.println("Has perdido :(");
            jugador.derrota();
        }
    }

    /**
     * Parte de la lógica del juego.
     * Compara dos opciones por posición del enum.
     * @return 1 si gana el jugador, -1 si gana la máquina, 0 si empatan
     */
    public int comparar(TiposEnum opcionJugador, TiposEnum opcionMaquina) {
        if (opcionJugador == opcionMaquina) return 0;

        else if ((opcionJugador.ordinal() + 1) % TiposEnum.values().length == opcionMaquina.ordinal()) return -1;

        else return 1;
    }

    // ===== MÉTODOS AUXILIARES ===== \\

    private TiposEnum pedirOpcionJugador() {
        while (true) {
            System.out.print("Elige una opción (PIEDRA, PAPEL, TIJERA): ");
            String entrada = scanner.nextLine().trim().toUpperCase();

            try {
                return TiposEnum.valueOf(entrada);
            } catch (IllegalArgumentException e) {
                System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
    }
}