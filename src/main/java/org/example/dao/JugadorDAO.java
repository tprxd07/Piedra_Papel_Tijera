package org.example.dao;

import org.example.dataAccess.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {
    public class InmuebleDAO {

        private final static String SQL_ALL = "SELECT * FROM jugador";
        private final static String SQL_FIND_BY_ID = "SELECT * FROM jugador Where idJugador = ?";
        private final static String SQL_INSERT = "INSERT INTO jugador (nombre) VALUES (?)";
        private final static String SQL_UPDATE_RACHA = "UPDATE jugador SET rachasDeVictoria = ? WHERE idJugador = ?";
        private final static String SQL_DELETE = "DELETE FROM jugador WHERE idJugador = ?";

        public static List<Jugador> findAll() {
            List<Jugador> jugadores = new ArrayList<>();
            try (ResultSet rs = ConnectionDB.getConnection().createStatement().executeQuery(SQL_ALL)) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int victorias = rs.getInt("victorias");
                    int partidasGanadas = rs.getInt("partidasGanadas");
                    int rachasDeVictoria = rs.getInt("rachasDeVictoria");
                    Jugador jugador = new Jugador(id, password,nombre,dinero);
                    jugador.add(jugador);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return propietarios;
        }

        public static Jugador findById(int id){
            Jugador jugador = null;
            try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_FIND_BY_ID)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int victorias = rs.getInt("victorias");
                    int partidasGanadas = rs.getInt("partidasGanadas");
                    int rachasDeVictoria = rs.getInt("rachasDeVictoria");
                    jugador = new Jugador(id, password,nombre,dinero);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return jugador;
        }

        public void add(Jugador jugador) {
            if (!jugador.getNombre().isEmpty()) {
                try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_INSERT)) {
                    ps.setString(1, jugador.getNombre());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public static void updateRacha(int id){
                try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_UPDATE_RACHA)) {
                    ps.setInt(1,0);
                    ps.setInt(2,id);
                    ps.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
            }
        }
        public static void deleteJugadorById(int id) {
            if(findById(id)!=null){
                try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_DELETE)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
