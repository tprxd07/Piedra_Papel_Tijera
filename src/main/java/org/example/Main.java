package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/inicio.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cuarzo Pergamino Tenazas");
        stage.setScene(scene);
        stage.show();
    }
}

