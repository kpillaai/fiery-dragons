package org.openjfx.fierydragons.turnlogic;


import javafx.util.Pair;
import org.openjfx.fierydragons.entities.Tile;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

public class NextTileContainsPlayer extends TurnHandler {

    public NextTileContainsPlayer() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {
        int[] nextTileLocation = {0, 0};
        int[] currentLocation = Board.getInstance().getPlayerLocation(Game.getInstance().getCurrentPlayer(), 0);
        if (currentLocation[1] < 0) { // Checks if dragon is in cave
            if (chitCard.getValue() > 0) {
                nextTileLocation[1] = chitCard.getValue();
                nextTileLocation[0] = currentLocation[0];
            } else { // Rolls a pirate inside a cave
                ArrayList<Boolean> result = new ArrayList<>();
                result.add(false);
                result.add(false);
                return result;
            }
        }
        else {
            nextTileLocation[1] += chitCard.getValue();
            nextTileLocation[0] = currentLocation[0];
        }

        if (nextTileLocation[1] > 2) { // Moving to next volcano piece
            nextTileLocation[0] += 1;
            nextTileLocation[1] = nextTileLocation[1] - 3;
            if (nextTileLocation[0] > 7) { // Index errors
                nextTileLocation[0] = 0;
            }
        } else if (nextTileLocation[1] < 0) { // Means pirate dragon has been flipped
            nextTileLocation[0] -= 1;
            nextTileLocation[1] = nextTileLocation[1] + 3;
            if (nextTileLocation[0] < 0) { // Index errors
                nextTileLocation[0] = 7;
            }
        }
        for (int i = 0; i < Board.getInstance().getPlayerLocationArray().size() - 1; i++) {
            System.out.println(Board.getInstance().getPlayerLocationArray().get(i));
            if (Board.getInstance().getPlayerLocationArray().get(i)[0] == nextTileLocation[0] && Board.getInstance().getPlayerLocationArray().get(i)[1] == nextTileLocation[1]) {

                ArrayList<Boolean> result = new ArrayList<>();
                result.add(false);
                result.add(false);
                return result;
            }
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
