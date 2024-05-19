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


        Tile nextTile = Board.getInstance().getMapPieces().get(nextTileLocation[0]).getTiles().get(nextTileLocation[1]);

        if (nextTile.getCurrentPlayer() == 0) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(false);
            result.add(false);
            return result;
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
