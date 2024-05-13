package org.openjfx.fierydragons.game;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.Deck;
import org.openjfx.fierydragons.entities.MapPiece;
import org.openjfx.fierydragons.entities.TileType;

import java.util.*;

public class Board {

    private ArrayList<MapPiece> mapPieces;

    private static Board instance;

    private Deck deck;

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
                if (i != 0) {
                    addMapPiece(new MapPiece(false, TileType.BABY_DRAGON));
                }
                if (i != 1) {
                    addMapPiece(new MapPiece(false, TileType.SPIDER));
                }
                if (i != 2) {
                    addMapPiece(new MapPiece(false, TileType.SALAMANDER));
                }
                if (i != 3) {
                    addMapPiece(new MapPiece(false, TileType.BAT));
                }
            }
        }
        this.deck = new Deck();
    }

    public Deck getDeck() {
        return this.deck;
    }
}