package net.treset.ridehud.entity_stats.instances;

import net.minecraft.world.entity.animal.equine.Llama;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.components.HealthStatsComponent;

import java.util.Map;

public class LlamaStats extends VehicleStats {
    public LlamaStats(Llama entity) {
        super(
                Map.ofEntries(new HealthStatsComponent(entity, 15, 30).asMapEntry())
        );
    }
}
