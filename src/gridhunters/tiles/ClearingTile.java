package gridhunters.tiles;

import gridhunters.Game;
import gridhunters.Item;

public class ClearingTile extends Tile {

    public ClearingTile(Game game, int x, int y) {
        super(game, "Clearing", x, y);
    }

    @Override
    public void explore() {
    }

    @Override
    public Item interact() {
        return null;
    }
    
    @Override
    public String getDescription() {
        return "(" + this.x + ", " + this.y + ") You find yourself in an empty clearing...";
    }
}