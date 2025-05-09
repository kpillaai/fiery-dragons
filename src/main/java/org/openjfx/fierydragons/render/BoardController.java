package org.openjfx.fierydragons.render;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.openjfx.fierydragons.gameSaving.CustomPair;
import org.openjfx.fierydragons.gameSaving.GameState;
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
import java.lang.reflect.Field;
import java.util.*;

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
    private Button endTurnButton, saveGameButton, backToStartButton;

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

    private ArrayList<ArrayList<Double>> caveLocationArray;

    private ArrayList<Color> playerColours;

    @JsonProperty("playerAnchorPaneMap")
    private static Map<Integer, String> playerAnchorPaneMap;

    private static BoardController instance;

    @JsonProperty("locationIndexArray")
    protected static ArrayList<Integer> locationIndexArray;

    @JsonProperty("flippedCardId")
    private static ArrayList<Integer> flippedCardId;

    private TimerController timerController;

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
     * @desc    Reset the instance of the Board Controller
     */
    public static void resetBoardController() {
        instance = null;
        tileLocationArray = null;
        locationIndexArray = null;
        flippedCardId = null;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Switches back to the starting screen, so that can exit the game after saving
     */
    public void switchToStartScene(ActionEvent event) throws IOException {
        timerController.stopTimer();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you want to return? Any unsaved changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty()) {
            //
        } else if (result.get() == ButtonType.OK) {
            Game.resetGame();
            Board.resetBoard();
            BoardController.resetBoardController();
            Turn.resetTurn();
            Game.getInstance().initialise();


            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("main-menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Renders the winning game token back in its own cave, and switches to win scene.
     */
    public void switchToWinScene(Node node, Player winningPlayer) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (winningPlayer.getId() > 0) {
            // move player's tile back to own cave
            int playerId = winningPlayer.getId();
            AnchorPane playerAnchorPane = (AnchorPane) getAnchorPane(playerAnchorPaneMap.get(playerId));
            moveToken(playerAnchorPane, caveLocationArray.get(playerId - 1));
        }
        try {
            // load the win scene
            fxmlLoader = new FXMLLoader(StartApplication.class.getResource("win-scene.fxml"));
            root = fxmlLoader.load();

            // send the player name to the WinSceneController to display they have won the game
            WinSceneController winSceneController = fxmlLoader.getController();

            if (winningPlayer.getId() < 0) {
                winSceneController.displayDraw(winningPlayer.getName());
            } else {
                String nameText = winningPlayer.getName();
                winSceneController.displayName(nameText);
            }


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
        Player currentPlayer = Game.getInstance().getCurrentPlayer();
        currentPlayerLabel.setText(currentPlayer.getName());
        currentPlayerLabel.setTextFill(playerColours.get(currentPlayer.getId() - 1));
        //this.renderPlayerTimer(currentPlayer.getTimeRemainingSeconds());
    }

    private void renderPlayerTimer(int timeRemaining) {
        TimerController timerController = new TimerController(timeRemaining, timeRemainingText);
        timerController.startTimer();
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
    public void initialize() throws NoSuchFieldException, IllegalAccessException {
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
        renderChits();
        renderVolcanoCards();
        if (locationIndexArray != null) {
            updatePlayerLocation();
        }

        // Starting timer
        if (Game.getInstance().getCurrentPlayer() == null) {
            this.timerController = new TimerController(150, timeRemainingText);
        } else {
            this.timerController = new TimerController(Game.getInstance().getCurrentPlayer().getTimeRemainingSeconds(), timeRemainingText);
        }
        timerController.startTimer();
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    If there are saved cards that are flipped, it will flip those cards
     */
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
                    } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
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
        if (flippedCardId != null && !flippedCardId.isEmpty() ) {
            flippedCardId.clear();
        }
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

        int numCards = Board.getInstance().getDeck().getChitCards().size();
        // Adding Chit card images

        for (Node chitCard: circles) {
            if (chitCard.getId().startsWith("chitCard") && numCards > 0) {
                numCards -= 1;
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
        double outerRadius = 385;
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
        caveLocationArray = new ArrayList<>();
        int loopCounter = -1;
        for (int i = 0; i < mapPieces.size(); i++) {
            for (int j = 0; j < mapPieces.get(i).getTiles().size(); j++) {

                // Calculating angles
                double endAngle = loopCounter * pieceAngle + offsetAngle;
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

                // checks to see if j is about to be the same as the number of tiles, if yes, it adds a line as that
                // mapPiece is about to finish printing all its images
                if (j + 1 == mapPieces.get(i).getTiles().size()) {
                    sliceLine.setStrokeWidth(4);
                }

                // Generating Animals for each tile
                double animalX = centreX + Math.cos(Math.toRadians(loopCounter * pieceAngle)) * middleRadius;
                double animalY = centreY + Math.sin(Math.toRadians(loopCounter * pieceAngle)) * middleRadius;

                String hello = mapPieces.get(i).getTiles().get(j).getTileType().toString();
                String filePath = "/org/openjfx/fierydragons/images/" + mapPieces.get(i).getTiles().get(j).getTileType().toString().toLowerCase() + "1.png";

                // Create new image
                Image image = new Image(getClass().getResourceAsStream(filePath));
                ImageView imageView = new ImageView(image);
                imageView.setVisible(true);
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                // Add image
                double layoutX = animalX - imageView.getFitWidth() / 2;
                double layoutY = animalY - imageView.getFitHeight() / 2;
                imageView.setLayoutX(layoutX);
                imageView.setLayoutY(layoutY);
                anchorPane.getChildren().add(imageView);
                // Add to array so tokens know the co-ordinates to move to
                ArrayList<Double> location = new ArrayList<>();
                location.add(layoutX);
                location.add(layoutY);
                tileLocationArray.add(location);

                loopCounter += 1;

                // Add Cave (if exist)
                if (mapPieces.get(i).getCave() != null && mapPieces.get(i).getCaveIndex() == j) {
                    // generate circle
                    Circle caveCircle = new Circle();
                    caveCircle.setRadius(55);
                    caveCircle.setFill(Color.CHOCOLATE);
                    caveCircle.setStrokeWidth(5);

                    //generate image of circle
                    String filePathCaveImage = "/org/openjfx/fierydragons/images/" + mapPieces.get(i).getCave().getTileType().toString().toLowerCase() + "1.png";
                    Image imageCave = new Image(getClass().getResourceAsStream(filePathCaveImage));
                    ImageView imageViewCave = new ImageView(imageCave);
                    imageViewCave.setVisible(true);
                    imageViewCave.setFitHeight(80);
                    imageViewCave.setFitWidth(80);

                    //find specific coordinates
                    int caveOffsetDistance = 118;
                    double caveCoordX = centreX + Math.cos(Math.toRadians((loopCounter - 1)* pieceAngle)) * (middleRadius + caveOffsetDistance);
                    double caveCoordY = centreY + Math.sin(Math.toRadians((loopCounter - 1)* pieceAngle)) * (middleRadius + caveOffsetDistance);
                    caveCircle.setLayoutX(caveCoordX);
                    caveCircle.setLayoutY(caveCoordY);
                    imageViewCave.setLayoutX(caveCoordX - imageViewCave.getFitWidth() / 2);
                    imageViewCave.setLayoutY(caveCoordY - imageViewCave.getFitHeight() / 2);

                    //add circle + image to that location
                    anchorPane.getChildren().add(caveCircle);
                    anchorPane.getChildren().add(imageViewCave);

                    double caveCentreOffset = 25;
                    ArrayList<Double> caveLocation = new ArrayList<>();
                    caveLocation.add(caveCoordX - caveCentreOffset);
                    caveLocation.add(caveCoordY - caveCentreOffset);
                    caveLocationArray.add(caveLocation);
                }
            }
        }
        ArrayList<Double> lastLocation = caveLocationArray.removeLast();
        caveLocationArray.add(0, lastLocation);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Dragon tokens are hard-coded in game-board.fxml, function's purpose is to remove unnecessary dragon
     *          tokens. Also based on number of players, initialise an array based on relative token position.
     *          locationIndexArray stores indexes that refer to tileLocationArray, so tokens can be moved to the correct
     *          co-ordinates during turns.
     *          Also initialises a HashMap to store player and their AnchorPane tokens as key,value pairs.
     */
    public void renderDragonTokens() throws NoSuchFieldException, IllegalAccessException {
        int playerCount = Game.getInstance().getPlayerCount();
        if (locationIndexArray == null) {
            locationIndexArray = new ArrayList<>();
            playerAnchorPaneMap = new HashMap<>();
            switch (playerCount) {
                case 2:
                    // Case where two players, start opposite to each other
                    playerAnchorPaneMap.put(1, "dragon");
                    playerAnchorPaneMap.put(2, "salamander");

                    for (int i = 0; i < playerCount; i++) {
                        int[] player1Location = Board.getInstance().getPlayerLocationArray().get(i);
                        locationIndexArray.add(Board.getInstance().getCaveTileLocation(player1Location) - 1);
                    }
                    break;
                case 3:
                    playerAnchorPaneMap.put(1, "dragon");
                    playerAnchorPaneMap.put(2, "spider");
                    playerAnchorPaneMap.put(3, "salamander");
                    for (int i = 0; i < playerCount; i++) {
                        int[] player1Location = Board.getInstance().getPlayerLocationArray().get(i);
                        locationIndexArray.add(Board.getInstance().getCaveTileLocation(player1Location) - 1);
                    }
                    break;
                case 4:
                    playerAnchorPaneMap.put(1, "dragon");
                    playerAnchorPaneMap.put(2, "spider");
                    playerAnchorPaneMap.put(3, "salamander");
                    playerAnchorPaneMap.put(4, "bat");
                    for (int i = 0; i < playerCount; i++) {
                        int[] player1Location = Board.getInstance().getPlayerLocationArray().get(i);
                        locationIndexArray.add(Board.getInstance().getCaveTileLocation(player1Location) - 1);
                    }
                    break;

            }
        }
        if (playerCount == 2) {
            caveLocationArray.set(1, caveLocationArray.get(2));
            anchorPane.getChildren().remove(batAnchorPane);
            anchorPane.getChildren().remove(spiderAnchorPane);

        }
        if (playerCount == 3) {
            anchorPane.getChildren().remove(batAnchorPane);
        }
        for (int i = 0; i <  playerCount; i++) {
            AnchorPane playerAnchorPane = (AnchorPane) getAnchorPane(playerAnchorPaneMap.get(i+1));
            moveToken(playerAnchorPane, caveLocationArray.get(i));
        }
        updatePlayerLocation();
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Show number of players in a game after initialisation.
     */
    public void showPlayerCount(int playerCount) {
        playerCountLabel.setText("Number of Players: " + playerCount);
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Updates player token locations after loading a game.
     */
    public void updatePlayerLocation() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<AnchorPane> anchorPanes = new ArrayList<>();
        for (int i = 0; i < locationIndexArray.size(); i++) {
            // need to check if still in caves using Board and playerLocationArray.get(i)[1]
            AnchorPane playerAnchorPane = (AnchorPane) getAnchorPane(playerAnchorPaneMap.get(i + 1));
            if (Board.getInstance().getPlayerLocationArray().get(i)[1] != -1) {
                // adds them to tile on the board depending on locationIndexArray
                instance.moveToken(playerAnchorPane, tileLocationArray.get(locationIndexArray.get(i)));
            }
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Given a string, get the corresponding anchorPane
     *          Uses field.get() as AnchorPanes are not serialisable when trying to save the game, therefore a string is
     *          stored in a Map instead.
     */
    public Object getAnchorPane(String creature) throws NoSuchFieldException, IllegalAccessException {
        String fieldName = creature + "AnchorPane";
        Field field = this.getClass().getDeclaredField(fieldName);
        return field.get(this);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Given chit card as input, function renders the dragon token location of the player as according to game
     *          state.
     *          First determine new index from chit card value, then based on index, find the correct co-ordinates from
     *          tileLocationArray.
     */
    public static void movePlayer(CustomPair<TileType, Integer> chitCard) throws NoSuchFieldException, IllegalAccessException {
        int moveValue = chitCard.getValue();
        int playerId = Game.getInstance().getCurrentPlayer().getId();
        BoardController instance = BoardController.getInstance();
        if (instance == null) {
            throw new IllegalStateException("BoardController instance is not initialized");
        }
        //updatePlayerLocation();

        AnchorPane playerAnchorPane = (AnchorPane) instance.getAnchorPane(playerAnchorPaneMap.get(playerId));
        int newLocationIndex = locationIndexArray.get(playerId - 1) + moveValue;
        if (newLocationIndex > Board.getInstance().getNumTiles() - 1) {
            newLocationIndex = newLocationIndex - Board.getInstance().getNumTiles();
        }
        if (newLocationIndex < 0) {
            newLocationIndex = newLocationIndex + Board.getInstance().getNumTiles();
        }
        ArrayList<Double> newLocation = tileLocationArray.get(newLocationIndex);
        instance.moveToken(playerAnchorPane, newLocation);
        locationIndexArray.set(playerId - 1, newLocationIndex);
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
    public void endTurn() throws IOException, NoSuchFieldException, IllegalAccessException {
        pauseTimer();
        if (Turn.getInstance().endTurn()) {
            showCurrentPlayer();
            hideCard();
            resumeTimer();
        }
    }

    /**
     * @author  Zilei Chen
     * @desc    Pauses the time on the screen, and saves the time to the player
     */
    public void pauseTimer() {
        // Stop timer when turn ends
        timerController.stopTimer();
        Game.getInstance().getCurrentPlayer().setTimeRemainingSeconds(timerController.getTimeRemainingSeconds());
    }

    /**
     * @author  Zilei Chen
     * @desc    Resumes the timer for the current player on the screen
     */
    public void resumeTimer() {
        //When the next player is iterated, start new timer
        timerController.setTimeRemainingSeconds(Game.getInstance().getCurrentPlayer().getTimeRemainingSeconds());
        timerController.startTimer();
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Saves the game when the save game button is clicked. Asks where to save it and uses the GameState to
     * capture the important information
     */
    @FXML
    private void onSaveGameClick() {
        String jarDir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));

        fileChooser.setInitialDirectory(new File(jarDir));

        Stage stage = (Stage) saveGameButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                GameState gameState = new GameState(Game.getInstance(),
                        Board.getInstance(),
                        BoardController.getInstance(),
                        tileLocationArray,
                        locationIndexArray,
                        flippedCardId,
                        playerAnchorPaneMap);
                gameState.saveGame(file.getAbsolutePath());
                System.out.println("Game saved successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle error saving game state
            }
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Takes two players as input, and swaps token locations and corresponding stored locations.
     * param:   player1 player to swap
     * param:   player2 player to swap
     */
    public void swapPlayerToken(Player player1, Player player2) throws NoSuchFieldException, IllegalAccessException {
        AnchorPane player1AnchorPane = (AnchorPane) getAnchorPane(playerAnchorPaneMap.get(player1.getId()));
        AnchorPane player2AnchorPane = (AnchorPane) getAnchorPane(playerAnchorPaneMap.get(player2.getId()));

        int player1Index = player1.getId() - 1;
        int player2Index = player2.getId() - 1;

        // Retrieve the current tile indices of the players
        int player1CurrentTile = locationIndexArray.get(player1Index);
        int player2CurrentTile = locationIndexArray.get(player2Index);

        // Swap the location indices in the locationIndexArray
        locationIndexArray.set(player1Index, player2CurrentTile);
        locationIndexArray.set(player2Index, player1CurrentTile);

        // Retrieve the new locations from the tileLocationArray
        ArrayList<Double> player1NewTileLocation = tileLocationArray.get(player2CurrentTile);
        ArrayList<Double> player2NewTileLocation = tileLocationArray.get(player1CurrentTile);

        // Move the tokens to their new locations
        BoardController.getInstance().moveToken(player1AnchorPane, player1NewTileLocation);
        BoardController.getInstance().moveToken(player2AnchorPane, player2NewTileLocation);

        // Update the tileLocationArray to reflect the new positions
        tileLocationArray.set(player2CurrentTile, player1NewTileLocation);
        tileLocationArray.set(player1CurrentTile, player2NewTileLocation);
    }

    public static ArrayList<ArrayList<Double>> getTileLocationArray() {
        return tileLocationArray;
    }

    public static void setTileLocationArray(ArrayList<ArrayList<Double>> tileLocationArray) {
        BoardController.tileLocationArray = tileLocationArray;
    }

    public ArrayList<ArrayList<Double>> getCaveLocationArray() {
        return caveLocationArray;
    }

    public void setCaveLocationArray(ArrayList<ArrayList<Double>> caveLocationArray) {
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

    public static Map<Integer, String> getPlayerAnchorPaneMap() {
        return playerAnchorPaneMap;
    }

    public static void setPlayerAnchorPaneMap(Map<Integer, String> playerAnchorPaneMap) {
        BoardController.playerAnchorPaneMap = playerAnchorPaneMap;
    }
}
