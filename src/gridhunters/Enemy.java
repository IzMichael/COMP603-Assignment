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

    public String executeEAttack(Player player) {
        Random random = new Random();
        StringBuilder logText = new StringBuilder();

        // Sandstorm Totem
        if (player.hasArtefact(Artefact.Artefacts.SANDSTORM_TOTEM) && random.nextDouble() < 0.05) {
            logText.append("The Sandstorm totem has protected you! You 0 hp has been taken from you.");
            return logText.toString();
        }

        // Glacial Aegis
        int currentDefence = player.getDefence();
        if (player.hasArtefact(Artefact.Artefacts.GLACIAL_AEGIS) && player.getHealthPercentage() <= 0.25) {
            currentDefence += 5;
            logText.append("Glacial Aegis triggered! Defense boosted by +5.\n");
        }

        double enemyAttackMultiplier = random.nextDouble(0, 2); 
        int calculatedStrength = (int) Math.round(this.strength * enemyAttackMultiplier);
        
        int chance = random.nextInt(0, 2);
        int finalDamage = Math.max(0, (calculatedStrength - currentDefence)) * chance;
        
        player.setHealth(player.getHealth() - finalDamage);
        logText.append(this.name).append(" attacks you for ").append(finalDamage).append(" damage.");

        // Echoing Shard
        if (player.hasArtefact(Artefact.Artefacts.ECHOING_SHARD) && finalDamage > 0 && random.nextDouble() < 0.15) {
            int reflected = (int) Math.ceil((double) finalDamage / 2);
            this.takeDamage(reflected);
            logText.append("\nEchoing Shard reflected ").append(reflected).append(" damage back!");
        }

        // Slime Friend
        if (player.hasArtefact(Artefact.Artefacts.SLIME_FRIEND) && finalDamage > 0 && random.nextDouble() < 0.15) {
            int reflected = (int) Math.ceil((double) finalDamage / 2);
            this.takeDamage(reflected);
            logText.append("\nGoopy (your slime friend) attacked and did ").append(reflected).append(" damage!");
        }

        return logText.toString();
    }
    
    public String executePAttack(int playerStrength, Player player) {
        Random random = new Random();
        StringBuilder logText = new StringBuilder();
        
        double multiplier = random.nextDouble(0, 2);
        int finalDamage = (int) Math.round(playerStrength * multiplier);

        // 1. Spirit Staff
        if (player.hasArtefact(Artefact.Artefacts.SPIRIT_STAFF) && random.nextDouble() < 0.15) {
            logText.append("Your Spirit Staff has summoned a spirit to assist in your attack.\n");
            finalDamage += finalDamage * 1.15;
        }

        // 2. Frozen Heart
        if (player.hasArtefact(Artefact.Artefacts.FROZEN_HEART) && player.getHealthPercentage() >= 0.75) {
            logText.append("Your Frozen Heart has activated.\n");
            finalDamage += 5;
        }

        // 3. Venomous Fang
        if (player.hasArtefact(Artefact.Artefacts.VENOMOUS_FANG) && random.nextDouble() < 0.1) {
            logText.append("Venomous Fang has poisoned the enemy.\n");
            this.applyEffect(StatusEffect.Effect.POISON, 3);
        }

        // 4. Sun Pendant
        if (player.hasArtefact(Artefact.Artefacts.SUN_PENDANT) && random.nextDouble() < 0.1) {
            logText.append("Sun Pendant has burned the enemy.\n");
            this.applyEffect(StatusEffect.Effect.BURN, 3);
        }

        this.health -= finalDamage;
        if (this.health < 0) this.health = 0;

        if (finalDamage < 1) {
                logText.append("Your attack was ineffective! No damage was dealt to the " + this.getName() + ".");
            } else {
                logText.append("You struck the enemy for " + finalDamage + " damage.");
            }
        return logText.toString();
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
    
    @Override
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
                return "Ruruna, The Sonic-Winged Queen";
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