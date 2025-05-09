package org.openjfx.fierydragons.turnlogic;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.gameSaving.CustomPair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;

public class MovePastCave extends TurnHandler {

    @JsonCreator
    public MovePastCave() {
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    This method extends TurnHandler, intends to calculate if the player will move past the cave when winning
     * the game.
     * Returns 3 booleans representing if the player can move, if the player will win this turn and if a player can swap.
     * Takes a Pair which represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        if (chitCard.getKey() == TileType.PIRATE) {
            return this.nextStep.handleTurn(chitCard);
        }

        if (Game.getInstance().getCurrentPlayer().getDistanceToCave() < chitCard.getValue()) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(false);
            result.add(false);
            result.add(false);
            return result;
        } else {
            return this.nextStep.handleTurn(chitCard);
        }

    }
}
