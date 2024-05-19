package org.openjfx.fierydragons.turnlogic;


import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;

public class MovePastCave extends TurnHandler {

    public MovePastCave() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {
        if (Game.getInstance().getCurrentPlayer().getDistanceToCave() > chitCard.getValue()) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            return result;
        } else {
            return this.nextStep.handleTurn(chitCard);
        }

    }
}
