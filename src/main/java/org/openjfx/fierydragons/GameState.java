package org.openjfx.fierydragons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.Deck;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;

import java.io.File;
import java.io.IOException;

public class GameState {
    private Deck deck;
    private Game game;
    private Board board;
    // private Turn turn;


    @JsonCreator
    public GameState(@JsonProperty("deck") Deck deck, @JsonProperty("game") Game game, @JsonProperty("board") Board board) {
        this.deck = deck;
        this.game = game;
        this.board = board;
        // this.turn = turn;
        // @JsonProperty("turn") Turn turn

    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

//    public Turn getTurn() {
//        return turn;
//    }
//
//    public void setTurn(Turn turn) {
//        this.turn = turn;
//    }

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

    // Save the entire game state to a JSON file
    public void saveGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), this);
    }

    // Load the entire game state from a JSON file
    public static GameState loadGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), GameState.class);
    }
}
