package org.openjfx.fierydragons.turnlogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.gameSaving.CustomPair;
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
     * Returns 3 booleans representing if the player can move, if the player will win this turn and if a player can swap.
     * Takes a Pair which represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        if (chitCard.getKey() == TileType.PIRATE) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            result.add(false);
            return result;
        }
        if (Game.getInstance().getCurrentPlayer().getDistanceToCave() != chitCard.getValue()) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            result.add(false);
            return result;
        } else {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(true);
            result.add(false);
            return result;
        }
    }
}
