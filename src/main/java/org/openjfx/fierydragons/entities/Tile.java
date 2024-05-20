package org.openjfx.fierydragons.entities;

public class Tile {

    private final boolean isCave;
    private TileType tileType;
    private int playerId = 0;

    public Tile(Boolean isCave, TileType tileType) {
        this.tileType = tileType;
        this.isCave = isCave;
    }

    public Tile(Boolean isCave, TileType tileType, int playerId) {
        this.tileType = tileType;
        this.isCave = isCave;
        this.playerId = playerId;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    returns the Tiletype of the Tile
     */
    public TileType getTileType() {
        return tileType;
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Takes an input of TileType and sets the TileType of this Tile
     */
    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
}
