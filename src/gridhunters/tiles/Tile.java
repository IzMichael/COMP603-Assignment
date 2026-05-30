/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters.tiles;

import gridhunters.Item;
import gridhunters.Game;
import java.io.Serializable;

/**
 *
 * @author Michael Martin
 */
public abstract class Tile implements Serializable {
    Game game;
    int x;
    int y;
    String type;
    boolean visited = false;
    
    public Tile(Game game, String type, int x, int y) {
        this.game = game;
        this.type = type;
        this.x = x;
        this.y = y;
    }
    
    public abstract Item interact();
    
    public abstract void explore();

    public String getType() {
        return type.substring(0, 1).toUpperCase();
    }
    
    public boolean isVisited() {
        return this.visited;
    }
    
    public Tile getToNorth() {
        return game.map.getTile(x, y + 1);
    }
    public Tile getToEast() {
        return game.map.getTile(x + 1, y);
    }
    public Tile getToSouth() {
        return game.map.getTile(x, y - 1);
    }
    public Tile getToWest() {
        return game.map.getTile(x - 1, y);
    }
    
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public void exploreVisual() {
        this.visited = true;
        this.game.player.setPosition(this.x, this.y);
    }
    
    public String getDescription() {
        return "(" + this.x + ", " + this.y + ") You traveled into an unknown sector.";
    }
}
