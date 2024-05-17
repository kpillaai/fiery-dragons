package org.openjfx.fierydragons.turnlogic;


import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

public class NextTileContainsPlayer extends TurnHandler {

    public NextTileContainsPlayer() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {

        if (false) {

            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            return result;
        } else {
            return this.nextStep.handleTurn(chitCard);
        }

    }
}
