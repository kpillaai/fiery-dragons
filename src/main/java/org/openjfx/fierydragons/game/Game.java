package org.openjfx.fierydragons.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.render.BoardController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Game {

    private static Game instance;

    private ArrayList<Player> playerList;

    private int playerCount;

    private Player currentPlayer;

    @JsonCreator
    private Game() {
        //initialise();
    }

    public static void resetGame() {
        instance = null;
    }

    public static void setInstance(Game instance) {
        Game.instance = instance;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @author  Zilei Chen
     * @desc    Creates an instance of the Game to make it a singleton
     */
    public static synchronized Game getInstance(){
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * @author  Zilei Chen
     * @desc    Initialises the ball by calling the initialiseBoard method
     */
    public void initialise() {
        Board.getInstance().initialiseBoard("");
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Adds players to an array list depending on the input to keep track of the players in the game
     */
    public void addPlayers() {
        if (playerList == null) {
            this.playerList = new ArrayList<>();
            for (int i = 1; i < playerCount+1; i++) {
                this.playerList.add(new Player("Player" + i, i));
            }
            this.currentPlayer = this.playerList.getFirst();

        }
        // call createPlayerLocationArray to initialise player locations on the map
        Board.getInstance().createPlayerLocationArray();

    }

    /**
     * @author  Zilei Chen
     * @desc    returns the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    returns the current number of players in the game
     */
    public int getPlayerCount() {
        return this.playerCount;
    }

    /**
     * @author  Jeffrey Yan
     * @desc    Takes an integer input which is the number of players and sets the number of players
     */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * @author  Zilei Chen
     * @desc    Used to set the next current player after a turn has ended
     */
    public void iterateNextPlayer() {
        // loop through all the players
        for (int i = 0; i < playerList.size(); i++) {
            // get the current player
            if (Objects.equals(playerList.get(i).getName(), this.getCurrentPlayer().getName())) {
                // handle index errors, set the next player to be the one after the previous
                if (i == playerList.size() - 1) {
                    this.currentPlayer = playerList.getFirst();
                    break;
                } else {
                    this.currentPlayer = playerList.get(i + 1);
                    break;
                }
            }
        }
    }

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    used when the game has ended, calls the switchToWinScene to display the winning player
     */
    public void endGame(Player winningPlayer) throws IOException {
        System.out.println("game is ending");
        BoardController.getInstance().switchToWinScene(BoardController.getInstance().anchorPane, winningPlayer);
    }
}
