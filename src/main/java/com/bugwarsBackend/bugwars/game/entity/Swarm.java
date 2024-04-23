package com.bugwarsBackend.bugwars.game.entity;

import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class Swarm implements Entity {
    private final List<Bug> bugs;
    private String name;
    public Swarm(String name) {
        this.name = name;
        this.bugs = new ArrayList<>();
    }

    @Override
    public void setPosition(Point position) {
        //implement logic
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public String toString() {
        return name; // Return the name of the swarm
    }
}

