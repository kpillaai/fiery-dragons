package org.openjfx.fierydragons.turnlogic;

public abstract class TurnHandler {
    protected TurnHandler nextStep;

    public TurnHandler() {

    }

    public void setNextStep(TurnHandler nextStep) {
        this.nextStep = nextStep;
    }

    public abstract boolean handleTurn(String request);
}
