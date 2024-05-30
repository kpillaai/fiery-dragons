package org.openjfx.fierydragons.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;
import org.openjfx.fierydragons.game.Turn;

import java.io.IOException;

public class Player {
    private String name;

    private int id;

    private int distanceToCave = 2;

    private int timeRemainingSeconds;

    @JsonCreator
    public Player(@JsonProperty("name") String name, @JsonProperty("id") int id) {
        this.name = name;
        this.id = id;
        this.timeRemainingSeconds = 150;
        // incrementing the players distance to their cave based on the number of tiles in the volcano
        for (int i = 0; i < Board.getInstance().getMapPieces().size(); i++) {
            for (int j = 0; j < Board.getInstance().getMapPieces().get(i).getTiles().size(); j++) {
                this.distanceToCave += 1;
            }
        }
    }

    public void setDistanceToCave(int distanceToCave) {
        this.distanceToCave = distanceToCave;
    }

    /**
     * @author  Zilei Chen
     * @desc    Takes an input of Integer which is a chit card ID and calls the next turn method, parsing the chit card
     */
    public void flipCard(Integer chitCardId) throws IOException {
        Turn.getInstance().nextTurn(chitCardId);
    }

    /**
     * @author  Zilei Chen
     * @desc    Returns the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * @author  Zilei Chen
     * @desc    Sets the name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @author  Zilei Chen
     * @desc    Returns the players id
     */
    public int getId() {
        return id;
    }

    /**
     * @author  Zilei Chen
     * @desc    Sets the id of the player
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @author  Zilei Chen
     * @desc    gets the distance the player is from their cave
     */
    public int getDistanceToCave() {
        return distanceToCave;
    }

    /**
     * @author  Zilei Chen
     * @desc    Subtracts the distance the player has moved from their distance to their cave
     */
    public void subtractDistance(int moveValue) {
        this.distanceToCave = this.distanceToCave - moveValue;
    }

    public int getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void setTimeRemainingSeconds(int timeRemaining) {
        this.timeRemainingSeconds = timeRemaining;
    }
}

