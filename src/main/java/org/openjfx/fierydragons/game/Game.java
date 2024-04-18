package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;

public class Game {
    private Board board;
    public Game() {
        this.board = Board.getInstance();
    }
    public Board getBoard() {
        return board;
    }

    public void initialise() {
        //set up the board
        Board.getInstance().initialiseBoard("");
        //set up players
        Turn turn = new Turn();
        turn.addPlayer(new Player("Player1", 1));
        turn.addPlayer(new Player("Player2", 2));
        turn.addPlayer(new Player("Player3", 3));
        turn.addPlayer(new Player("Player4", 4));
        //set up turn requests
    }

    public void startGame() {

    }

    public String fetchSettings() {
        return "";
    }
}
