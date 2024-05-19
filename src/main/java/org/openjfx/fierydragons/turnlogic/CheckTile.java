package org.openjfx.fierydragons.turnlogic;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;

public class CheckTile extends TurnHandler {
    public CheckTile() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {
        int[] currentPlayerLocation = Board.getInstance().getPlayerLocation(Game.getInstance().getCurrentPlayer(), 0);
        TileType currentTileType;
        if (currentPlayerLocation[1] < 0) {
            currentTileType = Board.getInstance().getMapPieces().get(currentPlayerLocation[0]).getCave().getTileType();
        } else {
            currentTileType = Board.getInstance().getMapPieces().get(currentPlayerLocation[0]).getTiles().get(currentPlayerLocation[1]).getTileType();
        }

        TileType chitCardTileType = chitCard.getKey();
        if (currentTileType != chitCardTileType) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(false);
            result.add(false);
            return result;
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
