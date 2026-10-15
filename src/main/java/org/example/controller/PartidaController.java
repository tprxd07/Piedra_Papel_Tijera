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
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.dao.JugadorDAO;
import org.example.logica.Batalla;
import org.example.logica.Jugador;
import org.example.logica.Maquina;
import org.example.logica.TiposEnum;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class PartidaController implements Initializable {

    @FXML private MediaView mediaView;
    @FXML private ProgressBar barraVidaJugador;
    @FXML private ProgressBar barraVidaMaquina;
    @FXML private Label lblMarcador;
    @FXML private Label lblNombreJugador;
    @FXML private Label lblNombreMaquina;
    @FXML private Label lblEstadoRonda;
    @FXML private Label lblResultadoFinal;

    @FXML private VBox contenedorBotones;
    @FXML private Button btnPiedra;
    @FXML private Button btnPapel;
    @FXML private Button btnTijeras;

    @FXML private HBox panelFinPartida;
    @FXML private Button btnReintentar;
    @FXML private Button btnVolverMenu;

    @FXML private ImageView imgRyu;
    @FXML private ImageView imgKen;

    private MediaPlayer mediaPlayerFondo;
    private MediaPlayer sfxWin;
    private MediaPlayer sfxLose;
    private MediaPlayer sfxWinPerfect;

    private Jugador jugador;
    private Maquina maquina;
    private Batalla batalla;
    private final Random random = new Random();

    private int rondasJugador = 0;
    private int rondasMaquina = 0;
    private int rachaRondasSeguidas = 0;
    private final int RONDAS_PARA_GANAR = 3;
    private boolean partidaFinalizada = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aplicarTipografiaArcade();
        iniciarVideoFondo();
        inicializarEfectosDeSonido();

        this.jugador = new Jugador("PLAYER", 1, 0, 0, 0);
        this.maquina = new Maquina("IA", 2);
        this.batalla = new Batalla(jugador, maquina);

        lblNombreJugador.setText(jugador.getNombre().toUpperCase());
        lblNombreMaquina.setText(maquina.getNombre().toUpperCase());

        actualizarMarcadorYVidas();
    }

    private void iniciarVideoFondo() {
        try {
            URL fondoUrl = getClass().getResource("/sprites/FondoPiedraPapelTijera.mp4");
            if (fondoUrl != null) {
                if (mediaPlayerFondo != null) {
                    mediaPlayerFondo.stop();
                    mediaPlayerFondo.dispose();
                }
                Media media = new Media(fondoUrl.toExternalForm());
                mediaPlayerFondo = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayerFondo);
                mediaPlayerFondo.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayerFondo.play();
            } else {
                System.err.println("No se encontró el vídeo en /sprites/FondoPiedraPapelTijera.mp4");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar vídeo de fondo: " + e.getMessage());
        }
    }

    private void inicializarEfectosDeSonido() {
        sfxWin = prepararReproductor("/audio/you_win.mp3");
        sfxLose = prepararReproductor("/audio/you_lose.mp3");
        sfxWinPerfect = prepararReproductor("/audio/you_win_perfect.mp3");
    }

    private MediaPlayer prepararReproductor(String ruta) {
        try {
            URL url = getClass().getResource(ruta);
            if (url == null) return null;

            Media media = new Media(url.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);

            player.setOnEndOfMedia(() -> {
                player.stop();
                player.seek(Duration.ZERO);

                // Reanudar fondo musical/vídeo sin reiniciarlo
                if (mediaPlayerFondo != null && mediaPlayerFondo.getStatus() != MediaPlayer.Status.PLAYING) {
                    mediaPlayerFondo.play();
                }

                if (!partidaFinalizada) {
                    setBotonesHabilitados(true);
                }
            });
            return player;
        } catch (Exception e) {
            System.err.println("No se pudo cargar el audio " + ruta + ": " + e.getMessage());
            return null;
        }
    }

    private void aplicarTipografiaArcade() {
        String rutaFuente = "/fonts/super-street-fighter-ii-large-colour.colr.ttf";

        Font fontMarcador = cargarFuente(rutaFuente, 24.0);
        Font fontNombres = cargarFuente(rutaFuente, 19.0);
        Font fontResultado = cargarFuente(rutaFuente, 36.0);
        Font fontBotones = cargarFuente(rutaFuente, 12.0);

        if (fontMarcador != null) lblMarcador.setFont(fontMarcador);

        if (fontNombres != null) {
            lblNombreJugador.setFont(fontNombres);
            lblNombreMaquina.setFont(fontNombres);
        } else {
            //Por si la fuente no carga
            Font fallback = Font.font("Impact", 19.0);
            lblNombreJugador.setFont(fallback);
            lblNombreMaquina.setFont(fallback);
        }

        if (fontResultado != null) lblResultadoFinal.setFont(fontResultado);

        if (fontBotones != null) {
            btnPiedra.setFont(fontBotones);
            btnPapel.setFont(fontBotones);
            btnTijeras.setFont(fontBotones);
            btnReintentar.setFont(fontBotones);
            btnVolverMenu.setFont(fontBotones);
        }

        lblNombreJugador.setStyle("-fx-text-fill: #ff9900; -fx-effect: dropshadow(three-pass-box, #000000, 3, 0, 2, 2);");
        lblNombreMaquina.setStyle("-fx-text-fill: #00e5ff; -fx-effect: dropshadow(three-pass-box, #000000, 3, 0, 2, 2);");
    }

    private Font cargarFuente(String ruta, double tamano) {
        try (InputStream is = getClass().getResourceAsStream(ruta)) {
            if (is != null) return Font.loadFont(is, tamano);
        } catch (Exception ignored) {}
        return null;
    }

    @FXML private void onPiedraClick() { procesarTurno(TiposEnum.PIEDRA); }
    @FXML private void onPapelClick() { procesarTurno(TiposEnum.PAPEL); }
    @FXML private void onTijerasClick() { procesarTurno(TiposEnum.TIJERA); }

    private void procesarTurno(TiposEnum opcionJugador) {
        if (partidaFinalizada) return;

        TiposEnum opcionMaquina = maquina.elegirOpcion(random);
        int resultado = batalla.comparar(opcionJugador, opcionMaquina);

        if (resultado == 1) {
            rondasJugador++;
            rachaRondasSeguidas++;
            lblEstadoRonda.setText("¡Ganas la ronda! (" + opcionJugador + " vs " + opcionMaquina + ")");
            actualizarMarcadorYVidas();

            if (rachaRondasSeguidas >= 3) {
                reproducirSFX(sfxWinPerfect);
            } else {
                reproducirSFX(sfxWin);
            }
        } else if (resultado == -1) {
            rondasMaquina++;
            rachaRondasSeguidas = 0;
            lblEstadoRonda.setText("Pierdes la ronda... (" + opcionJugador + " vs " + opcionMaquina + ")");
            actualizarMarcadorYVidas();
            reproducirSFX(sfxLose);
        } else {
            lblEstadoRonda.setText("Empate (" + opcionJugador + ")");
        }

        verificarFinPartida();
    }

    private void actualizarMarcadorYVidas() {
        lblMarcador.setText(rondasJugador + " - " + rondasMaquina);

        double vidaJugador = 1.0 - ((double) rondasMaquina / RONDAS_PARA_GANAR);
        double vidaMaquina = 1.0 - ((double) rondasJugador / RONDAS_PARA_GANAR);

        barraVidaJugador.setProgress(Math.max(0.0, vidaJugador));
        barraVidaMaquina.setProgress(Math.max(0.0, vidaMaquina));
    }

    private void reproducirSFX(MediaPlayer player) {
        if (player == null) return;

        //Pausar vídeo de fondo mientras suena la voz
        if (mediaPlayerFondo != null && mediaPlayerFondo.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayerFondo.pause();
        }

        setBotonesHabilitados(false);
        player.stop();
        player.seek(Duration.ZERO);
        player.play();
    }

    private void setBotonesHabilitados(boolean habilitados) {
        btnPiedra.setDisable(!habilitados);
        btnPapel.setDisable(!habilitados);
        btnTijeras.setDisable(!habilitados);
    }

    private void verificarFinPartida() {
        if (rondasJugador >= RONDAS_PARA_GANAR) {
            finalizarPartida(true);
        } else if (rondasMaquina >= RONDAS_PARA_GANAR) {
            finalizarPartida(false);
        }
    }

    private void finalizarPartida(boolean ganaJugador) {
        partidaFinalizada = true;

        contenedorBotones.setVisible(false);
        panelFinPartida.setVisible(true);
        lblResultadoFinal.setVisible(true);

        if (ganaJugador) {
            lblResultadoFinal.setText(rachaRondasSeguidas >= 3 ? "YOU WIN PERFECT" : "YOU WIN");
            jugador.victoria();
            try {
                if (jugador.getId() > 0) {
                    JugadorDAO.updateVictoria(jugador.getId());
                    JugadorDAO.updateJugadas(jugador.getId());
                }
            } catch (Exception ignored) {}
        } else {
            lblResultadoFinal.setText("YOU LOSE");
            jugador.derrota();
            try {
                if (jugador.getId() > 0) {
                    JugadorDAO.borrarRacha(jugador.getId());
                    JugadorDAO.updateJugadas(jugador.getId());
                }
            } catch (Exception ignored) {}
        }
    }

    @FXML
    private void onReintentarClick() {
        rondasJugador = 0;
        rondasMaquina = 0;
        rachaRondasSeguidas = 0;
        partidaFinalizada = false;

        lblResultadoFinal.setVisible(false);
        panelFinPartida.setVisible(false);
        contenedorBotones.setVisible(true);
        lblEstadoRonda.setText("");

        actualizarMarcadorYVidas();
        setBotonesHabilitados(true);

        //Si el vídeo se había quedado en blanco o cerrado, se levanta de nuevo
        if (mediaPlayerFondo == null || mediaPlayerFondo.getStatus() == MediaPlayer.Status.DISPOSED) {
            iniciarVideoFondo();
        } else {
            mediaPlayerFondo.seek(Duration.ZERO);
            mediaPlayerFondo.play();
        }
    }

    @FXML
    private void onVolverMenuClick(ActionEvent event) {
        try {
            URL menuUrl = getClass().getResource("/views/inicio.fxml");
            if (menuUrl == null) menuUrl = getClass().getResource("/inicio.fxml");
            if (menuUrl == null) menuUrl = getClass().getResource("/views/menuInicio.fxml");
            if (menuUrl == null) menuUrl = getClass().getResource("/menuInicio.fxml");

            if (menuUrl == null) {
                System.err.println("ERROR: No se encontró el archivo FXML del menú en /views/ ni en la raíz.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(menuUrl);
            Parent root = loader.load();

            detenerMultimedia();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Excepción al volver al menú: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void detenerMultimedia() {
        if (mediaPlayerFondo != null) {
            mediaPlayerFondo.stop();
            mediaPlayerFondo.dispose();
            mediaPlayerFondo = null;
        }
        if (sfxWin != null) { sfxWin.stop(); sfxWin.dispose(); }
        if (sfxLose != null) { sfxLose.stop(); sfxLose.dispose(); }
        if (sfxWinPerfect != null) { sfxWinPerfect.stop(); sfxWinPerfect.dispose(); }
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        if (lblNombreJugador != null) {
            lblNombreJugador.setText(jugador.getNombre().toUpperCase());
        }
        if (batalla != null) {
            batalla.setJugador(jugador);
        }
    }
}