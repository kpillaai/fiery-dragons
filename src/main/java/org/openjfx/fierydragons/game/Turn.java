package org.openjfx.fierydragons.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjfx.fierydragons.gameSaving.CustomPair;
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

    public static void resetTurn() {
        instance = null;
    }

    /**
     * @author  Zilei Chen
     * @desc    Ends the turn of the current player and iterates to the next player. Also tells BoardController to
     * render the changes.
     */
    public boolean endTurn() throws IOException, NoSuchFieldException, IllegalAccessException {
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

    private boolean checkTurnTimers() throws IOException, NoSuchFieldException, IllegalAccessException {
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
    public void nextTurn(Integer chitCardId) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<Boolean> turnOutcome = this.handleTurnLogic(chitCardId);
        boolean canPlayerMove = turnOutcome.get(0);
        boolean playerWon = turnOutcome.get(1);
        boolean isSwap = turnOutcome.get(2);
        CustomPair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);

        if (!canPlayerMove) { // End turn if player cannot move
            endTurn();
        }

        if (isSwap) {
            if (Board.getInstance().getPlayerLocation(Game.getInstance().getCurrentPlayer())[1] >= 0 ) {
                Player playerToSwap = Board.getInstance().findClosestPlayer(Game.getInstance().getCurrentPlayer());
                System.out.println("Closest Player: " + playerToSwap.getId());
                Board.getInstance().swapPlayers(Game.getInstance().getCurrentPlayer(), playerToSwap);
                BoardController.getInstance().swapPlayerToken(Game.getInstance().getCurrentPlayer(), playerToSwap);
            }
            endTurn();
            return; // Exit method after swap
        }

        if (canPlayerMove & !playerWon) {
            int moveValue = chitCard.getValue();
            Board.getInstance().movePlayer(Game.getInstance().getCurrentPlayer(), moveValue);
            BoardController.movePlayer(chitCard);

            System.out.println(Game.getInstance().getCurrentPlayer().getDistanceToCave());
            System.out.println(Game.getInstance().getCurrentPlayer().getName());
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
    private ArrayList<Boolean> handleTurnLogic(Integer chitCardId) throws IOException {
        TurnHandler t0 = new CheckTimer();
        TurnHandler t1 = new CheckSwap();
        TurnHandler t2 = new CheckTile();
        TurnHandler t3 = new MovePastCave();
        TurnHandler t4 = new NextTileContainsPlayer();
        TurnHandler t5 = new CheckForWin();
        t0.setNextStep(t1);
        t1.setNextStep(t2);
        t2.setNextStep(t3);
        t3.setNextStep(t4);
        t4.setNextStep(t5);

        CustomPair<TileType, Integer> chitCard = Board.getInstance().getDeck().getChitCard(chitCardId);

        return t0.handleTurn(chitCard);
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
