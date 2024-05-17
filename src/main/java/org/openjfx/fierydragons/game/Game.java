package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;

import java.util.ArrayList;

public class Game {

    private static Game instance;

    private ArrayList<Player> playerList;

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

        Turn.getInstance().addPlayer(new Player("Player1", 1));
        Turn.getInstance().addPlayer(new Player("Player2", 2));
        Turn.getInstance().addPlayer(new Player("Player3", 3));
        Turn.getInstance().addPlayer(new Player("Player4", 4));
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
}
