package org.openjfx.fierydragons.turnlogic;

import org.openjfx.fierydragons.gameSaving.CustomPair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.gameSaving.CustomPair;

import java.util.ArrayList;

public class CheckSwap extends TurnHandler{

    /**
     * @author  Jeffrey Yan
     * @desc    This method extends TurnHandler, intends to check whether the player flipped a swap chit card.
     * Returns 3 booleans representing if the player can move, if the player will win this turn and if a player can swap.
     * Takes a Pair which represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        if (chitCard.getKey() == TileType.SWAP) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            result.add(true);
            return result;
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
