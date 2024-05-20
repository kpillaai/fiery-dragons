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

    private Turn() {
    }

    /**
     * @author  Zilei Chen
     * @desc    Static constructor for Turn class to ensure only one instance of Turn is ever initialised.
     * Returns Turn instance.
     */
    public static synchronized Turn getInstance(){
        if (instance == null) {
            instance = new Turn();
        }
        return instance;
    }

    /**
     * @author  Zilei Chen
     * @desc    Ends the turn of the current player and iterates to the next player. Also tells BoardController to
     * render the changes.
     */
    public void endTurn() {
        Game.getInstance().iterateNextPlayer();
        BoardController.getInstance().showCurrentPlayer();
        BoardController.getInstance().hideCard();
    }

    /**
     * @author  Zilei Chen
     * @desc    Based on the results of Turn logic, it will play out the turn by either moving the player or ending
     * turn
     * Param is the index of the Chit Card the player flipped.
     */
    public void nextTurn(Integer chitCardId) throws IOException {
        boolean[] turnResult = this.handleTurnLogic(chitCardId);
        boolean canPlayerMove = turnResult[0];
        boolean playerWon = turnResult[1];
        Pair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);

        if (!canPlayerMove) { // End turn if player cannot move
            endTurn();
        }

        if (canPlayerMove & !playerWon) {
            int moveValue = chitCard.getValue();
            Board.getInstance().movePlayer(Game.getInstance().getCurrentPlayer(), moveValue);
            BoardController.movePlayer(chitCard);
            if (chitCard.getValue() < 0) { // End turn if player cannot move or pirate
                endTurn();
            }
        }
        if (playerWon) {
            Game.getInstance().endGame();
        }
    }

    /**
     * @author  Zilei Chen
     * @desc    Handles the Turn Logic of a turn using the Chain of Responsibility design pattern.
     * Param is the index of the Chit Card the player flipped.
     */
    private boolean[] handleTurnLogic(Integer chitCardId) throws IOException {
        boolean playerWon = false;
        TurnHandler t1 = new CheckTile();
        TurnHandler t2 = new MovePastCave();
        TurnHandler t3 = new NextTileContainsPlayer();
        TurnHandler t4 = new CheckForWin();
        t1.setNextStep(t2);
        t2.setNextStep(t3);
        t3.setNextStep(t4);

        Pair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);
        ArrayList<Boolean> canPlayerMove = t1.handleTurn(chitCard);

        if (canPlayerMove.get(1)) { // Win game if player won the game
            playerWon = true;
        }
        return new boolean[]{canPlayerMove.getFirst(), playerWon};
    }
}
