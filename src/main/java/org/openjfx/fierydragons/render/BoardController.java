package org.openjfx.fierydragons.render;

import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.entities.MapPiece;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView bat1;

    public void switchToSettingScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game-settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        ObservableList<Node> circles = anchorPane.getChildren();
        for (Node circle : circles) {
            if (circle.getId().startsWith("chitCard")) {
                circle.setOnMouseClicked(mouseEvent -> {
                    System.out.println(circle.getId());
                    flipCard(circle, circle.getId());
                });
            }
        }
        bat1.setVisible(false);

        renderTileTypes();
    }

    private void flipCard(Node circle, String id) {
        // Call the flipCard() method here

        // Displaying flipped card here
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), circle);
        rotateTransition.setByAngle(180);
        rotateTransition.setAxis(Rotate.X_AXIS);
        rotateTransition.play();
        bat1.setVisible(true);
        System.out.println(rotateTransition.getAxis());

        System.out.println(Board.getInstance().getMapPieces());
    }

    private void renderTileTypes() {
        ObservableList<Node> circles = anchorPane.getChildren();
        ArrayList<MapPiece> mapPieces = Board.getInstance().getMapPieces();
        List<Pair<TileType, Integer>> chits = Board.getInstance().getChitCards();

        ObservableList<Node> images = FXCollections.observableArrayList();

        for (Node chitCard: circles) {
            if (chitCard.getId().startsWith("chitCard")) {
                // Generating filepath
                String idString = chitCard.getId();
                int idNumber = Integer.parseInt(idString.substring(8));

                String fileNameKey = chits.get(idNumber).getKey().toString().toLowerCase();
                String fileNameValue = chits.get(idNumber).getValue().toString();
                String fileName = fileNameKey + fileNameValue + ".png";
                String filePath = "file:/D:/Monash/2024/FIT3077/FieryDragons/target/classes/org/openjfx/fierydragons/images/" + fileName;

                // Create new image
                Image image = new Image(filePath);
                ImageView imageView = new ImageView(image);
                imageView.setVisible(true);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                // Get location on where it should be placed
                double centreX = chitCard.getLayoutX();
                double centreY = chitCard.getLayoutY();
                double imageWidth = imageView.getFitWidth();
                double imageHeight = imageView.getFitHeight();
                double topLeftX = centreX - (imageWidth / 2);
                double topLeftY = centreY - (imageHeight / 2);

                // Add image
                AnchorPane.setTopAnchor(imageView, topLeftY);
                AnchorPane.setLeftAnchor(imageView, topLeftX);
                images.add(imageView);
            }
        }
        for (Node image : images) {
            circles.add(image);
        }
    }
}
