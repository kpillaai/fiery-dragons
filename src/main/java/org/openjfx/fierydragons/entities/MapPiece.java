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

    public void addTile(Tile newTile) {
        this.tiles.add(newTile);
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void addCave(Tile cave) {
        this.cave = cave;
    }

    public Tile getCave() {
        return cave;
    }
}
