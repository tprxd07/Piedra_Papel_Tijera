package org.example.controller;

import javafx.scene.text.Text;
import org.example.dao.JugadorDAO;
import org.example.logica.Jugador;

import java.util.Comparator;
import java.util.List;

public class JugadorController {

    public Text txtPlayer1;
    public Text txtPlayer2;
    public Text txtPlayer3;

    public Text txtScore1;
    public Text txtScore2;
    public Text txtScore3;

    public Text txtPlayerS1;
    public Text txtPlayerS2;
    public Text txtPlayerS3;

    public Text txtStreak1;
    public Text txtStreak2;
    public Text txtStreak3;

    public void initialize(){
        List<Jugador> jugadores = JugadorDAO.findAll();

        //Esto lo hago por prevención de errores aunque no se encuentren jugadores para poder mostrar algo en pantalla, 3 veces por las 3 puntuaciones mas altas
        Jugador j0 = new Jugador("NO SCORE YET", 0,0,0,0);

        jugadores.add(j0);
        jugadores.add(j0);
        jugadores.add(j0);

        //Ordenamos por número de victorias
        jugadores.sort(Comparator.comparingInt(Jugador::getNumeroVictorias));
        jugadores = jugadores.reversed();

        //Asignamos el texto en pantalla para jugadores y puntuacion
        txtPlayer1.setText(jugadores.getFirst().getNombre());
        txtPlayer2.setText(jugadores.get(1).getNombre());
        txtPlayer3.setText(jugadores.get(2).getNombre());

        txtScore1.setText(String.valueOf(jugadores.getFirst().getNumeroVictorias()));
        txtScore2.setText(String.valueOf(jugadores.get(1).getNumeroVictorias()));
        txtScore3.setText(String.valueOf(jugadores.get(2).getNumeroVictorias()));

        //Ordenamos por rachaVictorias
        jugadores.sort(Comparator.comparingInt(Jugador::getRachaVictorias));
        jugadores = jugadores.reversed();

        txtPlayerS1.setText(jugadores.getFirst().getNombre());
        txtPlayerS2.setText(jugadores.get(1).getNombre());
        txtPlayerS3.setText(jugadores.get(2).getNombre());

        txtStreak1.setText(String.valueOf(jugadores.getFirst().getRachaVictorias()));
        txtStreak2.setText(String.valueOf(jugadores.get(1).getRachaVictorias()));
        txtStreak3.setText(String.valueOf(jugadores.get(2).getRachaVictorias()));
    }
}