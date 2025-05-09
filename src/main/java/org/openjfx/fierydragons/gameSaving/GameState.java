package org.openjfx.fierydragons.gameSaving;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javafx.scene.paint.Color;
import org.openjfx.fierydragons.entities.Deck;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;
import org.openjfx.fierydragons.render.BoardController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GameState {
    private Game game;
    private Board board;
    private BoardController boardController;

    private ArrayList<ArrayList<Double>> tileLocationArray;
    private ArrayList<Integer> locationIndexArray;
    private ArrayList<Integer> flippedCardId;
    private Map<Integer, String> playerAnchorPaneMap;

    @JsonCreator
    public GameState(@JsonProperty("game") Game game,
                     @JsonProperty("board") Board board,
                     @JsonProperty("boardController") BoardController boardController,
                     @JsonProperty("tileLocationArray") ArrayList<ArrayList<Double>> tileLocationArray,
                     @JsonProperty("locationIndexArray") ArrayList<Integer> locationIndexArray,
                     @JsonProperty("flippedCardId") ArrayList<Integer> flippedCardId,
                     @JsonProperty("playerAnchorPaneMap") Map<Integer, String> playerAnchorPaneMap) {
        this.game = game;
        this.board = board;
        this.boardController = boardController;
        this.tileLocationArray = tileLocationArray;
        this.locationIndexArray = locationIndexArray;
        this.flippedCardId = flippedCardId;
        this.playerAnchorPaneMap = playerAnchorPaneMap;
    }

    public ArrayList<Integer> getFlippedCardId() {
        return flippedCardId;
    }

    public void setFlippedCardId(ArrayList<Integer> flippedCardId) {
        this.flippedCardId = flippedCardId;
    }

    public ArrayList<ArrayList<Double>> getTileLocationArray() {
        return tileLocationArray;
    }

    public void setTileLocationArray(ArrayList<ArrayList<Double>> tileLocationArray) {
        this.tileLocationArray = tileLocationArray;
    }

    public ArrayList<Integer> getLocationIndexArray() {
        return locationIndexArray;
    }

    public void setLocationIndexArray(ArrayList<Integer> locationIndexArray) {
        this.locationIndexArray = locationIndexArray;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Map<Integer, String> getPlayerAnchorPaneMap() {
        return playerAnchorPaneMap;
    }

    public void setPlayerAnchorPaneMap(Map<Integer, String> playerAnchorPaneMap) {
        this.playerAnchorPaneMap = playerAnchorPaneMap;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Writes what is labeled with JsonPropertys to a JSON file, effectively saving the game
     */
    public void saveGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), this);
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Loads the game from a previously saved JSON file. Returns a GameState object which contains all of the
     * save information.
     */
    public static GameState loadGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());
        module.addDeserializer(Color.class, new ColorDeserializer());
        objectMapper.registerModule(module);

        return objectMapper.readValue(new File(filePath), GameState.class);
    }
}
