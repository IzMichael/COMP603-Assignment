package gridhunters.tiles;

import java.util.ArrayList;
import gridhunters.items.Item;
import gridhunters.Game;
import gridhunters.ui.GameGUI;

public class TreasureTile extends Tile {
    ArrayList<Item> treasureChest = new ArrayList<>();

    public TreasureTile(Game game, int x, int y) {
        super(game, "Treasure", x, y);
        for (int i = 0; i < 5; i++) {
            this.treasureChest.add(new Item());
        }
    }    

    public ArrayList<Item> getTreasureChest() {
        return this.treasureChest;
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
        return "(" + this.x + ", " + this.y + ") You come across a treasure chest...";
    }
    
    @Override
    public void playerArrive(GameGUI gui) {
        gui.setTreasureTile(this);
        gui.logMessage("Press [R] to rummage through the treasure chest.");
    }
}