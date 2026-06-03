package gridhunters.patterns;

import gridhunters.Game;
import gridhunters.tiles.ClearingTile;
import gridhunters.tiles.EnemyTile;
import gridhunters.tiles.Tile;
import gridhunters.tiles.TreasureTile;
import java.io.Serializable;
import java.util.Random;

public class TileFactory implements Serializable {
    private Game game;

    public TileFactory(Game game) {
        this.game = game;
    }

    public Tile generateTile(int x, int y) {
        Random r = new Random();
        int typeIndex = r.nextInt(3) + 1;
        return switch (typeIndex) {
            case 1 -> new ClearingTile(game, x, y);
            case 2 -> new TreasureTile(game, x, y);
            case 3 -> new EnemyTile(game, x, y, false);
            default -> new ClearingTile(game, x, y);
        };
    }
}
