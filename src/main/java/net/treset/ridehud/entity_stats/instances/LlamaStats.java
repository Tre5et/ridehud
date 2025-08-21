package net.treset.ridehud.entity_stats.instances;

import net.minecraft.entity.passive.LlamaEntity;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.components.HealthStatsComponent;

import java.util.Map;

public class LlamaStats extends VehicleStats {
    public LlamaStats(LlamaEntity entity) {
        super(
                Map.ofEntries(new HealthStatsComponent(entity, 15, 30).asMapEntry())
        );
    }
}
