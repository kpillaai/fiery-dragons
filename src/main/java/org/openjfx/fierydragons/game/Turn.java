package org.openjfx.fierydragons.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.openjfx.fierydragons.CustomPair;
import org.openjfx.fierydragons.GameState;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.entities.TileType;
import org.openjfx.fierydragons.render.BoardController;
import org.openjfx.fierydragons.turnlogic.*;
import org.openjfx.fierydragons.turnlogic.CheckTile;
import org.openjfx.fierydragons.turnlogic.MovePastCave;
import org.openjfx.fierydragons.turnlogic.NextTileContainsPlayer;
import org.openjfx.fierydragons.turnlogic.TurnHandler;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Turn {

    private static Turn instance;

    @JsonCreator
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

    public static void setInstance(Turn instance) {
        Turn.instance = instance;
    }

    /**
     * @author  Zilei Chen
     * @desc    Ends the turn of the current player and iterates to the next player. Also tells BoardController to
     * render the changes.
     */
    public boolean endTurn() throws IOException {
        boolean continueGame = checkTurnTimers();
        if (!continueGame) {
            return false;
        }
        BoardController.getInstance().pauseTimer();
        Game.getInstance().iterateNextPlayer();
        BoardController.getInstance().showCurrentPlayer();
        BoardController.getInstance().hideCard();
        BoardController.getInstance().resumeTimer();
        return true;
    }

    private boolean checkTurnTimers() throws IOException {
        // Check if all players run out of time
        ArrayList<Player> playerList = Game.getInstance().getPlayerList();
        int timeRemainingEqualsZeroCounter = 0;
        for (Player player : playerList) {
            if (player.getTimeRemainingSeconds() <= 0) {
                timeRemainingEqualsZeroCounter += 1;
            }
        }
        if (timeRemainingEqualsZeroCounter == playerList.size()) {
            Player winningPlayer = calculateTimeWin();
            Game.getInstance().endGame(winningPlayer);
            return false;
        }
        return true;
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
        CustomPair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);

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
            Game.getInstance().endGame(Game.getInstance().getCurrentPlayer());
        }
    }

    /**
     * @author  Zilei Chen
     * @desc    Handles the Turn Logic of a turn using the Chain of Responsibility design pattern.
     * Param is the index of the Chit Card the player flipped.
     */
    private boolean[] handleTurnLogic(Integer chitCardId) throws IOException {
        boolean playerWon = false;
        TurnHandler t0 = new CheckTimer();
        TurnHandler t1 = new CheckTile();
        TurnHandler t2 = new MovePastCave();
        TurnHandler t3 = new NextTileContainsPlayer();
        TurnHandler t4 = new CheckForWin();
        t0.setNextStep(t1);
        t1.setNextStep(t2);
        t2.setNextStep(t3);
        t3.setNextStep(t4);

        CustomPair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);

        ArrayList<Boolean> canPlayerMove = t1.handleTurn(chitCard);

        if (canPlayerMove.get(1)) { // Win game if player won the game
            playerWon = true;
        }
        return new boolean[]{canPlayerMove.getFirst(), playerWon};
    }

    // Save the entire game state to a JSON file
    public void saveGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), this);
    }

    // Load the entire game state from a JSON file
    public static Turn loadGame(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Turn.class);
    }

    private Player calculateTimeWin() {
        // Todo: find all player distances, find the least, they win, if tie, no one wins
        Player minDistancePlayer = new Player("ITS A TIE", 10000);
        Integer minDistance = Integer.MAX_VALUE;
        // Find the player closest to winning
        for (Player player : Game.getInstance().getPlayerList()) {
            if (player.getDistanceToCave() < minDistance) {
                minDistance = player.getDistanceToCave();
                minDistancePlayer = player;
            }
        }
        // Find all other players the same distance away
        ArrayList<Player> winnersList = new ArrayList<>();
        for (Player player : Game.getInstance().getPlayerList()) {
            if (player.getDistanceToCave() == minDistance) {
                winnersList.add(player);
            }
        }
        // If more than 1 player wins, all of them win/tie
        if (winnersList.size() > 1) {
            minDistancePlayer = new Player("", -100);
            for (Player winner : winnersList) {
                minDistancePlayer.setName(STR."\{minDistancePlayer.getName()} \{winner.getName()}");
            }
        }
        return minDistancePlayer;
    }
}
