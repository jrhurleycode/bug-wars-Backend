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

    public void addBug(Bug bug) {
        bugs.add(bug);
    }

    public void removeBug(Bug bug) {
        bugs.remove(bug);
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

