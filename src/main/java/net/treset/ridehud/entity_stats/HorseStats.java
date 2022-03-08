package net.treset.ridehud.entity_stats;

import net.minecraft.entity.passive.HorseEntity;

public class HorseStats extends VehicleStats {

    public HorseEntity horse = null;

    public HorseStats(HorseEntity horse) {
        this.uuid = horse.getUuidAsString();
        this.entity = horse;
        this.horse = horse;

        this.jumpHeightMin = 1.089;
        this.jumpHeightMax = 5.293;
        this.jumpModifier = horse.getJumpStrength();
        this.jumpHeight = Math.pow(this.jumpModifier, 1.7) * 5.293;
        this.jumpScore = (int)((this.jumpHeight - this.jumpHeightMin) / (this.jumpHeightMax - this.jumpHeightMin) * 100);

        this.speedMin = 4.86;
        this.speedMax = 14.57;
        this.speedModifier = horse.getMovementSpeed();
        this.speed = this.speedModifier * 43.17;
        this.speedScore = (int)((this.speed - this.speedMin) / (this.speedMax - this.speedMin) * 100);

        this.healthMin = 15;
        this.healthMax = 30;
        this.health = (int)horse.getMaxHealth();
        this.healthHearts = this.health / 2;
        this.healthScore = (int)((float)(this.health - this.healthMin) / (float)(healthMax - this.healthMin) * 100);

        this.healthCurrent = (int)horse.getHealth();

        this.score = (this.jumpScore + this.speedScore + this.healthScore) / 3;
    }

    public void updateCurrentHealth() {
        this.healthCurrent = (int)this.horse.getHealth();
    }

    public void updateSpeed() {
        this.speedModifier = this.horse.getMovementSpeed();
        this.speed = this.speedModifier * 43.17;
        this.speedScore = (int)((this.speed - this.speedMin) / (this.speedMax - this.speedMin) * 100);
    }
}
