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
    
    Tile getToNorth() {
        return game.map.getTile(x, y + 1);
    }
    Tile getToEast() {
        return game.map.getTile(x + 1, y);
    }
    Tile getToSouth() {
        return game.map.getTile(x, y - 1);
    }
    Tile getToWest() {
        return game.map.getTile(x - 1, y);
    }
}
