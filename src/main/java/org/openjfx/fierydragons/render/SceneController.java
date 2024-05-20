package org.openjfx.fierydragons.render;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.fierydragons.StartApplication;


import java.io.IOException;

public class SceneController {

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

    public void initialize() {}
}