package org.openjfx.fierydragons.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Board {

    private ArrayList<MapPiece> mapPieces;

    private static Board instance;

    private Deck deck;

    private ArrayList<int[]> playerLocationArray;

    private int numTiles;

    @JsonCreator
    private Board() {
    }

    /**
     * @author  Zilei Chen
     * @desc    This is a static constructor for Board class to allow access globally and only let one instance of this
     * class to exist at any given time.
     * Returns Board instance.
     */
    public static synchronized Board getInstance(){
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public static void resetBoard() {
        instance = null;
    }

    public void setMapPieces(ArrayList<MapPiece> mapPieces) {
        this.mapPieces = mapPieces;
    }

    public static void setInstance(Board instance) {
        Board.instance = instance;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPlayerLocationArray(ArrayList<int[]> playerLocationArray) {
        this.playerLocationArray = playerLocationArray;
    }

    /**
     * @author  Zilei Chen
     * @desc    Getter for the MapPieces within the Board
     * Returns MapPieces list.
     */
    public ArrayList<MapPiece> getMapPieces() {
        return this.mapPieces;
    }

    /**
     * @author  Zilei Chen
     * @desc    Adds MapPieces to the arraylist in Board
     * Param is MapPiece class
     */
    public void addMapPiece(MapPiece mapPiece) {
        getMapPieces().add(mapPiece);
    }

    /**
     * @author  Zilei Chen
     * @desc    Removes MapPieces to the arraylist in Board
     * Param is the index of the MapPiece to be removed from the arraylist
     */
    public void removeMapPiece(int index) {
        getMapPieces().remove(index);
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Initialises the Board by generating MapPieces and the Chit cards
     * Param is a string of settings used for custom settings for extensions.
     */
    public void initialiseBoard(String settings) {
        this.mapPieces = new ArrayList<MapPiece>();
        // create normal pieces
        ArrayList<MapPiece> normalPiece = new ArrayList<>();
        normalPiece.add(new MapPiece(new Tile(TileType.SPIDER), new Tile(TileType.BAT), new Tile(TileType.SALAMANDER)));
        normalPiece.add(new MapPiece(new Tile(TileType.BABY_DRAGON), new Tile(TileType.SALAMANDER), new Tile(TileType.BAT)));
        normalPiece.add(new MapPiece(new Tile(TileType.BAT), new Tile(TileType.BABY_DRAGON), new Tile(TileType.SALAMANDER)));
        normalPiece.add(new MapPiece(new Tile(TileType.SALAMANDER), new Tile(TileType.BABY_DRAGON), new Tile(TileType.SPIDER)));

        // create cave pieces
        ArrayList<MapPiece> cavePiece = new ArrayList<>();
        MapPiece mapPiece1 = new MapPiece(new Tile(TileType.BABY_DRAGON), new Tile(TileType.BAT), new Tile(TileType.SPIDER));
        mapPiece1.setCaveIndex(1);
        cavePiece.add(mapPiece1);
        MapPiece mapPiece2 = new MapPiece(new Tile(TileType.SALAMANDER), new Tile(TileType.SPIDER), new Tile(TileType.BAT));
        mapPiece2.setCaveIndex(1);
        cavePiece.add(mapPiece2);
        MapPiece mapPiece3 = new MapPiece(new Tile(TileType.SPIDER), new Tile(TileType.SALAMANDER), new Tile(TileType.BABY_DRAGON));
        mapPiece3.setCaveIndex(1);
        cavePiece.add(mapPiece3);
        MapPiece mapPiece4 = new MapPiece(new Tile(TileType.BAT), new Tile(TileType.SPIDER), new Tile(TileType.BABY_DRAGON));
        mapPiece4.setCaveIndex(1);
        cavePiece.add(mapPiece4);

        // shuffle cave and normal pieces for random board
        Collections.shuffle(normalPiece);
        Collections.shuffle(cavePiece);

        // In each loop, set the cave, add a cave piece and a normal piece in alternating order
        for (int i = 0; i < cavePiece.size(); i++) {
            // setting the caves
            switch (i) {
                case 0:
                    cavePiece.get(i).addCave(new Tile(TileType.SPIDER));
                    break;
                case 1:
                    cavePiece.get(i).addCave(new Tile(TileType.SALAMANDER));
                    break;
                case 2:
                    cavePiece.get(i).addCave(new Tile(TileType.BAT));
                    break;
                case 3:
                    cavePiece.get(i).addCave(new Tile(TileType.BABY_DRAGON));
                    break;
            }

            // adding pieces to mapPiece array list
            addMapPiece(cavePiece.get(i));
            addMapPiece(normalPiece.get(i));
            }
        this.deck = new Deck();
    }

    public int getNumTiles() {
        return numTiles;
    }

    public void setNumTiles() {
        numTiles = 0;
        for (int i = 0; i < mapPieces.size(); i++) {
            for (int j = 0; j < mapPieces.get(j).getTiles().size(); j++) {
                numTiles++;
            }
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Getter for Deck attribute
     * Returns Deck, a list of chit cards.
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Initialises a list of players for the game.
     */
    public void createPlayerLocationArray() {
        if (playerLocationArray == null) {
            int playerCount = Game.getInstance().getPlayerCount();
            playerLocationArray = new ArrayList<>();
            switch (playerCount) {
                case 2:
                    playerLocationArray.add(new int[]{6, -1});
                    playerLocationArray.add(new int[]{2, -1});
                    break;
                case 3:
                    playerLocationArray.add(new int[]{6, -1});
                    playerLocationArray.add(new int[]{0, -1});
                    playerLocationArray.add(new int[]{2, -1});
                    break;
                case 4:
                    playerLocationArray.add(new int[]{6, -1});
                    playerLocationArray.add(new int[]{0, -1});
                    playerLocationArray.add(new int[]{2, -1});
                    playerLocationArray.add(new int[]{4, -1});
                    break;
            }
        }
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Moves the player from one Tile to another and from one MapPiece to another
     * Param is the Player to be moved, and the number of moves to be moved.
     */
    public void movePlayer(Player player, int noOfMoves) {
        int playerId = player.getId();
        int currVolcanoIndex = playerLocationArray.get(playerId - 1)[0];
        int currTileIndex = playerLocationArray.get(playerId - 1)[1];
        int newVolcanoIndex = currVolcanoIndex;
        int newTileIndex;
        // if on cave
        if (currTileIndex < 0) {
            newTileIndex = mapPieces.get(playerLocationArray.get(playerId - 1)[0]).getCaveIndex() + noOfMoves - 1;
            while (newTileIndex > mapPieces.get(currVolcanoIndex).getTiles().size() - 1) {
                newTileIndex = newTileIndex - mapPieces.get(currVolcanoIndex).getTiles().size();
                newVolcanoIndex = currVolcanoIndex + 1;
                if (newVolcanoIndex > mapPieces.size()) {
                    newVolcanoIndex = 0;
                }
            }
            playerLocationArray.get(playerId - 1)[0] = newVolcanoIndex;
            playerLocationArray.get(playerId - 1)[1] = newTileIndex;

        } else if (noOfMoves < 0) { // pirate card
            newTileIndex = currTileIndex + noOfMoves;
            while (newTileIndex < 0) {
                newVolcanoIndex = currVolcanoIndex - 1;
                if (newVolcanoIndex < 0) {
                    newVolcanoIndex = mapPieces.size() - 1;
                }
                newTileIndex = newTileIndex + mapPieces.get(newVolcanoIndex).getTiles().size();

            }
            playerLocationArray.get(playerId - 1)[0] = newVolcanoIndex;
            playerLocationArray.get(playerId - 1)[1] = newTileIndex;
        }
        else { // if on board
            newTileIndex = currTileIndex + noOfMoves;
            while (newTileIndex > mapPieces.get(currVolcanoIndex).getTiles().size() - 1) {
                newTileIndex = newTileIndex - mapPieces.get(currVolcanoIndex).getTiles().size();
                newVolcanoIndex++;
                if (newVolcanoIndex > mapPieces.size() - 1) {
                    newVolcanoIndex = 0;
                }
            }
            playerLocationArray.get(playerId - 1)[0] = newVolcanoIndex;
            playerLocationArray.get(playerId - 1)[1] = newTileIndex;
        }
        Game.getInstance().getCurrentPlayer().subtractDistance(noOfMoves);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Gets the player location for a specific player.
     * Param is the Player that we want to find the location of.
     */
    public int[] getPlayerLocation(Player player) {
        int playerId = player.getId();
        int volcanoIndex = playerLocationArray.get(playerId - 1)[0];
        int tileIndex = playerLocationArray.get(playerId - 1)[1];
        return new int[]{volcanoIndex, tileIndex};
    }

    private void setPlayerLocation(Player player, int[] newLocation) {
        playerLocationArray.set(player.getId() - 1, newLocation);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Gets tile position on board given volcano and tile indexes, assumes player is not in cave
     * param:   position array where [volcanoIndex, tileIndex] represents a player's position
     * returns: an int between 0 and numTiles that represents the tile the player is currently on.
     */
    public int getTileLocation(int[] position) {
        int counter = 0;
        for (int i = 0; i < mapPieces.size(); i++) {
            for (int j = 0; j < mapPieces.get(i).getTiles().size(); j++) {
                if (i == position[0] && j == position[1]) {
                    return counter;
                }
                counter++;
            }
        }
        return counter;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Gets tile position on board of where the cave should be
     * param:   position array where [volcanoIndex, tileIndex] represents a player's position
     * returns: an int between 0 and numTiles that represents where the cave is.
     */
    public int getCaveTileLocation(int[] position) {
        int counter = 0;
        for (int i = 0; i < mapPieces.size(); i++) {
            for (int j = 0; j < mapPieces.get(i).getTiles().size(); j++) {
                if (i == position[0] && j == mapPieces.get(i).getCaveIndex()) {
                    return counter;
                }
                counter++;
            }
        }
        return counter;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Getter for the PlayerLocationArray
     * Returns an array listing the locations of each Player.
     */
    public ArrayList<int[]> getPlayerLocationArray() {
        return playerLocationArray;
    }


    /**
     * @author  Jeffrey Yan
     * @desc    Function used to find the closest player relative to target player
     * param:   targetPlayer - the player that you want to find the closest player to
     * returns: closest player to targetPlayer
     */
    public Player findClosestPlayer(Player targetPlayer) {
        int targetPlayerId = targetPlayer.getId();
        int targetPlayerTile = getTileLocation(getPlayerLocation(targetPlayer));
        int closestPlayerIndex = 0;
        int distanceFromPlayer = 50;

        for (int i = 0; i < playerLocationArray.size(); i++) {
            if (i != targetPlayerId - 1) {
                int checkPlayerTile = getTileLocation(playerLocationArray.get(i));
                // if not on cave
                if (playerLocationArray.get(i)[1] >= 0) {
                    int directDistance = Math.abs(targetPlayerTile - checkPlayerTile);
                    int wrapAroundDistance = numTiles - directDistance;
                    int circularDistance = Math.min(directDistance, wrapAroundDistance);
                    if (circularDistance < distanceFromPlayer) {
                        closestPlayerIndex = i;
                        distanceFromPlayer = circularDistance;
                    }
                }
            }
        }
        return Game.getInstance().getPlayerList().get(closestPlayerIndex);
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Function used to swap two player locations, and update their distances from starting cave tile
     *          accordingly so that they can win as expected
     * param:   player1 player to swap
     * param:   player2 player to swap
     */
    public void swapPlayers(Player player1, Player player2) {
        int[] player1Location = {0, 0};
        int[] player2Location = {0, 0};
        player1Location[0] = getPlayerLocation(player1)[0] + 0;
        player1Location[1] = getPlayerLocation(player1)[1] + 0;
        player2Location[0] = getPlayerLocation(player2)[0] + 0;
        player2Location[1] = getPlayerLocation(player2)[1] + 0;

        int player1NewDistance = calculateDistanceToCave(player1, player2Location);
        int player2NewDistance = calculateDistanceToCave(player2, player1Location);

        player1.setDistanceToCave(player1NewDistance);
        player2.setDistanceToCave(player2NewDistance);

        setPlayerLocation(player1, player2Location);
        setPlayerLocation(player2, player1Location);
    }

    /*
     * @author  Jeffrey Yan
     * @desc    Function used to calculate a player's distance from their starting cave given some location on the board
     * param:   player player to check distance of
     * param:   targetLocation location to check
     */
    private int calculateDistanceToCave(Player player, int[] targetLocation) {
        int currentTile = getTileLocation(getPlayerLocation(player));
        int targetTile = getTileLocation(targetLocation);
        int distanceToCave = player.getDistanceToCave();

        int forwardDistance = (targetTile - currentTile + numTiles) % numTiles;
        int backwardDistance = (currentTile - targetTile + numTiles) % numTiles;
        if (forwardDistance <= backwardDistance) {
            if (distanceToCave - forwardDistance <= 0) {
                return distanceToCave - forwardDistance + numTiles;
            }
            return distanceToCave - forwardDistance;
        } else {
            if (distanceToCave + backwardDistance > numTiles) {
                return distanceToCave + backwardDistance;
            }
            return distanceToCave + backwardDistance;
        }
    }
}