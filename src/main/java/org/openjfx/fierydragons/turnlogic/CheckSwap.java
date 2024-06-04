package org.openjfx.fierydragons.turnlogic;

import org.openjfx.fierydragons.gameSaving.CustomPair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.gameSaving.CustomPair;

import java.util.ArrayList;

public class CheckSwap extends TurnHandler{

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
