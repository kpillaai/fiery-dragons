package org.openjfx.fierydragons.turnlogic;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.TileType;

import java.util.ArrayList;

public class CheckTile extends TurnHandler {
    public CheckTile() {
    }

    @Override
    public ArrayList<Boolean> handleTurn(Pair<TileType, Integer> chitCard) {
        TileType currentPlayerTile = TileType.BABY_DRAGON; // Todo
        TileType chitCardTileType = chitCard.getKey();
        if (currentPlayerTile != chitCardTileType) {
            ArrayList<Boolean> result = new ArrayList<>();
            result.add(false);
            result.add(false);
            return result;
        }
        return this.nextStep.handleTurn(chitCard);
    }
}
