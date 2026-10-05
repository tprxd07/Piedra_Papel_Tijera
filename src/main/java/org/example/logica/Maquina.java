package org.example.logica;

import java.util.Random;

public class Maquina {
    public String nombre;
    private int id;

    // ===== CONSTRUCTORES ===== \\
    public Maquina(){
        this.nombre = "Máquina";
    }

    public Maquina(String nombre){
        this.nombre = nombre;
    }

    public Maquina(String nombre, int id){
        this.nombre = nombre;
        this.id = id;
    }

    // ===== GETTERS && SETTERS ===== \\
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    // ===== METODOS DE MAQUINA ===== \\

    /**
     * Parte de la máquina
     * Este metodo elige una opción al azar de entre todas las que hay en TiposEnum
     * @param rng Es el Randomizer del jugador
     * @return La opción elegida
     */
    public TiposEnum elegirOpcion(Random rng) {
        TiposEnum[] opciones = TiposEnum.values();
        return opciones[rng.nextInt(opciones.length)];
    }
}
