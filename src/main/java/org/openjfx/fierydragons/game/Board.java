package org.openjfx.fierydragons.game;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.*;

import java.lang.reflect.Array;
import java.util.*;

public class Board {

    private ArrayList<MapPiece> mapPieces;

    private static Board instance;

    private Deck deck;

    private ArrayList<int[]> playerLocationArray;

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
        // create normal pieces
        ArrayList<MapPiece> normalPiece = new ArrayList<>();
        normalPiece.add(new MapPiece(new Tile(false, TileType.SPIDER), new Tile(false, TileType.BAT), new Tile(false, TileType.SALAMANDER)));
        normalPiece.add(new MapPiece(new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.BAT)));
        normalPiece.add(new MapPiece(new Tile(false, TileType.BAT), new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.SALAMANDER)));
        normalPiece.add(new MapPiece(new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.SPIDER)));

        // create cave pieces
        ArrayList<MapPiece> cavePiece = new ArrayList<>();
        cavePiece.add(new MapPiece(new Tile(false, TileType.BABY_DRAGON), new Tile(false, TileType.BAT), new Tile(false, TileType.SPIDER)));
        cavePiece.add(new MapPiece(new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.SPIDER), new Tile(false, TileType.BAT)));
        cavePiece.add(new MapPiece(new Tile(false, TileType.SPIDER), new Tile(false, TileType.SALAMANDER), new Tile(false, TileType.BABY_DRAGON)));
        cavePiece.add(new MapPiece(new Tile(false, TileType.BAT), new Tile(false, TileType.SPIDER), new Tile(false, TileType.BABY_DRAGON)));

        // shuffle cave and normal pieces for random board
        Collections.shuffle(normalPiece);
        Collections.shuffle(cavePiece);

        // In each loop, set the cave, add a cave piece and a normal piece in alternating order
        for (int i = 0; i < cavePiece.size(); i++) {
            // setting the caves
            switch (i) {
                case 0:
                    cavePiece.get(i).addCave(new Tile(true, TileType.SPIDER));
                    break;
                case 1:
                    cavePiece.get(i).addCave(new Tile(true, TileType.SALAMANDER));
                    break;
                case 2:
                    cavePiece.get(i).addCave(new Tile(true, TileType.BAT));
                    break;
                case 3:
                    cavePiece.get(i).addCave(new Tile(true, TileType.BABY_DRAGON));
                    break;
            }

            // adding pieces to mapPiece array list
            addMapPiece(cavePiece.get(i));
            addMapPiece(normalPiece.get(i));
            }
        this.deck = new Deck();
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void createPlayerLocationArray() {
        int playerCount = Game.getInstance().getPlayerCount();
        playerLocationArray = new ArrayList<>();
        System.out.println(playerCount);
        switch (playerCount) {
            case 2:
                playerLocationArray.add(new int[]{6, -1});
                playerLocationArray.add(new int[]{0, -1});
            case 3:
                playerLocationArray.add(new int[]{6, -1});
                playerLocationArray.add(new int[]{0, -1});
                playerLocationArray.add(new int[]{2, -1});
            case 4:
                playerLocationArray.add(new int[]{6, -1});
                playerLocationArray.add(new int[]{0, -1});
                playerLocationArray.add(new int[]{2, -1});
                playerLocationArray.add(new int[]{4, -1});
        }
    }

    public void movePlayer(Player player, int noOfMoves) {
        int playerId = player.getId();
        int currVolcanoIndex = playerLocationArray.get(playerId - 1)[0];
        int currTileIndex = playerLocationArray.get(playerId - 1)[1];
        int newVolcanoIndex = currVolcanoIndex;
        int newTileIndex;
        // if on cave
        if (currTileIndex < 0) {
            newTileIndex = noOfMoves;
            if (newTileIndex > 2) {
                newTileIndex = newTileIndex - 3;
                newVolcanoIndex = currVolcanoIndex + 1;
                if (newVolcanoIndex > 7) {
                    newVolcanoIndex = 0;
                }
            }
            playerLocationArray.get(playerId - 1)[0] = newVolcanoIndex;
            playerLocationArray.get(playerId - 1)[1] = newTileIndex;

        } else if (noOfMoves < 0) { // pirate card
            newTileIndex = currTileIndex + noOfMoves;
            if (newTileIndex < 0) {
                newTileIndex = newTileIndex + 3;
                newVolcanoIndex = currVolcanoIndex - 1;
                if (newVolcanoIndex < 0) {
                    newVolcanoIndex = 7;
                }
            }
            playerLocationArray.get(playerId - 1)[0] = newVolcanoIndex;
            playerLocationArray.get(playerId - 1)[1] = newTileIndex;
        }
        else { // if on board
            newTileIndex = currTileIndex + noOfMoves;
            if (newTileIndex > 2) {
                newTileIndex = newTileIndex - 3;
                newVolcanoIndex = currVolcanoIndex + 1;
                if (newVolcanoIndex > 7) {
                    newVolcanoIndex = 0;
                }
            }
            playerLocationArray.get(playerId - 1)[0] = newVolcanoIndex;
            playerLocationArray.get(playerId - 1)[1] = newTileIndex;
        }
    }

    public int[] getPlayerLocation(Player player, int noOfTilesAhead) {
        int playerId = player.getId();

        int volcanoIndex = playerLocationArray.get(playerId - 1)[0];
        int tileIndex = playerLocationArray.get(playerId - 1)[1];

        return new int[]{volcanoIndex, tileIndex};
    }

    public ArrayList<int[]> getPlayerLocationArray() {
        return playerLocationArray;
    }

    public int[] getNewLocation(int volcanoIndex, int tileIndex, int noOfMoves) {
        int newVolcanoIndex = volcanoIndex;
        int newTileIndex;
        if (tileIndex < 0) {
            newTileIndex = noOfMoves;
            if (newTileIndex > 2) {
                newTileIndex = newTileIndex - 3;
                newVolcanoIndex = volcanoIndex + 1;
                if (newVolcanoIndex > 7) {
                    newVolcanoIndex = 0;
                }
            }
        } else {
            newTileIndex = tileIndex + noOfMoves;
            if (newTileIndex > 2) {
                newTileIndex = newTileIndex - 3;
                newVolcanoIndex = volcanoIndex + 1;
                if (newVolcanoIndex > 7) {
                    newVolcanoIndex = 0;
                }
            }
        }
        return new int[]{newVolcanoIndex, newTileIndex};
    }
}