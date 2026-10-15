package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.example.dao.JugadorDAO;
import org.example.logica.Jugador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private Button btnConfirmar;
    @FXML private Button btnJugar;
    @FXML private Button btnClasificacion;
    @FXML private Label lblMensajeNombre;

    // ===== SESIÓN ESTÁTICA (PERSISTE MIENTRAS LA APP ESTÉ ABIERTA) ===== \\
    private static Jugador jugadorSesion = null;
    private static String nombreSesion = null;

    private boolean nombreConfirmado = false;
    private Jugador jugadorActual = null;

    private final String ESTILO_CONFIRMAR = "-fx-background-color: #2c3e50; -fx-text-fill: #ffffff; -fx-font-family: 'Impact'; -fx-font-size: 14px; -fx-border-color: #e67e22; -fx-border-width: 2px; -fx-cursor: hand;";
    private final String ESTILO_CONFIRMADO = "-fx-background-color: #27ae60; -fx-text-fill: #ffffff; -fx-font-family: 'Impact'; -fx-font-size: 14px; -fx-border-color: #2ecc71; -fx-border-width: 2px; -fx-cursor: hand;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Límite de 16 caracteres
        txtNombre.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= 16) {
                return change;
            }
            return null;
        }));

        // Si ya había un nombre guardado en esta sesión, restaurarlo confirmado
        if (nombreSesion != null && !nombreSesion.isEmpty() && jugadorSesion != null) {
            txtNombre.setText(nombreSesion);
            this.jugadorActual = jugadorSesion;
            this.nombreConfirmado = true;

            btnConfirmar.setText("Confirmado");
            btnConfirmar.setStyle(ESTILO_CONFIRMADO);
            btnJugar.setDisable(false);

            lblMensajeNombre.setText("¡Luchador listo!");
            lblMensajeNombre.setStyle("-fx-text-fill: #2ecc71;");
        } else {
            btnJugar.setDisable(true);
        }

        // Si el usuario edita el texto, pedir confirmar de nuevo
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (nombreConfirmado && !newValue.equals(nombreSesion)) {
                nombreConfirmado = false;
                btnConfirmar.setText("Confirmar");
                btnConfirmar.setStyle(ESTILO_CONFIRMAR);
                btnJugar.setDisable(true);
                lblMensajeNombre.setText("Has modificado el nombre. Vuelve a confirmar.");
                lblMensajeNombre.setStyle("-fx-text-fill: #e67e22;");
            }
        });
    }

    @FXML
    private void onConfirmarClick() {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();

        if (nombre.length() < 3) {
            lblMensajeNombre.setText("El nombre debe tener mínimo 3 caracteres.");
            lblMensajeNombre.setStyle("-fx-text-fill: #e74c3c;");
            nombreConfirmado = false;
            btnJugar.setDisable(true);
            return;
        }

        // Válido: guardar en memoria de sesión
        nombreConfirmado = true;
        btnConfirmar.setText("Confirmado");
        btnConfirmar.setStyle(ESTILO_CONFIRMADO);
        btnJugar.setDisable(false);

        lblMensajeNombre.setText("¡Luchador confirmado!");
        lblMensajeNombre.setStyle("-fx-text-fill: #2ecc71;");

        this.jugadorActual = obtenerOCrearJugador(nombre);
        nombreSesion = nombre;
        jugadorSesion = this.jugadorActual;
    }

    private Jugador obtenerOCrearJugador(String nombre) {
        try {
            List<Jugador> jugadores = JugadorDAO.findAll();
            for (Jugador j : jugadores) {
                if (j.getNombre().equalsIgnoreCase(nombre)) {
                    return j;
                }
            }

            Jugador nuevo = new Jugador(nombre);
            JugadorDAO.add(nuevo);

            List<Jugador> actualizados = JugadorDAO.findAll();
            for (Jugador j : actualizados) {
                if (j.getNombre().equalsIgnoreCase(nombre)) {
                    return j;
                }
            }
            return nuevo;
        } catch (Exception e) {
            System.err.println("Aviso BD: modo local para jugador: " + e.getMessage());
            return new Jugador(nombre, 1, 0, 0, 0);
        }
    }

    @FXML
    private void onJugarClick(ActionEvent event) {
        if (!nombreConfirmado || jugadorActual == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/pantallaPartida.fxml"));
            Parent root = loader.load();

            PartidaController controller = loader.getController();
            controller.setJugador(jugadorActual);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClasificacionClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menuClasificacion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}