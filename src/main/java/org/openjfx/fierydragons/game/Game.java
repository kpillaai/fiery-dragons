package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;

import java.util.ArrayList;

public class Game {

    private static Game instance;

    private ArrayList<Player> playerList;

    private int playerCount;

    private Player currentPlayer;

    private Game() {
        //initialise();
    }

    public static synchronized Game getInstance(){
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void initialise() {
        //set up the board
        Board.getInstance().initialiseBoard("");
        //set up players
        for (int i = 1; i < playerCount+1; i++) {
            Turn.getInstance().addPlayer(new Player("Player" + i, i));
        }
        this.currentPlayer = Turn.getInstance().getPlayer(0);
        //set up turn requests
    }



    public void startGame() {

    }

    public String fetchSettings() {
        return "";
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
