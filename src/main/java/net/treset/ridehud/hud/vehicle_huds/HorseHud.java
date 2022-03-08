package net.treset.ridehud.hud.vehicle_huds;

import net.minecraft.entity.passive.HorseEntity;
import net.treset.ridehud.entity_stats.HorseStats;

public class HorseHud extends VehicleHud {
    public HorseStats horseStats = null;

    public HorseHud(HorseEntity horse) {
        this.stats = new HorseStats(horse);
        this.horseStats = (HorseStats)this.stats;
        this.hasHealth = true;
        this.hasJump = true;
        this.hasSpeed = true;
    }
}
