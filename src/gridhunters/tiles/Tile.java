package gridhunters.tiles;

import gridhunters.Item;
import gridhunters.Game;
import gridhunters.GameGUI;
import java.io.Serializable;

public abstract class Tile implements Serializable {
    Game game;
    public int x;
    public int y;
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
    
    public void exploreVisual() {
        this.visited = true;
    }

    public String getType() {
        return type.substring(0, 1).toUpperCase();
    }
    
    public boolean isVisited() {
        return this.visited;
    }
    
    public void setVisited(boolean visited) {
        this.visited = visited;
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
    
    public abstract String getDescription();
    
    public void playerArrive(GameGUI gui) {
    }
}