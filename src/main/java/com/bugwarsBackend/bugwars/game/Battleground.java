package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.*;
import com.bugwarsBackend.bugwars.game.setup.TurnOrderCalculator;
import com.bugwarsBackend.bugwars.model.Script;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Battleground {
    private List<Bug> bugs;
    private String name;
    private Swarm swarm;
    private Entity[][] grid;
    private final Map<Integer, Action> actions = new HashMap<>();
    private final Map<Integer, Command> commands = new HashMap<>();
    private List<Bug> turnOrder;
    private int index;

    public Battleground(String name, Entity[][] grid, List<Bug> bugs) {
        this.name = name;
        this.grid = grid;
        if (bugs != null) {
            this.bugs = new ArrayList<>(bugs);
        } else {
            this.bugs = new ArrayList<>();
        }
//        this.swarm0 = new Swarm("0");
//        this.swarm1 = new Swarm("1");

        init();
        loadCommands();
        updateGrid();
    }


    public void print() {
        for (Entity[] entities : grid) {
            for (Entity entity : entities) {
                if (entity == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(entity);
                }
            }
            System.out.println();
        }
    }

    public TickSummary nextTick(Script script) {
        int[] bytecode = Arrays.copyOf(script.getBytecode(), script.getBytecode().length);

        TurnOrderCalculator turnOrderCalculator = new TurnOrderCalculator(grid, new Swarm("0"), new Swarm("1"));
        List<Bug> turnOrder = turnOrderCalculator.calculateTurnOrder();

        List<ActionSummary> actionsTaken = new ArrayList<>();
        for (int action : bytecode) {
            for (Bug bug : turnOrder) {
                Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());

                if (action >= 30 && action <= 35) {
                    switch (action) {
                        case 30:
                            bug.ifEnemy(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 31:
                            bug.ifAlly(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 32:
                            bug.ifFood(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 33:
                            bug.ifEmpty(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 34:
                            bug.ifWall(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 35:
                            bug._goto(getEntityAtCoords(bugFrontCoords));
                            break;
                        default:
                            throw new RuntimeException("Invalid command: " + action);
                    }
                    commands.get(action).execute(bug);
                } else if (actions.containsKey(action)) {
                    actionsTaken.add(new ActionSummary(bug.getCoords(), action));
                    print();
                    actions.get(action).run(bug);
                } else {
                    throw new RuntimeException("Invalid command: " + action);
                }
                updateGrid();
            }
        }
        return new TickSummary(actionsTaken, lastSwarmStanding());
    }

    private void updateGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Bug bug) {
                    Point newCoords = bug.getCoords();
                    if (newCoords == null) {
                        bug.setCoords(new Point(i, j));
                        bug.setDirection(Direction.NORTH);
                        newCoords = bug.getCoords();
                    } else {
                        grid[i][j] = null;
                    }
                    grid[newCoords.y][newCoords.x] = bug;
                } else if (grid[i][j] instanceof Food || grid[i][j] instanceof Wall || grid[i][j] instanceof EmptySpace) {
                }
            }
        }
    }


    private void init() {
        actions.put(0, bug -> noop(bug));
        actions.put(10, bug -> mov(bug));
        actions.put(11, bug -> rotr(bug));
        actions.put(12, bug -> rotl(bug));
        actions.put(13, bug -> att(bug));
        actions.put(14, bug -> eat(bug));
    }

    private void noop(Bug bug) {
    }
    // keep moving until you find an entity
    private void mov(Bug bug) {
        Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
        Entity destination = getEntityAtCoords(bugFrontCoords);

//        if (destination != null) return;

        grid[bugFrontCoords.y][bugFrontCoords.x] = bug;
        grid[bug.getCoords().y][bug.getCoords().x] = null;
        bug.setCoords(bugFrontCoords);

        // if there's a wall, then rotr and rotl
        // if there's nothing, keep going
        // if there is an entity, then do go to movement
        bug.setMoved(true); // Set moved flag to true regardless of movement success
    }

    private void rotr(Bug bug) {
        Direction newDirection = bug.getDirection().turnRight();
        bug.setDirection(newDirection);
    }

    private void rotl(Bug bug) {
        Direction newDirection = bug.getDirection().turnLeft();
        bug.setDirection(newDirection);
    }

    private void att(Bug bug) {
        Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
        Entity target = getEntityAtCoords(bugFrontCoords);

        if (target == null) return;

        if (target instanceof Bug) {
            removeBugAndReplaceWithFood(bugFrontCoords);
        } else if (target instanceof Food) {
            removeFood(bugFrontCoords);
        }
    }

    private void removeBugAndReplaceWithFood(Point bugFrontCoords) {
        Entity entity = grid[bugFrontCoords.y][bugFrontCoords.x];
        if (!(entity instanceof Bug)) return;

        Bug targetBug = (Bug) entity;
        if (bugs.indexOf(targetBug) < index) index--;
        bugs.remove(targetBug);
        grid[bugFrontCoords.y][bugFrontCoords.x] = new Food();
    }

    private void removeFood(Point bugFrontCoords) {
        Entity entity = grid[bugFrontCoords.y][bugFrontCoords.x];
        if (!(entity instanceof Food)) return;

        grid[bugFrontCoords.y][bugFrontCoords.x] = null;
    }

    private void eat(Bug bug) {
        Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
        Entity target = getEntityAtCoords(bugFrontCoords);

        if (!(target instanceof Food)) return;

        Bug newSpawn = new Bug(
                bug.getSwarm(),
                bugFrontCoords,
                bug.getDirection(),
                bug.getUserBytecode()
        );

        grid[bugFrontCoords.y][bugFrontCoords.x] = newSpawn;
        bugs.add(index, newSpawn);
        index++;

        bug.setUserBytecode(doubleUserBytecode(bug.getUserBytecode()));
    }

    private int[] doubleUserBytecode(int[] UserBytecode) {
        int[] newUserBytecode = new int[UserBytecode.length * 2];
        System.arraycopy(UserBytecode, 0, newUserBytecode, 0, UserBytecode.length);
        System.arraycopy(UserBytecode, 0, newUserBytecode, UserBytecode.length, UserBytecode.length);
        return newUserBytecode;
    }

    private Entity getEntityAtCoords(Point coords) {
        int y = coords.y;
        int x = coords.x;

        if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
            //System.out.println("Valid Coords: " + coords);
            return grid[y][x];
        } else {
            System.out.println("Coordinates are out of bounds from getEntityAtCoords method.");
            // Coordinates are out of bounds, return null or handle appropriately
            return null;
        }
    }
    private boolean lastSwarmStanding() {
        return bugs.stream().map(Bug::getSwarm).distinct().limit(2).count() <= 1;
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
            return enemyBug.getSwarm() != this.swarm;
        }
        return false;
    }

    public boolean ifAlly(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug allyBug = (Bug) frontEntity;
            return allyBug.getSwarm() == this.swarm;
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
        if (frontEntity instanceof Food) {
            return true;
        } else if (frontEntity instanceof Wall) {
            return false;
        } else if (frontEntity instanceof Bug) {
            Bug enemyBug = (Bug) frontEntity;
            return enemyBug.getSwarm() != swarm;
        } else {
            return true;
        }
    }

    @FunctionalInterface
    interface Action {
        void run(Bug bug);
    }

    @FunctionalInterface
    interface Command {
        boolean execute(Entity frontEntity);
    }
}
