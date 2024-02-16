package com.bugwarsBackend.bugwars.game.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor  // Exclude Entity fields from constructor generation
public class Bug implements Entity {
    private int bugType;

    @Override
    public void setPosition(Point position) {
    }

    public Bug(int bugType) {
        this.bugType = bugType;
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(bugType); // Return the bugType as a string
    }
}
