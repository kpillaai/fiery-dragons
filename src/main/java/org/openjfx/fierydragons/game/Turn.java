package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;

import java.util.ArrayList;

public class Turn {

    private static Turn instance;

    private ArrayList<Player> playerList;
    private Turn() {
        this.playerList = new ArrayList<Player>();
    }

    public static synchronized Turn getInstance(){
        if (instance == null) {
            instance = new Turn();
        }
        return instance;
    }

    public int getPlayerInput() {
        return 0;
    }

    public void nextTurn() {

    }

    public boolean handleTurn(Player player, Integer chitCardId) {
        return false;
    }

    public void addPlayer(Player player) {
        getPlayerList().add(player);
    }

    public void removePlayer(int index) {
        getPlayerList().remove(index);
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
