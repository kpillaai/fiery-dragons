package org.openjfx.fierydragons.render;

import javafx.animation.PauseTransition;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.entities.Deck;
import org.openjfx.fierydragons.entities.MapPiece;
import org.openjfx.fierydragons.entities.Tile;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BoardController {

    @FXML
    private AnchorPane anchorPane;

    private boolean animationInProgress = false;

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
                    flipCard(circle, circle.getId());
                });
            }
        }
        renderChits();
        renderVolcanoCards();
    }

    private void flipCard(Node circle, String id) {
        if (animationInProgress) {
            return;
        }
        animationInProgress = true;
        // Call the flipCard() method here

        Game.getInstance().getCurrentPlayer().flipCard(Integer.parseInt(id.substring(8)));


        // Rendering card flipping
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), circle);
        rotateTransition.setByAngle(180);
        rotateTransition.setAxis(Rotate.X_AXIS);
        RotateTransition rotateTransition1 = new RotateTransition(Duration.seconds(1), circle);
        rotateTransition1.setByAngle(180);
        rotateTransition1.setAxis(Rotate.X_AXIS);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));

        for (Node picture : anchorPane.getChildren()) {
            if (Objects.equals(picture.getId(), id.substring(8))) {
                picture.setVisible(true);
                rotateTransition.setOnFinished(event -> {
                    pauseTransition.play();
                    picture.setVisible(false);
                    rotateTransition1.play();
                    animationInProgress = false;
                });
            }
        }
        rotateTransition.play();
    }

    private void renderChits() {
        ObservableList<Node> circles = anchorPane.getChildren();
        Deck chits = Board.getInstance().getDeck();

        ObservableList<Node> images = FXCollections.observableArrayList();

        // Adding Chit card images
        for (Node chitCard: circles) {
            if (chitCard.getId().startsWith("chitCard")) {
                // Generating filepath
                String idString = chitCard.getId();
                int idNumber = Integer.parseInt(idString.substring(8));

                String fileNameKey = chits.getChitCard(idNumber).getKey().toString().toLowerCase();
                String fileNameValue = chits.getChitCard(idNumber).getValue().toString();
                String fileName = fileNameKey + fileNameValue + ".png";

                // Create new image
                System.out.println("/org/openjfx/fierydragons/images/" + fileName);
                Image image = new Image(getClass().getResourceAsStream("/org/openjfx/fierydragons/images/" + fileName));
                ImageView imageView = new ImageView(image);
                imageView.setId(idString.substring(8));
                imageView.setVisible(false);
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

    private void renderVolcanoCards() {
        ArrayList<MapPiece> mapPieces = Board.getInstance().getMapPieces();
        double outerRadius = 388;
        double innerRadius = 265;
        double middleRadius = (outerRadius + innerRadius) / 2;
        int pieces = 0;
        for (int i = 0; i < mapPieces.size(); i++) {
            pieces += mapPieces.get(i).getTiles().size();
        }

        double centreX = 960;
        double centreY = 540;
        double pieceAngle = 360.0/pieces;
        double offsetAngle = pieceAngle / 2;

        // looping through mapPieces.size (8) and then looping through each MapPiece (3) results in 24 tiles
        for (int i = 0; i < mapPieces.size(); i++) {
            for (int j = 0; j < mapPieces.get(i).getTiles().size(); j++) {
                // calculate what tile number. -1 offset for alignment of cave with middle of map piece when displaying
                int tile_increment = (i * 3) + j - 1;

                // Calculating angles
                double endAngle = tile_increment * pieceAngle + offsetAngle;
                double angleRad = Math.toRadians(endAngle);

                // Calculate endpoints of the lines
                double endX = centreX + Math.cos(angleRad) * outerRadius;
                double endY = centreY + Math.sin(angleRad) * outerRadius;
                double startX = centreX + Math.cos(angleRad) * innerRadius;
                double startY = centreY + Math.sin(angleRad) * innerRadius;

                // Creating and adding lines
                Line sliceLine = new Line(startX, startY, endX, endY);
                sliceLine.setStroke(Color.BLACK);
                anchorPane.getChildren().add(sliceLine);

                // Generating Animals for each tile
                double animalX = centreX + Math.cos(Math.toRadians(tile_increment * pieceAngle)) * middleRadius;
                double animalY = centreX + Math.sin(Math.toRadians(tile_increment * pieceAngle)) * middleRadius;

                String filePath = "/org/openjfx/fierydragons/images/" + mapPieces.get(i).getTiles().get(j).getTileType().toString().toLowerCase() + "1.png";

                // Create new image
                Image image = new Image(getClass().getResourceAsStream(filePath));
                ImageView imageView = new ImageView(image);
                imageView.setVisible(true);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                // Get location on where it should be placed
                double topLeftX = animalX - 40; // Figure out a way to do this better
                double topLeftY = animalY - 460;

                // Add image
                AnchorPane.setTopAnchor(imageView, topLeftY);
                AnchorPane.setLeftAnchor(imageView, topLeftX);
                anchorPane.getChildren().add(imageView);
            }
        }
    }
}
