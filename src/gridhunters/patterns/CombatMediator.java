package gridhunters.patterns;

import gridhunters.Enemy;
import gridhunters.Player;
import gridhunters.StatusEffect;
import gridhunters.items.Artefact;
import java.util.Random;

public class CombatMediator {
    private Player player;
    private Enemy enemy;
    private GameObserver observer;
    
    public CombatMediator(Player player, Enemy enemy, GameObserver observer) {
        this.player = player;
        this.enemy = enemy;
        this.observer = observer;
    }
    
    public String executePAttack(int playerStrength, Player player) {
        Random r = new Random();
        StringBuilder logText = new StringBuilder();
        
        double multiplier = r.nextDouble(0, 2);
        int finalDamage = (int) Math.round(playerStrength * multiplier);

        // 1. Spirit Staff
        if (player.hasArtefact(Artefact.Artefacts.SPIRIT_STAFF) && r.nextDouble() < 0.15) {
            logText.append("Your Spirit Staff has summoned a spirit to assist in your attack.\n");
            finalDamage += (int) Math.round(finalDamage * 0.15);
        }

        // 2. Frozen Heart
        if (player.hasArtefact(Artefact.Artefacts.FROZEN_HEART) && player.getHealthPercentage() >= 0.75) {
            logText.append("Your Frozen Heart has activated.\n");
            finalDamage += 5;
        }

        // 3. Venomous Fang
        if (player.hasArtefact(Artefact.Artefacts.VENOMOUS_FANG) && r.nextDouble() < 0.1) {
            logText.append("Venomous Fang has poisoned the enemy.\n");
            enemy.applyEffect(StatusEffect.Effect.POISON, 3);
        }

        // 4. Sun Pendant
        if (player.hasArtefact(Artefact.Artefacts.SUN_PENDANT) && r.nextDouble() < 0.1) {
            logText.append("Sun Pendant has burned the enemy.\n");
            enemy.applyEffect(StatusEffect.Effect.BURN, 3);
        }

        enemy.health -= finalDamage;
        if (enemy.health < 0) {
            enemy.health = 0;
        }

        if (finalDamage < 1) {
                logText.append("Your attack was ineffective! No damage was dealt to the " + enemy.getName() + ".\n");
            } else {
                logText.append("You struck the enemy for " + finalDamage + " damage.\n");
            }
        return logText.toString();
    }

    public String executeEAttack(Player player) {
        Random random = new Random();
        StringBuilder logText = new StringBuilder();

        // Sandstorm Totem
        if (player.hasArtefact(Artefact.Artefacts.SANDSTORM_TOTEM) && random.nextDouble() < 0.05) {
            logText.append("The Sandstorm totem has protected you! You 0 hp has been taken from you.\n");
            return logText.toString();
        }

        // Glacial Aegis
        int currentDefence = player.getDefence();
        if (player.hasArtefact(Artefact.Artefacts.GLACIAL_AEGIS) && player.getHealthPercentage() <= 0.25) {
            currentDefence += 5;
            logText.append("Glacial Aegis triggered! Defense boosted by +5.\n");
        }

        double enemyAttackMultiplier = random.nextDouble(0, 2); 
        int calculatedStrength = (int) Math.round(enemy.getAttack() * enemyAttackMultiplier);
        
        int chance = random.nextInt(0, 2);
        int finalDamage = Math.max(0, (calculatedStrength - currentDefence)) * chance;
        
        player.setHealth(player.getHealth() - finalDamage);
        logText.append(enemy.getName()).append(" attacks you for ").append(finalDamage).append(" damage.\n");

        // Echoing Shard
        if (player.hasArtefact(Artefact.Artefacts.ECHOING_SHARD) && finalDamage > 0 && random.nextDouble() < 0.15) {
            int reflected = (int) Math.ceil((double) finalDamage / 2);
            enemy.takeDamage(reflected);
            logText.append("Echoing Shard reflected ").append(reflected).append(" damage back!\n");
        }

        // Slime Friend
        if (player.hasArtefact(Artefact.Artefacts.SLIME_FRIEND) && finalDamage > 0 && random.nextDouble() < 0.15) {
            int reflected = (int) Math.ceil((double) finalDamage / 2);
            enemy.takeDamage(reflected);
            logText.append("\nGoopy (your slime friend) attacked and did ").append(reflected).append(" damage!\n");
        }

        return logText.toString();
    }
}
