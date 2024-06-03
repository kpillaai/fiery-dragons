package org.openjfx.fierydragons.turnlogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.gameSaving.CustomPair;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;

import java.util.ArrayList;

public class CheckTile extends TurnHandler {

    @JsonCreator
    public CheckTile() {
    }

    /**
     * @author  Zilei Chen
     * @desc    This method extends TurnHandler, intends to check whether the chit card flipped matches the current
     * tile the player is on.
     * Returns 2 booleans representing if the player can move, and if the player will win this turn. Takes a Pair which
     * represents chit cards and their number value.
     */
    @Override
    public ArrayList<Boolean> handleTurn(CustomPair<TileType, Integer> chitCard) {
        if (chitCard.getKey() == TileType.PIRATE) {
            return this.nextStep.handleTurn(chitCard);
        }

        if (chitCard.getKey() == TileType.SWAP) {
            return this.nextStep.handleTurn(chitCard);
        }

        int[] currentPlayerLocation = Board.getInstance().getPlayerLocation(Game.getInstance().getCurrentPlayer());
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
