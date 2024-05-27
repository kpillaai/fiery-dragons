package org.openjfx.fierydragons.render;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openjfx.fierydragons.GameState;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SceneController {
    @FXML
    private Button loadGameButton;

    /**
     * @author  Zilei Chen
     * @desc    Changes the scene and screen to the settings scene and screen through JavaFX
     */
    public void switchToSettingScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game-settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loadGame(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));

        Stage stage = (Stage) loadGameButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                GameState gameState = GameState.loadGame(file.getAbsolutePath());
                System.out.println("Game loaded successfully.");
                Game.setInstance(gameState.getGame());
                Board.setInstance(gameState.getBoard());
                Board.getInstance().setDeck(gameState.getDeck());
                BoardController.setInstance(gameState.getBoardController());
                BoardController.setLocationIndexArray(gameState.getLocationIndexArray());
                BoardController.setTileLocationArray(gameState.getTileLocationArray());
//                ArrayList<AnchorPane> anchorPanes = new ArrayList<>();
//                anchorPanes.add(BoardController.getInstance().getDragonAnchorPane());
//                anchorPanes.add(BoardController.getInstance().getSalamanderAnchorPane());
//                anchorPanes.add(BoardController.getInstance().getSpiderAnchorPane());
//                anchorPanes.add(BoardController.getInstance().getBatAnchorPane());
//
//                for (int i = 0; i < anchorPanes.size(); i++) {
//                    BoardController.getInstance().moveToken(anchorPanes.get(i), BoardController.getTileLocationArray().get(BoardController.getLocationIndexArray().get(i)));
//                }

                // Update your UI with the loaded game state if necessary
                SettingsController settingsController = new SettingsController();
                settingsController.switchToBoardScene(event);
            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle error loading game state
            }
        }
    }

    public void initialize() {}
}