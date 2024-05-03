package com.bugwarsBackend.bugwars.game.entity;

import com.bugwarsBackend.bugwars.service.ScriptService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class Bug implements Entity {
    @Getter
    private final Map<Integer, Command> commands = new HashMap<>();
    private final Swarm swarm;
    private Point coords;
    @Getter
    private Direction direction;
    private int[] userBytecode;
    private int index = 0;

    private boolean moved = false;
    @Autowired
    ScriptService scriptService;

    public Bug(Swarm swarm, Point coords, Direction direction, int[] userBytecode) {
        this.swarm = swarm;
        this.coords = coords;
        this.direction = direction;
        this.userBytecode = userBytecode;
    }

    public Bug(Swarm swarm, Point coords) {
        this(swarm, coords, Direction.NORTH, null);
    }

    public Bug(Swarm swarm, Point coords, Direction direction) {
        this(swarm, coords, direction, null);
    }

    public Bug(Swarm swarm, Direction direction) {
        this(swarm, new Point(), direction, null);
    }

    public Bug(Point coords) {
        this(null, coords, Direction.NORTH, null);
    }

    public Bug(Swarm swarm, int[] userBytecode) {
        this(swarm, new Point(), Direction.NORTH, userBytecode);
    }

    public void loadCommands() {
        commands.put(30, this::ifEnemy);
        commands.put(31, this::ifAlly);
        commands.put(32, this::ifFood);
        commands.put(33, this::ifEmpty);
        commands.put(34, this::ifWall);
        commands.put(35, this::_goto);
    }

    public boolean ifEnemy(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug enemyBug = (Bug) frontEntity;
            return enemyBug.swarm != swarm;
        }
        return false;
    }

    public boolean ifAlly(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug allyBug = (Bug) frontEntity;
            return allyBug.swarm == swarm;
        }
        return false;
    }

    public boolean ifFood(Entity frontEntity) {
        return frontEntity instanceof Food;
    }

    public boolean ifEmpty(Entity frontEntity) {
        return frontEntity == null;
    }

    public boolean ifWall(Entity frontEntity) {
        return frontEntity instanceof Wall;
    }

    public boolean _goto(Entity frontEntity) {
        // Implement the logic for determining whether the bug should move to the specified location
        // This method should return true if the bug should move, false otherwise
        // You need to determine the conditions under which the bug should move based on your game's requirements
        if (frontEntity instanceof Food) {
            // Move to the location if it contains food
            return true;
        } else if (frontEntity instanceof Wall) {
            // Do not move if the location contains a wall
            return false;
        } else if (frontEntity instanceof Bug) {
            // Move to the location if it contains an enemy bug
            Bug enemyBug = (Bug) frontEntity;
            return enemyBug.getSwarm() != swarm;
        } else {
            // Move to the location by default if no specific condition is met
            return true;
        }
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setPosition(Point position) {
    }

    @Override
    public String toString() {
        String color;
        String bugTypeString = "";

        if (swarm.getName().equals("0")) {
            color = "\033[0;34m"; // blue
            bugTypeString = "^";
        } else if (swarm.getName().equals("1")) {
            color = "\033[0;31m"; // red
            bugTypeString = "^";
        } else {
            throw new IllegalStateException("Unexpected value: " + swarm);
        }

        return String.format("%s%s\033[0m", color, bugTypeString);
    }


    @FunctionalInterface
    interface Command {
        boolean execute(Entity frontEntity);
    }


}
