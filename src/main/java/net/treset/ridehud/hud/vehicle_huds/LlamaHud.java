package net.treset.ridehud.hud.vehicle_huds;

import net.minecraft.entity.passive.LlamaEntity;
import net.treset.ridehud.entity_stats.LlamaStats;

public class LlamaHud extends VehicleHud {
    public LlamaStats llamaStats = null;

    public LlamaHud(LlamaEntity llama) {
        this.stats = new LlamaStats(llama);
        this.llamaStats = (LlamaStats)this.stats;
        this.hasHealth = true;

    }
}
