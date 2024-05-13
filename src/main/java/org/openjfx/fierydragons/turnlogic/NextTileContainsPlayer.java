package org.openjfx.fierydragons.turnlogic;


import javafx.util.Pair;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

public class NextTileContainsPlayer extends TurnHandler {

    public NextTileContainsPlayer() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(String request) {
        if (true) {

            ArrayList<Boolean> result = new ArrayList<>();
            result.add(true);
            result.add(false);
            return result;
        } else {
            return this.nextStep.handleTurn(request);
        }

    }
}
