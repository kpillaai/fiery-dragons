package org.openjfx.fierydragons.turnlogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import javafx.util.Pair;
import org.openjfx.fierydragons.CustomPair;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.entities.Tile;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;
import java.util.NavigableMap;

public class CheckTimer extends TurnHandler {

    @JsonCreator
    public CheckTimer() {
    }

    /**
     * @author  Zilei Chen
     * @desc    This method extends TurnHandler, intends to check whether the player has run out of time.
     * Returns 2 booleans representing if the player can move, and if the player will win this turn. Takes a Pair which
     * represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        // Check if current player runs out of time
        if (Game.getInstance().getCurrentPlayer().getTimeRemainingSeconds() <= 0) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(false);
            result.add(false);
            return result;
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
