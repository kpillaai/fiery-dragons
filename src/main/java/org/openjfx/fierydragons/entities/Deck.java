package org.openjfx.fierydragons.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.openjfx.fierydragons.CustomPair;
import org.openjfx.fierydragons.GameState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<CustomPair<TileType, Integer>> chitCards;

    @JsonCreator
    public Deck() {
        this.chitCards = new ArrayList<>();
        generateChitCards();
    }

    public List<CustomPair<TileType, Integer>> getChitCards() {
        return chitCards;
    }

    public void setChitCards(List<CustomPair<TileType, Integer>> chitCards) {
        this.chitCards = chitCards;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    creates chit cards and shuffles
     */
    private void generateChitCards() {
        this.chitCards.add(new CustomPair<>(TileType.BABY_DRAGON, 1));
        this.chitCards.add(new CustomPair<>(TileType.BABY_DRAGON, 2));
        this.chitCards.add(new CustomPair<>(TileType.BABY_DRAGON, 3));
        this.chitCards.add(new CustomPair<>(TileType.BAT, 1));
        this.chitCards.add(new CustomPair<>(TileType.BAT, 2));
        this.chitCards.add(new CustomPair<>(TileType.BAT, 3));
        this.chitCards.add(new CustomPair<>(TileType.SALAMANDER, 1));
        this.chitCards.add(new CustomPair<>(TileType.SALAMANDER, 2));
        this.chitCards.add(new CustomPair<>(TileType.SALAMANDER, 3));
        this.chitCards.add(new CustomPair<>(TileType.SPIDER, 1));
        this.chitCards.add(new CustomPair<>(TileType.SPIDER, 2));
        this.chitCards.add(new CustomPair<>(TileType.SPIDER, 3));
        this.chitCards.add(new CustomPair<>(TileType.PIRATE, -1));
        this.chitCards.add(new CustomPair<>(TileType.PIRATE, -2));
        this.chitCards.add(new CustomPair<>(TileType.PIRATE, -1));
        this.chitCards.add(new CustomPair<>(TileType.PIRATE, -2));
        this.chitCards.add(new CustomPair<>(TileType.SWAP, 0));
        this.chitCards.add(new CustomPair<>(TileType.SWAP, 0));
        //Collections.shuffle(this.chitCards);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    takes an index in the chit cards array list and returns the corresponding chit card
     */
    public CustomPair<TileType, Integer> getChitCard(int index) {
        return chitCards.get(index);
    }

    // Save the entire game state to a JSON file
    public void saveGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), this);
    }

    // Load the entire game state from a JSON file
    public static Deck loadGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Deck.class);
    }
}
