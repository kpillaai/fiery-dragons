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
        generateChitCards();
    }

    public List<CustomPair<TileType, Integer>> getChitCards() {
        return chitCards;
    }

    public void setChitCards(List<CustomPair<TileType, Integer>> chitCards) {
        this.chitCards = chitCards;
    }

    public void resetDeck() {
        generateChitCards();
    }

    /**
     * @author  Jeffrey Yan
     * @desc    creates chit cards and shuffles
     */
    private void generateChitCards() {
        this.chitCards = new ArrayList<>();
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
        Collections.shuffle(this.chitCards);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    takes an index in the chit cards array list and returns the corresponding chit card
     */
    public CustomPair<TileType, Integer> getChitCard(int index) {
        return chitCards.get(index);
    }
}
