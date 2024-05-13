package org.openjfx.fierydragons.turnlogic;


public class NextTileEmptyHandler extends TurnHandler {

    public NextTileEmptyHandler() {
    }

    @Override
    public boolean handleTurn(String request) {
        if (true) {
            return false;
        } else {
            return this.nextStep.handleTurn(request);
        }

    }
}
