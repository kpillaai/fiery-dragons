package org.openjfx.fierydragons.game;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.*;

import java.lang.reflect.Array;
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

        ArrayList<MapPiece> normalPiece = new ArrayList<>();
        normalPiece.add(new MapPiece(new Tile(false, TileType.SPIDER), new Tile(false, TileType.BAT), new Tile(false, TileType.SALAMANDER)));
        normalPiece.add(new MapPiece(new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.BAT)));
        normalPiece.add(new MapPiece(new Tile(false, TileType.BAT), new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.SALAMANDER)));
        normalPiece.add(new MapPiece(new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.SPIDER)));

        ArrayList<MapPiece> cavePiece = new ArrayList<>();
        cavePiece.add(new MapPiece(new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.BAT), new Tile(false, TileType.SPIDER)));
        cavePiece.add(new MapPiece(new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.SPIDER), new Tile(false, TileType.BAT)));
        cavePiece.add(new MapPiece(new Tile(false, TileType.SPIDER), new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.BABY_DRAGON)));
        cavePiece.add(new MapPiece(new Tile(false, TileType.BAT), new Tile(false, TileType.SPIDER), new Tile(false, TileType.BABY_DRAGON)));

        Collections.shuffle(normalPiece);
        Collections.shuffle(cavePiece);

        for (int i = 0; i < cavePiece.size(); i++) {
            addMapPiece(cavePiece.get(i));
            addMapPiece(normalPiece.get(i));
        }

        /*
        for (int i = 0; i < noOfPlayers; i++) {
            addMapPiece(new Tile(true, animals[i], i + 1));
            for (int j = 0; j < noOfSectionsBetweenCaves; j++) {
                if (i != 0) {
                    addMapPiece(new Tile(false, TileType.BABY_DRAGON));
                }
                if (i != 1) {
                    addMapPiece(new Tile(false, TileType.SPIDER));
                }
                if (i != 2) {
                    addMapPiece(new Tile(false, TileType.SALAMANDER));
                }
                if (i != 3) {
                    addMapPiece(new Tile(false, TileType.BAT));
                }
            }
        }
         */
        this.deck = new Deck();
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void movePlayer(Player player, int noOfMoves) {
        //Todo
    }

    public int[] getPlayerLocation(Player player, int noOfTilesAhead) {
        int first = 1; // replace with actual logic
        int second = 2; // replace with actual logic
        // first means index of mapPiece, second means index of tile within mapPiece
        return new int[]{first, second};
    }
}