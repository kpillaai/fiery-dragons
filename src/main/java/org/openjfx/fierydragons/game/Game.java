package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.render.BoardController;

import java.io.IOException;
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
        //set up turn requests
    }

    public void addPlayers() {
        this.playerList = new ArrayList<>();

        for (int i = 1; i < playerCount+1; i++) {
            this.playerList.add(new Player("Player" + i, i));
        }
        this.currentPlayer = this.playerList.get(0);
    }



    public void startGame() {

    }

    public String fetchSettings() {
        return "";
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void iterateNextPlayer() {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == this.getCurrentPlayer()) {
                if (i == playerList.size() - 1) {
                    this.currentPlayer = playerList.getFirst();
                    System.out.println(this.currentPlayer.getId());
                    break;
                } else {
                    this.currentPlayer = playerList.get(i + 1);
                    System.out.println(this.currentPlayer.getId());
                    break;
                }
            }
        }
    }

    public void endGame() throws IOException {
        BoardController.getInstance().switchToWinScene(BoardController.getInstance().anchorPane);
    }
}
