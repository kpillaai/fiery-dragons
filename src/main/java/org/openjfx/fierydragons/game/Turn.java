package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;

import java.util.ArrayList;

public class Turn {

    private ArrayList<Player> playerList;
    public Turn() {
        this.playerList = new ArrayList<Player>();
    }

    public int getPlayerInput() {
        return 0;
    }

    public void nextTurn() {

    }

    private boolean handleTurn() {
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
