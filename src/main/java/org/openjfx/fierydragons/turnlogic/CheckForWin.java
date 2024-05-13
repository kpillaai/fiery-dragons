package org.openjfx.fierydragons.turnlogic;

import java.util.ArrayList;

public class CheckForWin extends TurnHandler{

    public CheckForWin() {
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
