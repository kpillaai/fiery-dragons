package org.openjfx.fierydragons.turnlogic;


public class MovePastCave extends TurnHandler {

    public MovePastCave() {
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
