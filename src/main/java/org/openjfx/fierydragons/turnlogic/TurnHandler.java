package org.openjfx.fierydragons.turnlogic;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.gameSaving.CustomPair;
import org.openjfx.fierydragons.entities.TileType;

import java.util.ArrayList;

public abstract class TurnHandler {

    protected TurnHandler nextStep;

    @JsonCreator
    public TurnHandler() {

    }

    /**
     * @author  Zilei Chen
     * @desc    This abstract method sets up the next step of the turn logic
     */
    public void setNextStep(TurnHandler nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * @author  Zilei Chen
     * @desc    This abstract method intends to calculate the turn logic for the specific child class.
     * Returns 3 booleans representing if the player can move, if the player will win this turn and if a player can swap.
     * Takes a Pair which represents chit cards and their number value.
     */
    public abstract ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard);
}
