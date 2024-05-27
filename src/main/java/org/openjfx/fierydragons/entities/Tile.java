package org.openjfx.fierydragons.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tile {

    private final Boolean isCave;
    private TileType tileType;
    private int playerId = 0;

    @JsonCreator
    public Tile(@JsonProperty("isCave") Boolean isCave, @JsonProperty("tileType") TileType tileType) {
        this.tileType = tileType;
        this.isCave = isCave;
    }

    public boolean isCave() {
        return isCave;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
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
