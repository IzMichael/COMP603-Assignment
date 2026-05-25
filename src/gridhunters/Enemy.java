package gridhunters;

import java.util.Random;
import gridhunters.items.Artefact;

/**
 *
 * @author Michael Martin, LD Mao
 */
public class Enemy extends Creature {

    int strength;
    String name;
    boolean isBoss; 

    public Enemy(int maxHealth, int strength, boolean isBoss) {
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.strength = strength;
        this.isBoss = isBoss;
        if (this.isBoss == true) {
            this.name = this.generateBossName();
        } else {
        this.name = this.generateName();
    }
    }

    public void reset() {
        this.health = this.maxHealth;
        this.name = this.generateName();
    }

    public int attack(int strength, Player player) {
        Random random = new Random();
        double multiplier = random.nextDouble(0, 2);
        int finalDamage = (int) Math.round(strength * multiplier);

        // Spirit Staff
        if (player.hasArtefact(Artefact.Artefacts.SPIRIT_STAFF) && random.nextDouble() < 0.15) {
            System.out.println("Your Spirit Staff has summoned a spirit to assist in your attack.");
            finalDamage += finalDamage * 1.15;
        }

        // Frozen Heart
        if (player.hasArtefact(Artefact.Artefacts.FROZEN_HEART) && player.getHealthPercentage() >= 0.75) {
            System.out.println("Your Frozen Heart has activated.");
            finalDamage += 5;
        }

        // Venomous Fang
        if (player.hasArtefact(Artefact.Artefacts.VENOMOUS_FANG) && random.nextDouble() < 0.1) {
            System.out.println("Venomous Fang has poisoned the enemy.");
            applyEffect(StatusEffect.Effect.POISON, 3);
        }

        // Sun Pendant
        if (player.hasArtefact(Artefact.Artefacts.SUN_PENDANT) && random.nextDouble() < 0.1) {
            System.out.println("Sun Pendant has burned the enemy.");
            applyEffect(StatusEffect.Effect.BURN, 3);
        }

        this.health -= finalDamage;
        return finalDamage;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public int getAttack() {
        return this.strength;
    }

    public String getName() {
        return this.name;
    }

    private String generateName() {
        Random r = new Random();
        int roll = r.nextInt(10);
        switch (roll) {
            case 0 -> {
                return "Wolf";
            }
            case 1 -> {
                return "Goblin";
            }
            case 2 -> {
                return "Skeleton";
            }
            case 3 -> {
                return "Spider";
            }
            case 4 -> {
                return "Slime";
            }
            case 5 -> {
                return "Lizardman";
            }
            case 6 -> {
                return "Sandworm";
            }
            case 7 -> {
                return "Rock Golem";
            }
            case 8 -> {
                return "Ice Spirit";
            }
            case 9 -> {
                return "Yeti";
            }
            default -> {
                return "Creature";
            }
        }
    }
    
    private String generateBossName() {
        Random r = new Random();
        int roll = r.nextInt(5);
        switch (roll) {
            case 0 -> {
                return "Strixie, The Spectral Huntress";
            }
            case 1 -> {
                return "Adamas, The Iron Pulse Shard-Titan";
            }
            case 2 -> {
                return "Tokui, The Draconic Gastropod";
}
            case 3 -> {
                return "Hikki, The Solar-Humped Sentinel";
            }
            case 4 -> {
                return "Neptunia, The Rime-Scaled Empress";
            }
            default -> {
                return "Boss, A Creature";
            }
        }
    }
}