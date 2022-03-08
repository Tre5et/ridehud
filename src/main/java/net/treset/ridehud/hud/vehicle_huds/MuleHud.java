package net.treset.ridehud.hud.vehicle_huds;

import net.minecraft.entity.passive.MuleEntity;
import net.treset.ridehud.entity_stats.MuleStats;

public class MuleHud extends VehicleHud {
    public MuleStats muleStats = null;

    public MuleHud(MuleEntity mule) {
        this.stats = new MuleStats(mule);
        this.muleStats = (MuleStats)this.stats;
        this.hasHealth = true;
        this.hasJump = true;
        this.hasSpeed = true;
    }
}
