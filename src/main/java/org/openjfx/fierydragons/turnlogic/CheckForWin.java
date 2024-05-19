package org.openjfx.fierydragons.turnlogic;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;

public class CheckForWin extends TurnHandler{

    public CheckForWin() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {
        if (chitCard.getKey() == TileType.PIRATE) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(false);
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
