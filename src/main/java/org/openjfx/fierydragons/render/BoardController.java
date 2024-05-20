package org.openjfx.fierydragons.render;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.entities.Deck;
import org.openjfx.fierydragons.entities.MapPiece;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;
import org.openjfx.fierydragons.game.Turn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BoardController   {

    @FXML
    public AnchorPane anchorPane;

    @FXML
    private Label playerCountLabel;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private AnchorPane dragonAnchorPane;

    @FXML
    private AnchorPane spiderAnchorPane;

    @FXML
    private AnchorPane salamanderAnchorPane;

    @FXML
    private AnchorPane batAnchorPane;

    @FXML
    private Parent root;

    @FXML
    private Button endTurnButton;

    private FXMLLoader fxmlLoader;

    private Stage stage;

    private Scene scene;

    private boolean animationInProgress = false;

    private static ArrayList<ArrayList<Double>> tileLocationArray;

    private ArrayList<double[]> caveLocationArray;

    private ArrayList<Color> playerColours;

    private static BoardController instance;

    protected static ArrayList<Integer> locationIndexArray;

    public BoardController() {
        instance = this;
    }

    public static BoardController getInstance() {
        return instance;
    }

    public void switchToSettingScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("game-settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToWinScene(Node node) throws IOException {
        Player winningPlayer = Game.getInstance().getCurrentPlayer();
        // move player's tile back to own cave
        int playerId = winningPlayer.getId();
        switch (playerId) {
            case 1:
                dragonAnchorPane.setLayoutX(caveLocationArray.get(playerId - 1)[0]);
                dragonAnchorPane.setLayoutY(caveLocationArray.get(playerId - 1)[1]);
                break;
            case 2:
                if (Game.getInstance().getPlayerCount() == 2) {
                    spiderAnchorPane.setLayoutX(caveLocationArray.get(2)[0]);
                    spiderAnchorPane.setLayoutY(caveLocationArray.get(2)[1]);
                }
                spiderAnchorPane.setLayoutX(caveLocationArray.get(playerId - 1)[0]);
                spiderAnchorPane.setLayoutY(caveLocationArray.get(playerId - 1)[1]);
                break;
            case 3:
                salamanderAnchorPane.setLayoutX(caveLocationArray.get(playerId - 1)[0]);
                salamanderAnchorPane.setLayoutY(caveLocationArray.get(playerId - 1)[1]);
                break;
            case 4:
                batAnchorPane.setLayoutX(caveLocationArray.get(playerId - 1)[0]);
                batAnchorPane.setLayoutY(caveLocationArray.get(playerId - 1)[1]);
                break;
        }
        try {
            // load the win scene
            fxmlLoader = new FXMLLoader(StartApplication.class.getResource("win-scene.fxml"));
            root = fxmlLoader.load();

            // send the player name to the WinSceneController to display they have won the game
            WinSceneController winSceneController = fxmlLoader.getController();
            String nameText = winningPlayer.getName();
            winSceneController.displayName(nameText);

            stage = (Stage) node.getScene().getWindow();
            scene = new Scene(root);

            // add a pause of 2 seconds to show the game board after the game is won before going to the win scene
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                // Set the scene to the stage after the delay
                stage.setScene(scene);
                // Show the stage
                stage.show();
            });
            pause.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCurrentPlayer() {
        currentPlayerLabel.setText(Game.getInstance().getCurrentPlayer().getName());
        currentPlayerLabel.setTextFill(playerColours.get(Game.getInstance().getCurrentPlayer().getId() - 1));
    }

    public void initialisePlayerColour() {
        playerColours = new ArrayList<>();
        AnchorPane[] anchorPanes = {dragonAnchorPane, spiderAnchorPane, salamanderAnchorPane, batAnchorPane};
        for (int i = 0; i < caveLocationArray.size(); i++) {
            ObservableList<Node> circleAndImage = anchorPanes[i].getChildren();
            for (Node circle: circleAndImage) {
                if (circle instanceof Circle) {
                    Color fillColour = (Color) ((Circle) circle).getFill();
                    playerColours.add(fillColour);
                }
            }
        }

        if (Game.getInstance().getPlayerCount() == 2) {
            Color colour1 = playerColours.get(2);
            playerColours.set(1, colour1);
        }
    }

    public void initialize() {
        ObservableList<Node> circles = anchorPane.getChildren();
        for (Node circle : circles) {
            if (circle.getId().startsWith("chitCard")) {
                circle.setOnMouseClicked(mouseEvent -> {
                    try {
                        flipCard(circle, circle.getId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        // add initial token locations to an array so token can go back to cave after winning
        caveLocationArray = new ArrayList<>();
        caveLocationArray.add(new double[]{dragonAnchorPane.getLayoutX(), dragonAnchorPane.getLayoutY()});
        caveLocationArray.add(new double[]{spiderAnchorPane.getLayoutX(), spiderAnchorPane.getLayoutY()});
        caveLocationArray.add(new double[]{salamanderAnchorPane.getLayoutX(), salamanderAnchorPane.getLayoutY()});
        caveLocationArray.add(new double[]{batAnchorPane.getLayoutX(), batAnchorPane.getLayoutY()});
        renderChits();
        renderVolcanoCards();
    }

    private void flipCard(Node circle, String id) throws IOException {
        if (animationInProgress) {
            return;
        }
        animationInProgress = false;

        // Rendering card flipping
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), circle);
        rotateTransition.setByAngle(180);
        rotateTransition.setAxis(Rotate.X_AXIS);

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));

        for (Node picture : anchorPane.getChildren()) {
            if (Objects.equals(picture.getId(), "picture" + id.substring(8))) {
                rotateTransition.setOnFinished(event -> {
                    endTurnButton.setDisable(false);
                    picture.setVisible(true);
                    pauseTransition.play();

                    try {
                        // Call the flipCard() to start turn logic
                        Game.getInstance().getCurrentPlayer().flipCard(Integer.parseInt(id.substring(8)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        endTurnButton.setDisable(true);
        circle.setDisable(true);
        rotateTransition.play();
    }

    public void hideCard() {
        endTurnButton.setDisable(true);
        ObservableList<Node> circles = anchorPane.getChildren();
        for (Node circle : circles) {
            if (circle.getId() != null) {
                if (circle.getId().startsWith("chitCard")) {
                    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), circle);
                    rotateTransition.setByAngle(180);
                    rotateTransition.setAxis(Rotate.X_AXIS);

                    rotateTransition.setOnFinished(event -> {
                        endTurnButton.setDisable(false);
                        for (Node picture : circles) {
                            if (picture.getId() != null) {
                                if (picture.getId().startsWith("picture")) {
                                    picture.setVisible(false);
                                }
                            }
                        }
                    });
                    rotateTransition.play();
                    circle.setDisable(false);
                }
            }
        }
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
                String fileNameValue = Integer.toString(Math.abs(chits.getChitCard(idNumber).getValue()));
                String fileName = fileNameKey + fileNameValue + ".png";

                // Create new image
                System.out.println("/org/openjfx/fierydragons/images/" + fileName);
                Image image = new Image(getClass().getResourceAsStream("/org/openjfx/fierydragons/images/" + fileName));
                ImageView imageView = new ImageView(image);
                imageView.setId("picture" + idString.substring(8));
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
        tileLocationArray = new ArrayList<>();
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

                String hello = mapPieces.get(i).getTiles().get(j).getTileType().toString();
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
                ArrayList<Double> location = new ArrayList<>();
                location.add(topLeftX);
                location.add(topLeftY);
                tileLocationArray.add(location);

                // Add image
                AnchorPane.setTopAnchor(imageView, topLeftY);
                AnchorPane.setLeftAnchor(imageView, topLeftX);
                anchorPane.getChildren().add(imageView);
            }
        }
    }

    public void renderDragonTokens() {
        int playerCount = Game.getInstance().getPlayerCount();
        switch (playerCount) {
            case 2:
                anchorPane.getChildren().remove(batAnchorPane);
                anchorPane.getChildren().remove(spiderAnchorPane);
            case 3:
                anchorPane.getChildren().remove(batAnchorPane);
            default:
                break;
        }
        // using player count generate starting positions for each token
        switch (playerCount) {
            case 2:
                locationIndexArray.add(18);
                locationIndexArray.add(6);
            case 3:
                locationIndexArray.add(18);
                locationIndexArray.add(0);
                locationIndexArray.add(6);
            case 4:
                locationIndexArray.add(18);
                locationIndexArray.add(0);
                locationIndexArray.add(6);
                locationIndexArray.add(12);
        }
    }

    public void displayPlayerCount(int playerCount) {
        playerCountLabel.setText("Current Players: " + playerCount);
        locationIndexArray = new ArrayList<>();
    }

    public static void movePlayer(Pair<TileType, Integer> chitCard) {
        int moveValue = chitCard.getValue();
        int playerId = Game.getInstance().getCurrentPlayer().getId();
        System.out.println(playerId + "moveplayer");
        BoardController instance = BoardController.getInstance();
        if (instance == null) {
            throw new IllegalStateException("BoardController instance is not initialized");
        }
        int newLocationIndex;
        ArrayList<Double> newLocation;
        switch (playerId) {
            case 1:
                newLocationIndex = locationIndexArray.get(playerId - 1) + moveValue;
                if (newLocationIndex > 23) {
                    newLocationIndex = newLocationIndex - 24;
                }
                if (newLocationIndex < 0) {
                    newLocationIndex = newLocationIndex + 24;
                }
                newLocation = tileLocationArray.get(newLocationIndex);
                instance.moveToken(instance.dragonAnchorPane, newLocation);
                locationIndexArray.set(playerId - 1, newLocationIndex);
                break;
            case 2:
                newLocationIndex = locationIndexArray.get(playerId - 1) + moveValue;
                if (newLocationIndex > 23) {
                    newLocationIndex = newLocationIndex - 24;
                }
                if (newLocationIndex < 0) {
                    newLocationIndex = newLocationIndex + 24;
                }
                newLocation = tileLocationArray.get(newLocationIndex);
                if (Game.getInstance().getPlayerCount() == 2) {
                    instance.moveToken(instance.salamanderAnchorPane, newLocation);
                    locationIndexArray.set(playerId - 1, newLocationIndex);
                    break;
                }
                instance.moveToken(instance.spiderAnchorPane, newLocation);
                locationIndexArray.set(playerId - 1, newLocationIndex);
                break;
            case 3:
                newLocationIndex = locationIndexArray.get(playerId - 1) + moveValue;
                if (newLocationIndex > 23) {
                    newLocationIndex = newLocationIndex - 24;
                }
                if (newLocationIndex < 0) {
                    newLocationIndex = newLocationIndex + 24;
                }
                newLocation = tileLocationArray.get(newLocationIndex);
                instance.moveToken(instance.salamanderAnchorPane, newLocation);
                locationIndexArray.set(playerId - 1, newLocationIndex);
                break;
            case 4:
                newLocationIndex = locationIndexArray.get(playerId - 1) + moveValue;
                if (newLocationIndex > 23) {
                    newLocationIndex = newLocationIndex - 24;
                }
                if (newLocationIndex < 0) {
                    newLocationIndex = newLocationIndex + 24;
                }
                newLocation = tileLocationArray.get(newLocationIndex);
                instance.moveToken(instance.batAnchorPane, newLocation);
                locationIndexArray.set(playerId - 1, newLocationIndex);
                break;
        }
    }

    public void moveToken(AnchorPane anchorPane, ArrayList<Double> newLocation) {
        anchorPane.setLayoutX(newLocation.get(0));
        anchorPane.setLayoutY(newLocation.get(1));
        anchorPane.toFront();
    }

    public void endTurn() {
        Turn.getInstance().endTurn();
        System.out.println("ending ?");
        showCurrentPlayer();
        hideCard();
    }
}
