package org.openjfx.fierydragons.render;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.openjfx.fierydragons.CustomPair;
import org.openjfx.fierydragons.GameState;
import org.openjfx.fierydragons.StartApplication;
import org.openjfx.fierydragons.entities.Deck;
import org.openjfx.fierydragons.entities.MapPiece;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;
import org.openjfx.fierydragons.game.Turn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class BoardController   {

    @FXML
    @JsonIgnore
    public AnchorPane anchorPane, dragonAnchorPane, spiderAnchorPane, salamanderAnchorPane, batAnchorPane;

    @FXML
    @JsonIgnore
    private Label playerCountLabel, currentPlayerLabel;

    @FXML
    @JsonIgnore
    private Parent root;

    @FXML
    private Label timeRemainingText;

    @FXML
    @JsonIgnore
    private Button endTurnButton, saveGameButton;

    @JsonIgnore
    private FXMLLoader fxmlLoader;

    @JsonIgnore
    private Stage stage;

    @JsonIgnore
    private Scene scene;

    @JsonIgnore
    private boolean animationInProgress = false;

    @JsonProperty("tileLocationArray")
    private static ArrayList<ArrayList<Double>> tileLocationArray;

    private ArrayList<double[]> caveLocationArray;

    private ArrayList<Color> playerColours;

    private static BoardController instance;

    @JsonProperty("locationIndexArray")
    protected static ArrayList<Integer> locationIndexArray;

    @JsonProperty("flippedCardId")
    private static ArrayList<Integer> flippedCardId;

    @JsonCreator
    public BoardController() {
        instance = this;
    }

    public static synchronized BoardController getInstance() {
        if (instance == null) {
            instance = new BoardController();
        }
        return instance;
    }

    public static void setInstance(BoardController instance) {
        BoardController.instance = instance;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Renders the winning game token back in its own cave, and switches to win scene.
     */
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
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
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

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Displays current player's turn on scene.
     */
    public void showCurrentPlayer() {
        currentPlayerLabel.setText(Game.getInstance().getCurrentPlayer().getName());
        currentPlayerLabel.setTextFill(playerColours.get(Game.getInstance().getCurrentPlayer().getId() - 1));
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Token colours are hard-coded in game-board.fxml, this array is used to store the associated colours for
     *          showCurrentPlayer().
     */
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

    /**
     * @author  Zilei Chen, Jeffrey Yan
     * @desc    Function is used when scene is initially generated; sets chit card ids and finds initial token
     *          coordinates.
     */
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
        if (caveLocationArray == null) {
            caveLocationArray = new ArrayList<>();
            caveLocationArray.add(new double[]{dragonAnchorPane.getLayoutX(), dragonAnchorPane.getLayoutY()});
            caveLocationArray.add(new double[]{spiderAnchorPane.getLayoutX(), spiderAnchorPane.getLayoutY()});
            caveLocationArray.add(new double[]{salamanderAnchorPane.getLayoutX(), salamanderAnchorPane.getLayoutY()});
            caveLocationArray.add(new double[]{batAnchorPane.getLayoutX(), batAnchorPane.getLayoutY()});
        }
        renderChits();
        renderVolcanoCards();
        if (locationIndexArray != null) {
            updatePlayerLocation();
        }
    }

    public void initialiseFlippedCards() {
        if (flippedCardId != null && !flippedCardId.isEmpty()) {
            for (Node node : anchorPane.getChildren()) {
                if (node.getId() != null) {
                    if (node.getId().startsWith("chitCard")) {
                        if (flippedCardId.contains(parseInt(node.getId().substring(8)))) {
                            // disable the circle
                            node.setDisable(true);
                        }
                    }
                    if (node.getId().startsWith("picture")) {
                        if (flippedCardId.contains(parseInt(node.getId().substring(7)))) {
                            // show the picture
                            node.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * @author  Zilei Chen
     * @desc    Displays the animation for a card being flipped and begins the turn process
     */
    private void flipCard(Node circle, String id) throws IOException {
        if (animationInProgress) {
            return;
        }
        animationInProgress = false;

        if (flippedCardId == null) {
            flippedCardId = new ArrayList<>();
        }

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

                    flippedCardId.add(parseInt(id.substring(8)));

                    try {
                        // Call the flipCard() to start turn logic
                        Game.getInstance().getCurrentPlayer().flipCard(parseInt(id.substring(8)));
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

    /**
     * @author  Zilei Chen
     * @desc    Hides all chit cards after a player turn has ended.
     */
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
        flippedCardId.clear();
    }

    /**
     * @author  Zilei Chen
     * @desc    Renders initial chit cards flipped down on the game board and adds the associated image to each chit
     *          card according to their values.
     */
    private void renderChits() {
        ObservableList<Node> circles = anchorPane.getChildren();
        Deck chits = Board.getInstance().getDeck();

        ObservableList<Node> images = FXCollections.observableArrayList();

        // Adding Chit card images
        for (Node chitCard: circles) {
            if (chitCard.getId().startsWith("chitCard")) {
                // Generating filepath
                String idString = chitCard.getId();
                int idNumber = parseInt(idString.substring(8));

                String fileNameKey = chits.getChitCard(idNumber).getKey().toString().toLowerCase();
                String fileNameValue = Integer.toString(Math.abs(chits.getChitCard(idNumber).getValue()));
                String fileName = fileNameKey + fileNameValue + ".png";

                // Create new image
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

    /**
     * @author  Krishna Pillaai Manogaran, Zilei Chen, Jeffrey Yan
     * @desc    Renders all volcano cards on the game board; stores each tile's location into an array so that it can be
     *          referenced when moving dragon tokens.
     */
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
                // Add to array so tokens know the co-ordinates to move to
                tileLocationArray.add(location);

                // Add image
                AnchorPane.setTopAnchor(imageView, topLeftY);
                AnchorPane.setLeftAnchor(imageView, topLeftX);
                anchorPane.getChildren().add(imageView);
            }
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Dragon tokens are hard-coded in game-board.fxml, function's purpose is to remove unnecessary dragon
     *          tokens. Also based on number of players, initialise an array based on relative token position.
     *          locationIndexArray stores indexes that refer to tileLocationArray, so tokens can be moved to the correct
     *          co-ordinates during turns.
     */
    public void renderDragonTokens() {
        int playerCount = Game.getInstance().getPlayerCount();
        switch (playerCount) {
            case 2:
                // Case where two players, start opposite to each other
                anchorPane.getChildren().remove(batAnchorPane);
                anchorPane.getChildren().remove(spiderAnchorPane);
                break;
            case 3:
                anchorPane.getChildren().remove(batAnchorPane);
                break;
            default:
                break;
        }
        if (locationIndexArray == null) {
            locationIndexArray = new ArrayList<>();
            // using player count generate starting positions for each token
            switch (playerCount) {
                case 2:
                    // Case where two players, start opposite to each other
                    locationIndexArray.add(18);
                    locationIndexArray.add(6);
                    break;
                case 3:
                    locationIndexArray.add(18);
                    locationIndexArray.add(0);
                    locationIndexArray.add(6);
                    break;
                case 4:
                    locationIndexArray.add(18);
                    locationIndexArray.add(0);
                    locationIndexArray.add(6);
                    locationIndexArray.add(12);
                    break;
            }
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Show number of players in a game after initialisation.
     */
    public void showPlayerCount(int playerCount) {
        playerCountLabel.setText("Number of Players: " + playerCount);
    }

    public void updatePlayerLocation() {
        ArrayList<AnchorPane> anchorPanes = new ArrayList<>();
        switch (locationIndexArray.size()) {
            case 2:
                anchorPanes.add(BoardController.getInstance().getDragonAnchorPane());
                anchorPanes.add(BoardController.getInstance().getSalamanderAnchorPane());
                for (int i = 0; i < locationIndexArray.size(); i++) {
                    // need to check if still in caves using Board and playerLocationArray.get(i)[1]
                    if (Board.getInstance().getPlayerLocationArray().get(i)[1] != -1) {
                        // adds them to tile on the board depending on locationIndexArray
                        instance.moveToken(anchorPanes.get(i), tileLocationArray.get(locationIndexArray.get(i)));
                    }
                }
                break;
            case 3:
            case 4:
                anchorPanes.add(BoardController.getInstance().getDragonAnchorPane());
                anchorPanes.add(BoardController.getInstance().getSpiderAnchorPane());
                anchorPanes.add(BoardController.getInstance().getSalamanderAnchorPane());
                if (locationIndexArray.size() == 4) {
                    anchorPanes.add(BoardController.getInstance().getBatAnchorPane());
                }
                for (int i = 0; i < locationIndexArray.size(); i++) {
                    // need to check if still in caves using Board and playerLocationArray.get(i)[1]
                    if (Board.getInstance().getPlayerLocationArray().get(i)[1] != -1) {
                        // adds them to tile on the board depending on locationIndexArray
                        instance.moveToken(anchorPanes.get(i), tileLocationArray.get(locationIndexArray.get(i)));
                    }
                }
                break;
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Given chit card as input, function renders the dragon token location of the player as according to game
     *          state.
     *          First determine new index from chit card value, then based on index, find the correct co-ordinates from
     *          tileLocationArray.
     */
    public static void movePlayer(CustomPair<TileType, Integer> chitCard) {
        int moveValue = chitCard.getValue();
        int playerId = Game.getInstance().getCurrentPlayer().getId();
        BoardController instance = BoardController.getInstance();
        if (instance == null) {
            throw new IllegalStateException("BoardController instance is not initialized");
        }
        // updatePlayerLocation();
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
                // If only two players, player 2 has a different token
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

    /**
     * @author  Jeffrey Yan
     * @desc    Helper function used in movePlayer(). Given the object to move and location to move to, move the object.
     */
    public void moveToken(AnchorPane anchorPane, ArrayList<Double> newLocation) {
        anchorPane.setLayoutX(newLocation.get(0));
        anchorPane.setLayoutY(newLocation.get(1));
        anchorPane.toFront();
    }

    /**
     * @author  Zilei Chen
     * @desc    endTurn() function called by end turn button in the scene.
     */
    public void endTurn() {
        Turn.getInstance().endTurn();
        showCurrentPlayer();
        hideCard();
    }

    @FXML
    private void onSaveGameClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));

        Stage stage = (Stage) saveGameButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                GameState gameState = new GameState(Board.getInstance().getDeck(),
                        Game.getInstance(),
                        Board.getInstance(),
                        BoardController.getInstance(),
                        tileLocationArray,
                        locationIndexArray,
                        flippedCardId);
                gameState.saveGame(file.getAbsolutePath());
                System.out.println("Game saved successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle error saving game state
            }
        }
    }

    public static ArrayList<ArrayList<Double>> getTileLocationArray() {
        return tileLocationArray;
    }

    public static void setTileLocationArray(ArrayList<ArrayList<Double>> tileLocationArray) {
        BoardController.tileLocationArray = tileLocationArray;
    }

    public ArrayList<double[]> getCaveLocationArray() {
        return caveLocationArray;
    }

    public void setCaveLocationArray(ArrayList<double[]> caveLocationArray) {
        this.caveLocationArray = caveLocationArray;
    }

    public ArrayList<Color> getPlayerColours() {
        return playerColours;
    }

    public void setPlayerColours(ArrayList<Color> playerColours) {
        this.playerColours = playerColours;
    }

    public static ArrayList<Integer> getLocationIndexArray() {
        return locationIndexArray;
    }

    public static void setLocationIndexArray(ArrayList<Integer> locationIndexArray) {
        BoardController.locationIndexArray = locationIndexArray;
    }

    public AnchorPane getDragonAnchorPane() {
        return dragonAnchorPane;
    }

    public void setDragonAnchorPane(AnchorPane dragonAnchorPane) {
        this.dragonAnchorPane = dragonAnchorPane;
    }

    public AnchorPane getSpiderAnchorPane() {
        return spiderAnchorPane;
    }

    public void setSpiderAnchorPane(AnchorPane spiderAnchorPane) {
        this.spiderAnchorPane = spiderAnchorPane;
    }

    public AnchorPane getSalamanderAnchorPane() {
        return salamanderAnchorPane;
    }

    public void setSalamanderAnchorPane(AnchorPane salamanderAnchorPane) {
        this.salamanderAnchorPane = salamanderAnchorPane;
    }

    public AnchorPane getBatAnchorPane() {
        return batAnchorPane;
    }

    public void setBatAnchorPane(AnchorPane batAnchorPane) {
        this.batAnchorPane = batAnchorPane;
    }

    public ArrayList<Integer> getFlippedCardId() {
        return flippedCardId;
    }

    public void setFlippedCardId(ArrayList<Integer> flippedCardId) {
        this.flippedCardId = flippedCardId;
    }
}
