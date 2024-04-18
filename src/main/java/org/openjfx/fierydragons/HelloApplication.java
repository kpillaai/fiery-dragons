package org.openjfx.fierydragons;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.openjfx.fierydragons.game.Game;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Creating Buttons for number of players
        Button noOfPlayersButton2 = new Button();
        noOfPlayersButton2.setText("2");
        Button noOfPlayersButton3 = new Button();
        noOfPlayersButton3.setText("3");
        Button noOfPlayersButton4 = new Button();
        noOfPlayersButton4.setText("4");

        //Adding buttons to new group
        Group settingsGroup = new Group(noOfPlayersButton2);
        settingsGroup.getChildren().add(noOfPlayersButton3);
        settingsGroup.getChildren().add(noOfPlayersButton4);

        // Updating Positions of these buttons
        noOfPlayersButton3.setTranslateX(100);
        noOfPlayersButton4.setTranslateX(200);

        

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
        Game game = new Game();
        game.initialise();

    }
}