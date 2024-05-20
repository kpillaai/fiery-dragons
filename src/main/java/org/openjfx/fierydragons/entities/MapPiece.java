package org.openjfx.fierydragons.entities;

import java.util.ArrayList;

public class MapPiece {
    private ArrayList<Tile> tiles;
    private Tile cave;

    public MapPiece(Tile tile1, Tile tile2, Tile tile3) {
        this.tiles = new ArrayList<Tile>();
        addTile(tile1);
        addTile(tile2);
        addTile(tile3);
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Takes an input of Tile and adds it to the MapPiece
     */
    public void addTile(Tile newTile) {
        this.tiles.add(newTile);
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Returns the tiles in the MapPiece
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Takes an input of Tile which is a cave and adds it to the MapPiece in the cave attribute
     */
    public void addCave(Tile cave) {
        this.cave = cave;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Returns the cave Tile
     */
    public Tile getCave() {
        return cave;
    }
}
