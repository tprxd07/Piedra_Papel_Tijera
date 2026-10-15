package org.example.logica;

import java.util.Random;

public class Jugador {
    public String nombre;
    private int id;

    private int numeroVictorias;
    private int partidasJugadas;
    private int rachaVictorias;

    private final Random random = new Random();

    // ===== CONSTRUCTORES ===== \\
    public Jugador(){
        this.nombre = "Nuevo Jugador";
        this.numeroVictorias = 0;
        this.partidasJugadas = 0;
        this.rachaVictorias = 0;
    }

    public Jugador(String nombre){
        this.nombre = nombre;
        this.numeroVictorias = 0;
        this.partidasJugadas = 0;
        this.rachaVictorias = 0;
    }

    public Jugador(String nombre, int id, int victorias, int partidasJugadas, int rachaVictorias) {
        this.nombre = nombre;
        this.id = id;
        this.numeroVictorias = victorias;
        this.partidasJugadas = partidasJugadas;
        this.rachaVictorias = rachaVictorias;
    }

    // ===== GETTERS && SETTERS ===== \\

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getNumeroVictorias() {
        return numeroVictorias;
    }

    public void setNumeroVictorias(int numeroVictorias) {
        this.numeroVictorias = numeroVictorias;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public int getRachaVictorias() {
        return rachaVictorias;
    }

    public void setRachaVictorias(int rachaVictorias) {
        this.rachaVictorias = rachaVictorias;
    }


    // ===== TO STRING ===== \\
    @Override
    public String toString() {
        String datosJugador = "===== DATOS DEL JUGADOR =====" + "\n" +
                "Nombre: " + nombre + "\n" +
                "ID: " + id + "\n" +
                "Partidas ganadas: " + numeroVictorias + "\n" +
                "Partidas jugadas: " + partidasJugadas + "\n" +
                "Racha de victorias: " + rachaVictorias;
        return datosJugador;
    }


    // ===== METODOS DE JUGADOR ===== \\

    /**
     * Parte de la lógica del jugador.
     * Suma 1 a partidas guardadas, racha de victorias y numero de victorias
     */
    public void victoria() {
        numeroVictorias ++;
        rachaVictorias ++;
        partidasJugadas ++;
    }

    /**
     * Parte de la lógica del jugador.
     * Reestablece la racha de victorias a 0 y suma 1 a las partidas jugadas
     */
    public void derrota() {
        rachaVictorias = 0;
        partidasJugadas ++;
    }

}
