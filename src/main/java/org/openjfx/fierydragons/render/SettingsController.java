package org.openjfx.fierydragons.render;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerCountSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                playerCount = (int) playerCountSlider.getValue();
            }
        });
    }

    public void switchToBoardScene(ActionEvent event) throws IOException {
        Game.getInstance().setPlayerCount(playerCount);
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game-board.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
