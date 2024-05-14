package org.openjfx.fierydragons.turnlogic;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;

import java.util.ArrayList;

public class CheckForWin extends TurnHandler{

    public CheckForWin() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {
        if (true) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            return result;
        } else {
            return this.nextStep.handleTurn(chitCard);
        }

    }
}
