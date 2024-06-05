package org.openjfx.fierydragons.turnlogic;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;
import org.openjfx.fierydragons.gameSaving.CustomPair;
import java.util.ArrayList;

public class NextTileContainsPlayer extends TurnHandler {

    @JsonCreator
    public NextTileContainsPlayer() {
    }

    /**
     * @author  Zilei Chen
     * @desc    This method extends TurnHandler, intends to calculate if the next tile the player will move to
     * will contain a player.
     * Returns 2 booleans representing if the player can move, and if the player will win this turn. Takes a Pair which
     * represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        int[] currentLocation = Board.getInstance().getPlayerLocation(Game.getInstance().getCurrentPlayer());
        int[] nextTileLocation = {0, 0};
        nextTileLocation[0] = currentLocation[0] + 0;
        nextTileLocation[1] = currentLocation[1] + 0;
        if (currentLocation[1] < 0) { // Checks if dragon is in cave
            if (chitCard.getValue() > 0) {
                nextTileLocation[1] = chitCard.getValue();
                nextTileLocation[0] = currentLocation[0];
            } else { // Rolls a pirate inside a cave
                ArrayList<Boolean> result = new ArrayList<>();
                result.add(false);
                result.add(false);
                result.add(false);
                return result;
            }
        }
        else {
            nextTileLocation[1] += chitCard.getValue();
            nextTileLocation[0] = currentLocation[0];
        }

        if (nextTileLocation[1] > Board.getInstance().getMapPieces().get(currentLocation[0]).getTiles().size() - 1) { // Moving to next volcano piece
            nextTileLocation[0] += 1;
            nextTileLocation[1] = nextTileLocation[1] - Board.getInstance().getMapPieces().get(currentLocation[0]).getTiles().size();
            if (nextTileLocation[0] > Board.getInstance().getMapPieces().size() - 1) { // Index errors
                nextTileLocation[0] = 0;
            }
        } else if (nextTileLocation[1] < 0) { // Means pirate dragon has been flipped
            nextTileLocation[0] -= 1;
            if (nextTileLocation[0] < 0) { // Index errors
                nextTileLocation[0] = Board.getInstance().getMapPieces().size() - 1;
            }
            nextTileLocation[1] = nextTileLocation[1] + Board.getInstance().getMapPieces().get(nextTileLocation[0]).getTiles().size();
        }

        for (int i = 0; i < Board.getInstance().getPlayerLocationArray().size(); i++) {
            if (Board.getInstance().getPlayerLocationArray().get(i)[0] == nextTileLocation[0] && Board.getInstance().getPlayerLocationArray().get(i)[1] == nextTileLocation[1]) {
                ArrayList<Boolean> result = new ArrayList<>();
                result.add(false);
                result.add(false);
                result.add(false);
                return result;
            }
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
