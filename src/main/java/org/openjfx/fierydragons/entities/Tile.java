package org.openjfx.fierydragons.entities;

public class Tile {

    private boolean isCave;
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

    public boolean isCave() {
        return isCave;
    }

    public void setCave(boolean cave) {
        isCave = cave;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public int getCurrentPlayer() {
        return playerId;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.playerId = currentPlayer;
    }
}
