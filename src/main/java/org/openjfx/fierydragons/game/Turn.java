package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.turnlogic.*;
import org.openjfx.fierydragons.turnlogic.CheckTile;
import org.openjfx.fierydragons.turnlogic.MovePastCave;
import org.openjfx.fierydragons.turnlogic.NextTileContainsPlayer;
import org.openjfx.fierydragons.turnlogic.TurnHandler;


import java.util.ArrayList;

public class Turn {

    private static Turn instance;

    private ArrayList<Player> playerList;

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
        TurnHandler t1 = new CheckTile();
        TurnHandler t2 = new MovePastCave();
        TurnHandler t3 = new NextTileContainsPlayer();
        TurnHandler t4 = new CheckForWin();
        t1.setNextStep(t2);
        t2.setNextStep(t3);
        t3.setNextStep(t4);

        //Figure out a way to get the chitcard type and number here
        /*
        Deck chitCards = Board.getInstance().getDeck().getChitCards();
        ArrayList<Boolean> canPlayerMove = t1.handleTurn(chitCards.get(chitCardId));
        handleTurn(Game.getInstance.getCurrentPlayer());
         */
    }

    private boolean handleTurn(Player player, Integer chitCardId) {
        return false;
    }

    // delete this later
    public void addPlayer(Player player) {
        this.playerList.add(player);
    }


}
