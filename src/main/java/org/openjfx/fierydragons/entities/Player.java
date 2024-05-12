package org.openjfx.fierydragons.entities;

import org.openjfx.fierydragons.game.Turn;

public class Player {
    private String name;

    private int id;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void flipCard(Integer chitCardId) {
        Turn.getInstance().handleTurn(this, chitCardId);
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
}
