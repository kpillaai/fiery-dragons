package org.openjfx.fierydragons.render;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openjfx.fierydragons.gameSaving.GameState;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Called when the load game button is pressed. Asks for a JSON file and deserialises it. The deserialised
     * info is then set to the relevant attributes using getters and setters to effectively present the saved game
     */
    @FXML
    private void loadGame(ActionEvent event) {
        // Get the directory containing the JAR file and decode the path
        String jarDirPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            jarDirPath = java.net.URLDecoder.decode(jarDirPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File jarDir = new File(jarDirPath).getParentFile();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));

        // Validate if jarDir is a valid directory
        if (jarDir.exists() && jarDir.isDirectory()) {
            fileChooser.setInitialDirectory(jarDir);
        } else {
            // Optionally handle the case where the initial directory is invalid
            System.err.println("Initial directory is invalid: " + jarDirPath);
        }

        Stage stage = (Stage) loadGameButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                GameState gameState = GameState.loadGame(file.getAbsolutePath());
                System.out.println("Game loaded successfully.");
                Game.setInstance(gameState.getGame());
                Board.setInstance(gameState.getBoard());
                Board.getInstance().setDeck(gameState.getBoard().getDeck());
                BoardController.setInstance(gameState.getBoardController());
                BoardController.setLocationIndexArray(gameState.getLocationIndexArray());
                BoardController.setTileLocationArray(gameState.getTileLocationArray());
                BoardController.getInstance().setFlippedCardId(gameState.getFlippedCardId());
                BoardController.setPlayerAnchorPaneMap(gameState.getPlayerAnchorPaneMap());
                SettingsController settingsController = new SettingsController();
                settingsController.switchToBoardScene(event);

                for (int i = 0; i < Game.getInstance().getPlayerList().size(); i++) { // Loading using JSON creates 2 separate objects rather than pointers
                    if (Game.getInstance().getCurrentPlayer().getId() == Game.getInstance().getPlayerList().get(i).getId()) {
                        Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayerList().get(i));
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle error loading game state
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void initialize() {}
}