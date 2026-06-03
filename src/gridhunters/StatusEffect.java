/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gridhunters;

import java.io.Serializable;

/**
 *
 * @author LD Mao
 */
public class StatusEffect implements Serializable {

    public enum Effect {
        POISON, BURN
    }

    Effect effect;
    int duration;

    StatusEffect(Effect effect, int duration) {
        this.effect = effect;
        this.duration = duration;
    }

    public String activateEffect(Creature creature) {
        String logMessage = "";
        String displayName = creature.getName();

        switch (effect) {
            case POISON -> {
                creature.health -= 1;
                logMessage = displayName + " took 1 poison damage.";
            }
            case BURN -> {
                creature.health -= 1;
                logMessage = displayName + " took 1 burn damage.";
            }
        }
        if (creature.health <= 0) {
            creature.health = 0;
        }
        duration--;
        return logMessage;
    }

    public int getDuration() {
        return duration;
    }
}
