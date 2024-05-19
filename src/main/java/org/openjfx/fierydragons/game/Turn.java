package org.openjfx.fierydragons.game;

import javafx.util.Pair;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.render.BoardController;
import org.openjfx.fierydragons.turnlogic.*;
import org.openjfx.fierydragons.turnlogic.CheckTile;
import org.openjfx.fierydragons.turnlogic.MovePastCave;
import org.openjfx.fierydragons.turnlogic.NextTileContainsPlayer;
import org.openjfx.fierydragons.turnlogic.TurnHandler;


import java.io.IOException;
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
        Game.getInstance().iterateNextPlayer();
    }
    public void nextTurn(Integer chitCardId) {
        boolean canPlayerMove = this.handleTurnLogic(chitCardId);
        // Move player here
        Pair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);
        BoardController.movePlayer(chitCard);
        System.out.println("wokring??");
        if (canPlayerMove) {
            Board.getInstance().movePlayer(Game.getInstance().getCurrentPlayer(), chitCard.getValue());


        }
    }

    private boolean handleTurnLogic(Integer chitCardId) throws IOException {
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

        if (canPlayerMove.get(1)) {
            Game.getInstance().endGame();
        }

        return canPlayerMove.getFirst();
    }

    // delete this later
    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

}
