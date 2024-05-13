package org.openjfx.fierydragons.game;

import org.openjfx.fierydragons.entities.Player;
import org.openjfx.fierydragons.turnlogic.CheckTile;
import org.openjfx.fierydragons.turnlogic.MovePastCave;
import org.openjfx.fierydragons.turnlogic.NextTileContainsPlayer;
import org.openjfx.fierydragons.turnlogic.TurnHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Turn {

    private static Turn instance;

    private ArrayList<Player> playerList;
    private Turn() {
        this.playerList = new ArrayList<Player>();

        TurnHandler t1 = new CheckTile();
        TurnHandler t2 = new MovePastCave();
        TurnHandler t3 = new NextTileContainsPlayer();

        t1.setNextStep(t2);
        t2.setNextStep(t3);

        ArrayList<Boolean> canPlayerMove = t1.handleTurn("HI");


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

    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public boolean handleTurn(Player player, Integer chitCardId) {
        return false;
    }


    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
