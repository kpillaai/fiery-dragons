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
        int[] nextTileLocation = Board.getInstance().getPlayerLocation(Game.getInstance().getCurrentPlayer(), 0);
        nextTileLocation[1] += chitCard.getValue();
        if (nextTileLocation[1] > 3) {
            nextTileLocation[0] += 1;
            nextTileLocation[1] = nextTileLocation[1] - 3;
        }
        for (int i = 0; i < Board.getInstance().getPlayerLocationArray().size() - 1; i++) {
            if (Board.getInstance().getPlayerLocationArray().get(i).getFirst() == nextTileLocation[0] && Board.getInstance().getPlayerLocationArray().get(i).get(1) == nextTileLocation[1]) {
                ArrayList<Boolean> result = new ArrayList<>();
                result.add(false);
                result.add(false);
                return result;
            }
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
