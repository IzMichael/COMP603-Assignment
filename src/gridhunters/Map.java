/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import gridhunters.tiles.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import gridhunters.tiles.EnemyTile;
import gridhunters.tiles.ClearingTile;
import gridhunters.tiles.TreasureTile;
import java.io.Serializable;

/**
 *
 * @author Michael Martin
 */
public class Map implements Serializable {

    HashMap<String, Tile> tiles = new HashMap<>();
    transient Game game;
    int maxViewRadius = 3;

    public Map(Game game) {
        this.game = game;
        ClearingTile origin = new ClearingTile(this.game, 0, 0);
        setTile(0, 0, origin);
    }

    public ArrayList<StringBuilder> getMinimap(int x, int y, int radius) {
        ArrayList<StringBuilder> lines = new ArrayList<>();

        for (int i = 0 - maxViewRadius; i <= maxViewRadius; i++) {
            StringBuilder line = new StringBuilder();
            line.append("| ");
            for (int j = 0 - maxViewRadius; j <= maxViewRadius; j++) {
                Tile tile = this.getTile(j + x, (i * -1) + y);
                if (Math.abs(i) <= radius && Math.abs(j) <= radius && tile != null && tile.isVisited()) {
                    line.append(tile.getType());
                } else {
                    line.append(" ");
                }
                line.append(" ");
            }
            line.append("|");
            lines.add(line);
        }

        StringBuilder horizontalBorder = new StringBuilder();
        horizontalBorder.append("+");
        horizontalBorder.repeat("-", ((maxViewRadius * 2 * 2) - 4) / 2);
        horizontalBorder.append("Minimap");
        horizontalBorder.repeat("-", ((maxViewRadius * 2 * 2) - 4) / 2);
        horizontalBorder.append("+");

        lines.addFirst(horizontalBorder);
        lines.addLast(new StringBuilder(horizontalBorder.toString()));
        return lines;
    }

    public Tile getTile(int x, int y) {
        Tile tile = tiles.get(x + "," + y);
        if (tile == null) {
            tile = this.generateTile(x, y);
            this.setTile(x, y, tile);
        }
        return tile;
    }

    private void setTile(int x, int y, Tile tile) {
        tiles.put(x + "," + y, tile);
    }

    private Tile generateTile(int x, int y) {
        Random random = new Random();
        int typeIndex = random.nextInt(0, 3) + 1;
        switch (typeIndex) {
            case 1 -> {
                return new ClearingTile(this.game, x, y);
            }
            case 2 -> {
                return new TreasureTile(this.game, x, y);
            }
            case 3 -> {
                return new EnemyTile(this.game, x, y, false);
            }
            case 4 -> {
                return new EnemyTile(this.game, x, y, true);
            }
            default -> {
                return this.generateTile(x, y);
            }
        }
    }
}
