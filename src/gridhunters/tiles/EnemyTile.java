package gridhunters.tiles;

import java.util.Random;
import gridhunters.Enemy;
import gridhunters.Item;
import gridhunters.Game;
import gridhunters.GameGUI;
import gridhunters.items.Artefact;
import gridhunters.items.Potion;

public class EnemyTile extends Tile {

    Enemy enemy;
    boolean isBoss;
    boolean isDefeated = false;

    public EnemyTile(Game game, int x, int y, boolean isBoss) {
        super(game, "Enemy", x, y);
        Random r = new Random();
        int roll = r.nextInt(20);
        if (roll == 0) {
            this.isBoss = true;
        } else {
            this.isBoss = false;
        }
        Random random = new Random();
        int maxHealth, strength;
        if (this.isBoss) {
            maxHealth = random.nextInt(2, 7) * 50;
            strength = random.nextInt(10, 20);
        } else {
            maxHealth = random.nextInt(4, 11) * 12;
            strength = random.nextInt(5, 12);
        }
        this.enemy = new Enemy(maxHealth, strength, this.isBoss);
    }

    public Enemy getEnemy() {
        return this.enemy;
    }

    public boolean isDefeated() {
        return this.isDefeated;
    }
    
    public boolean isBoss() {
        return this.isBoss;
    }

    public Item claimDrops() {
        this.isDefeated = true;
        if (this.isBoss) {
            Artefact drop = new Artefact();
            this.game.player.addArtefact(drop);
            if (this.game.player.hasArtefact(Artefact.Artefacts.NATURES_COMPASS) || this.game.player.hasArtefact(Artefact.Artefacts.SONAR_GOGGLES)) {
                this.game.player.setMapRadius(3);
            }
            return null; 
        } else {
            Random r = new Random();
            if (r.nextInt(2) == 0) {
                return new Potion(Potion.potionType.HEALTH);
            } else {
                return new Item();
            }
        }
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
        if (this.isDefeated) {
            return "The enemy has already been defeated. You attack the remains just to be certain.";
        }
        if (isBoss == true) {
            return "(" + this.x + ", " + this.y + ") Your instincts scream for you to turn back. A legendary presence is felt nearby...";
        }
        return "(" + this.x + ", " + this.y + ") You feel an forboding presence in the area... \nA great and powerful enemy is near!";
    }
    
    @Override
    public void playerArrive(GameGUI gui) {
        if (!this.isDefeated) {
            gui.setEnemyTile(this);
            gui.logMessage("Press [F] to initiate combat with the enemy!");
        }
    }
}