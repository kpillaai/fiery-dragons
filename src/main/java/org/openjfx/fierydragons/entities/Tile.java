package org.openjfx.fierydragons.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tile {

    private TileType tileType;

    @JsonCreator
    public Tile(@JsonProperty("tileType") TileType tileType) {
        this.tileType = tileType;
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
