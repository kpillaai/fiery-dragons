package org.openjfx.fierydragons;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.fierydragons.game.Game;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Rendering the initial screen
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Fiery Dragons");
        stage.setScene(scene);
        stage.show();

        startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startGame() {
        Game.getInstance().initialise();
    }
}