package org.openjfx.fierydragons.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class MapPiece {
    private ArrayList<Tile> tiles;
    private Tile cave;

    private int caveIndex;

    @JsonCreator
    public MapPiece(@JsonProperty("tile1") Tile tile1, @JsonProperty("tile2") Tile tile2, @JsonProperty("tile3") Tile tile3) {
        this.tiles = new ArrayList<Tile>();
        addTile(tile1);
        addTile(tile2);
        addTile(tile3);
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public void setCave(Tile cave) {
        this.cave = cave;
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
