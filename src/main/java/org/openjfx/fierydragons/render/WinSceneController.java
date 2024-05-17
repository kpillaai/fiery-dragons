package org.openjfx.fierydragons.render;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinSceneController {
    @FXML
    Label winLabel;

    // display winning players name
    public void displayName(String playerName) {
        winLabel.setText("Game Over. " + playerName + " wins!");
    }
}

