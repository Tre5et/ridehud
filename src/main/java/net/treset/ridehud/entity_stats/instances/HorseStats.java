package net.treset.ridehud.entity_stats.instances;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.components.HealthStatsComponent;
import net.treset.ridehud.entity_stats.components.JumpStatsComponent;
import net.treset.ridehud.entity_stats.components.SpeedStatsComponent;

import java.util.Map;

public class HorseStats extends VehicleStats {
    public HorseStats(AbstractHorseEntity entity) {
        super(
                Map.ofEntries(
                        new SpeedStatsComponent(entity, 4.86, 14.57).asMapEntry(),
                        new JumpStatsComponent(entity, 1.086, 5.293).asMapEntry(),
                        new HealthStatsComponent(entity, 15, 30).asMapEntry()
                )
        );
    }
}
