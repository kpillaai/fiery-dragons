package org.openjfx.fierydragons.game;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.turnlogic.*;
import org.openjfx.fierydragons.turnlogic.CheckTile;
import org.openjfx.fierydragons.turnlogic.MovePastCave;
import org.openjfx.fierydragons.turnlogic.NextTileContainsPlayer;
import org.openjfx.fierydragons.turnlogic.TurnHandler;


import java.util.ArrayList;

public class Turn {

    private static Turn instance;

    private ArrayList<Player> playerList = new ArrayList<>();

    private Turn() {

    }

    public static synchronized Turn getInstance(){
        if (instance == null) {
            instance = new Turn();
        }
        return instance;
    }

    public void endTurn() {
        /*
        Game.getInstance().iterateNextPlayer();
         */
    }
    public void nextTurn(Integer chitCardId) {
        boolean canPlayerMove = this.handleTurnLogic(chitCardId);
        // Move player here

    }

    private boolean handleTurnLogic(Integer chitCardId) {
        Player currentPlayer = Game.getInstance().getCurrentPlayer();
        TurnHandler t1 = new CheckTile();
        TurnHandler t2 = new MovePastCave();
        TurnHandler t3 = new NextTileContainsPlayer();
        TurnHandler t4 = new CheckForWin();
        t1.setNextStep(t2);
        t2.setNextStep(t3);
        t3.setNextStep(t4);

        Pair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);
        ArrayList<Boolean> canPlayerMove = t1.handleTurn(chitCard);

        return canPlayerMove.getFirst();
    }

    // delete this later
    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public Player getPlayer(int index) {
        return this.playerList.get(index);
    }


}
