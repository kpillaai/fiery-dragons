package org.openjfx.fierydragons.turnlogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.CustomPair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;

public class CheckForWin extends TurnHandler{

    @JsonCreator
    public CheckForWin() {
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    This method extends TurnHandler, intends to check whether the player will win this turn.
     * Returns 2 booleans representing if the player can move, and if the player will win this turn. Takes a Pair which
     * represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        if (chitCard.getKey() == TileType.PIRATE) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            return result;
        }

        if (Game.getInstance().getCurrentPlayer().getDistanceToCave() != chitCard.getValue()) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            return result;
        } else {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(true);
            return result;
        }
    }
}
