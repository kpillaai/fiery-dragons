package org.openjfx.fierydragons.turnlogic;

public class CheckTile extends TurnHandler {
    public CheckTile() {
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
