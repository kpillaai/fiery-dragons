package org.openjfx.fierydragons.entities;

import org.openjfx.fierydragons.game.Board;
import org.openjfx.fierydragons.game.Game;
import org.openjfx.fierydragons.game.Turn;

import java.io.IOException;

public class Player {
    private String name;

    private int id;

    private int distanceToCave = 2;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        for (int i = 0; i < Board.getInstance().getMapPieces().size(); i++) {
            for (int j = 0; j < Board.getInstance().getMapPieces().get(i).getTiles().size(); j++) {
                this.distanceToCave += 1;
            }
        }
    }

    public void flipCard(Integer chitCardId) throws IOException {
        Turn.getInstance().nextTurn(chitCardId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistanceToCave() {
        return distanceToCave;
    }

    public void setDistanceToCave(int distanceToCave) {
        this.distanceToCave = distanceToCave;
    }

    public void addDistance(int moveValue) {
        this.distanceToCave = this.distanceToCave + moveValue;
    }
}
