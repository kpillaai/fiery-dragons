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
import org.openjfx.fierydragons.GameState;
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


    /**
     * @author  Jeffrey Yan
     * @desc    used to get the desired number of players depending on the slider value selected. Uses listeners
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerCountSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            }
        });
        playerCount = 4;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    sets the player count depending on the value of the slider
     */
    public void onSliderChanged() {
        playerCount = (int) playerCountSlider.getValue();

    }

    /**
     * @author  Jeffrey Yan
     * @desc    Takes an input of event which is an onclick. Switches to the Board scene where the game is played
     */
    public void switchToBoardScene(ActionEvent event) throws IOException {
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game-board.fxml"));
        root = fxmlLoader.load();

        if (playerCount == 0) {
            playerCount = Game.getInstance().getPlayerCount();
        }

        // setup the board controller and initialise values
        BoardController boardController = fxmlLoader.getController();
        boardController.showPlayerCount(playerCount);

        // set player values
        Game.getInstance().setPlayerCount(playerCount);
        Game.getInstance().addPlayers();

        // render the board
        boardController.renderDragonTokens();
        boardController.initialisePlayerColour();
        boardController.showCurrentPlayer();
        boardController.initialiseFlippedCards();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
