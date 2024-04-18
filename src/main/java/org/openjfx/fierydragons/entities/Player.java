package org.openjfx.fierydragons.entities;

public class Player {
    private String name;

    private int id;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int flipCard() {
        return 0;
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
