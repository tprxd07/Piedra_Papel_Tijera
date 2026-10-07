package org.example.dao;

import org.example.dataAccess.ConnectionDB;
import org.example.logica.Maquina;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaquinaDAO {
    public class JugadorDAO {
        public class InmuebleDAO {

            private final static String SQL_ALL = "SELECT * FROM maquina";
            private final static String SQL_FIND_BY_ID = "SELECT * FROM jugador Where idJugador = ?";
            private final static String SQL_INSERT = "INSERT INTO jugador (nombre) VALUES (?)";
            private final static String SQL_DELETE = "DELETE FROM jugador WHERE idJugador = ?";

            public static List<Maquina> findAll() {
                List<Maquina> maquinas = new ArrayList<>();
                try (ResultSet rs = ConnectionDB.getConnection().createStatement().executeQuery(SQL_ALL)) {
                    while (rs.next()) {
                        String nombre = rs.getString("nombre");
                        int id = rs.getInt("idmaquina");
                        Maquina maquina = new Maquina(nombre,id);
                        maquinas.add(maquina);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return maquinas;
            }

            public static Maquina findById(int id){
                Maquina maquina = null;
                try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_FIND_BY_ID)) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String nombre = rs.getString("nombre");
                        maquina = new Maquina(nombre,id);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return maquina;
            }

            public void add(Maquina maquina) {
                if (!maquina.getNombre().isEmpty()) {
                    try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(SQL_INSERT)) {
                        ps.setString(1, maquina.getNombre());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
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
}
