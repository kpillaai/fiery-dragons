package org.openjfx.fierydragons.render;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinSceneController {
    @FXML
    Label winLabel;

    /**
     * @author  Krishna Pillai Manogaran
     * @desc    Function used to set label and show who the winning player is.
     */
    public void displayName(String playerName) {
        winLabel.setText("Game Over. " + playerName + " wins!");
    }

    public void displayDraw(String playersWhoTied) {
        winLabel.setText("All players ran out of time! Game Over.\n" + playersWhoTied + " are Tied!");
    }
}

