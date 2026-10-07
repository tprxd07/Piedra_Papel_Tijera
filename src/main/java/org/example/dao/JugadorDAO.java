package org.example.dao;

import org.example.dataAccess.ConnectionDB;
import org.example.logica.Jugador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    private final static String SQL_ALL = "SELECT * FROM jugador";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM jugador Where idJugador = ?";
    private final static String SQL_INSERT = "INSERT INTO jugador (nombre) VALUES (?)";
    private final static String SQL_UPDATE_RACHA = "UPDATE jugador SET rachasDeVictoria = ? WHERE idJugador = ?";
    private final static String SQL_UPDATE_VICTORIA = "UPDATE jugador SET victorias = ?, rachasDeVictoria = ? WHERE idJugador = ?";
    private final static String SQL_UPDATE_PARTIDAS_JUGADAS = "UPDATE jugador SET partidasJugadas = ? WHERE idJugador = ?";


    public static List<Jugador> findAll() {
        List<Jugador> jugadores = new ArrayList<>();
        try (ResultSet rs = ConnectionDB.getConnection().createStatement().executeQuery(SQL_ALL)) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int id = rs.getInt("idJugador");
                int victorias = rs.getInt("victorias");
                int partidasJugadas = rs.getInt("partidasJugadas");
                int rachasDeVictoria = rs.getInt("rachasDeVictoria");
                Jugador jugador = new Jugador(nombre, id, victorias, partidasJugadas, rachasDeVictoria);
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jugadores;
    }

    public static Jugador findById(int id) {
        Jugador jugador = null;
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int victorias = rs.getInt("victorias");
                int partidasJugadas = rs.getInt("partidasJugadas");
                int rachasDeVictoria = rs.getInt("rachasDeVictoria");
                jugador = new Jugador(nombre, id, victorias, partidasJugadas, rachasDeVictoria);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jugador;
    }

    public static void add(Jugador jugador) {
        if (!jugador.getNombre().isEmpty()) {
            try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_INSERT)) {
                ps.setString(1, jugador.getNombre());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void borrarRacha(int id) {
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_UPDATE_RACHA)) {
            ps.setInt(1, 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateVictoria(int id) {
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_UPDATE_VICTORIA)) {
            Jugador jugador = findById(id);
            if (jugador != null) {
                ps.setInt(1, jugador.getNumeroVictorias() + 1);
                ps.setInt(2, jugador.getRachaVictorias() + 1);
                ps.setInt(3, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateJugadas(int id) {
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_UPDATE_PARTIDAS_JUGADAS)) {
            Jugador jugador = findById(id);
            if (jugador != null) {
                ps.setInt(1, jugador.getPartidasJugadas() + 1);
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

