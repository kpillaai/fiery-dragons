package org.openjfx.fierydragons.render;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.game.Game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private Slider playerCountSlider;

    private int playerCount;

    private Parent root;

    private FXMLLoader fxmlLoader;
    
    private Stage stage;

    private Scene scene;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerCountSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            }
        });
        playerCount = 4;
    }

    public void onSliderChanged() {
        playerCount = (int) playerCountSlider.getValue();
    }


    public void switchToBoardScene(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game-board.fxml"));
        root = fxmlLoader.load();

        BoardController boardController = fxmlLoader.getController();
        boardController.displayPlayerCount(playerCount);
        Game.getInstance().setPlayerCount(playerCount);
        Game.getInstance().addPlayers();
        boardController.renderDragonTokens();
        boardController.initialisePlayerColour();
        boardController.showCurrentPlayer();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
