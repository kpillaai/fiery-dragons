package org.openjfx.fierydragons.game;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.MapPiece;
import org.openjfx.fierydragons.entities.TileType;

import java.util.*;

public class Board {

    private ArrayList<MapPiece> mapPieces;

    private static Board instance;

    private List<Pair<TileType, Integer>> chitCards = new ArrayList<>();

    private Board() {
        this.mapPieces = new ArrayList<MapPiece>();
    }
    public static synchronized Board getInstance(){
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public ArrayList<MapPiece> getMapPieces() {
        return this.mapPieces;
    }

    public void addMapPiece(MapPiece mapPiece) {
        getMapPieces().add(mapPiece);
    }

    public void removeMapPiece(int index) {
        getMapPieces().remove(index);
    }

    public void initialiseBoard(String settings) {
        TileType[] animals = {TileType.BABY_DRAGON, TileType.SPIDER, TileType.SALAMANDER, TileType.BAT};
        int noOfSectionsBetweenCaves = 2;
        int noOfPlayers = 4;
        for (int i = 0; i < noOfPlayers; i++) {
            addMapPiece(new MapPiece(true, animals[i], i + 1));
            for (int j = 0; j < noOfSectionsBetweenCaves; j++) {
                addMapPiece(new MapPiece(false, TileType.BABY_DRAGON));
                addMapPiece(new MapPiece(false, TileType.SPIDER));
                addMapPiece(new MapPiece(false, TileType.SALAMANDER));
                addMapPiece(new MapPiece(false, TileType.BAT));
            }
        }
        generateChitCards();
    }

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
        this.chitCards.add(new Pair<>(TileType.PIRATE, 1));
        this.chitCards.add(new Pair<>(TileType.PIRATE, 2));
        this.chitCards.add(new Pair<>(TileType.PIRATE, 1));
        this.chitCards.add(new Pair<>(TileType.PIRATE, 2));
        Collections.shuffle(this.chitCards);
    }

    public List<Pair<TileType, Integer>> getChitCards() {
        return chitCards;
    }
}