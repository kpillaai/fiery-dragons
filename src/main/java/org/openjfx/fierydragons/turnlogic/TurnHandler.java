package org.openjfx.fierydragons.turnlogic;


import java.util.ArrayList;
import java.util.List;

public abstract class TurnHandler {
    protected TurnHandler nextStep;

    public TurnHandler() {

    }

    public void setNextStep(TurnHandler nextStep) {
        this.nextStep = nextStep;
    }

    public abstract ArrayList<Boolean> handleTurn(String request);
}
