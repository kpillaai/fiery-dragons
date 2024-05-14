package org.openjfx.fierydragons.turnlogic;


import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;

import java.util.ArrayList;

public abstract class TurnHandler {
    protected TurnHandler nextStep;

    public TurnHandler() {

    }

    public void setNextStep(TurnHandler nextStep) {
        this.nextStep = nextStep;
    }

    public abstract ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard);
}
