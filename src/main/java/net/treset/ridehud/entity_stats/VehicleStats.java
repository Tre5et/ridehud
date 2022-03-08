package net.treset.ridehud.entity_stats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseEntity;
import net.treset.ridehud.RideHudMod;

public class VehicleStats {
    public String uuid = null;
    public Entity entity = null;
    public double jumpModifier = 0;
    public double jumpHeight = 0;
    public double jumpHeightMin = 0;
    public double jumpHeightMax = 0;
    public int jumpScore = 0;
    public double jumpCurrent = 0;
    public double speedModifier = 0;
    public double speed = 0;
    public double speedMin = 0;
    public double speedMax = 0;
    public int speedScore = 0;
    public double speedCurrent = 0;
    public int health = 0;
    public int healthHearts = 0;
    public int healthMin = 0;
    public int healthMax = 0;
    public int healthScore = 0;
    public int healthCurrent = 0;
    public int score = 0;

    private double groundHeight = 0;

    public void updateCurrentJumpHeight() {
        if(entity.isOnGround()) {
            groundHeight = entity.getY();
            this.jumpCurrent = 0;
        } else {
            this.jumpCurrent = entity.getY() - groundHeight;
        }
    }

    public void updateCurrentSpeed() {
        speedCurrent = (Math.sqrt(Math.pow(entity.getX() - entity.prevX, 2) + Math.pow(entity.getZ() - entity.prevZ, 2))) * 20D;
    }
}
