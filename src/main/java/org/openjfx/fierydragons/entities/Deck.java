package org.openjfx.fierydragons.entities;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Pair<TileType, Integer>> chitCards;

    public Deck() {
        this.chitCards = new ArrayList<>();
        generateChitCards();
    }

    /**
     * @author  Jeffrey Yan
     * @desc    creates chit cards and shuffles
     */
    private void generateChitCards() {
        this.chitCards.add(new Pair<>(TileType.BABY_DRAGON, 1));
        this.chitCards.add(new Pair<>(TileType.BABY_DRAGON, 2));
        this.chitCards.add(new Pair<>(TileType.BABY_DRAGON, 3));
        this.chitCards.add(new Pair<>(TileType.BAT, 1));
        this.chitCards.add(new Pair<>(TileType.BAT, 2));
        this.chitCards.add(new Pair<>(TileType.BAT, 3));
        this.chitCards.add(new Pair<>(TileType.SALAMANDER, 1));
        this.chitCards.add(new Pair<>(TileType.SALAMANDER, 2));
        this.chitCards.add(new Pair<>(TileType.SALAMANDER, 3));
        this.chitCards.add(new Pair<>(TileType.SPIDER, 1));
        this.chitCards.add(new Pair<>(TileType.SPIDER, 2));
        this.chitCards.add(new Pair<>(TileType.SPIDER, 3));
        this.chitCards.add(new Pair<>(TileType.PIRATE, -1));
        this.chitCards.add(new Pair<>(TileType.PIRATE, -2));
        this.chitCards.add(new Pair<>(TileType.PIRATE, -1));
        this.chitCards.add(new Pair<>(TileType.PIRATE, -2));
        //Collections.shuffle(this.chitCards);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    takes an index in the chit cards array list and returns the corresponding chit card
     */
    public Pair<TileType, Integer> getChitCard(int index) {
        return chitCards.get(index);
    }
}
