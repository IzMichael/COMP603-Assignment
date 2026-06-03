package gridhunters.patterns;

import gridhunters.Enemy;
import gridhunters.items.Item;
import gridhunters.tiles.Tile;
import java.util.List;

public interface GameObserver {
    void onPlayerStatsChanged(int health, int maxHealth, String logText);
    void onTileDiscovered(Tile tile, String description);
    void onCombatTriggered(Enemy enemy);
    void onLootDiscovered(List<Item> items);
    void onGameOver(boolean won);
}