/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import java.util.ArrayList;

import java.io.Serializable;

/**
 *
 * @author Michael Martin, LD Mao
 */
public abstract class Creature implements Serializable {
    int x;
    int y;
    int maxHealth;
    public int health;
    ArrayList<StatusEffect> activeEffects = new ArrayList<>();

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public int getHealth() {
        return this.health;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public double getHealthPercentage() {
        return (double) this.health / this.maxHealth;
    }
    
    public abstract String getName();
    
    public void applyEffect(StatusEffect.Effect effect, int duration) {
        activeEffects.add(new StatusEffect(effect, duration));
    }
    
    public String processStatusEffects() {
        StringBuilder statusLogs = new StringBuilder();
        var iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next();
            String result = effect.activateEffect(this);
            if (!result.isEmpty()) {
                statusLogs.append(result).append("\n");
            }
            if (effect.getDuration() <= 0) {
                iterator.remove();
            }
        }
        return statusLogs.toString();
    }
}