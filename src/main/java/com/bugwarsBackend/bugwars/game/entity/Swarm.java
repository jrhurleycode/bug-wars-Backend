package com.bugwarsBackend.bugwars.game.entity;

import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class Swarm implements Entity {
    private final List<Bug> bugs = new ArrayList<>();
    private String name;

    public Swarm(String name) {
        this.name = name;
    }

    private void addBug(Bug bug) {
        bugs.add(bug);
    }

    public Bug getBug(int index) {
        return bugs.get(index);
    }

    @Override
    public void setPosition(Point position) {
        //implement logic
    }

    @Override
    public Point getPosition() {
        return null;
    }
}

