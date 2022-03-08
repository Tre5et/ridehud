package net.treset.ridehud.hud.vehicle_huds;

import net.minecraft.entity.passive.DonkeyEntity;
import net.treset.ridehud.entity_stats.DonkeyStats;

public class DonkeyHud extends VehicleHud {
    public DonkeyStats donkeyStats = null;

    public DonkeyHud(DonkeyEntity donkey) {
        this.stats = new DonkeyStats(donkey);
        this.donkeyStats = (DonkeyStats)this.stats;
        this.hasHealth = true;
        this.hasJump = true;
        this.hasSpeed = true;
    }
}
