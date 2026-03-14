package net.treset.ridehud.entity_stats.components;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;
import net.treset.ridehud.entity_stats.VehicleStatsType;
import net.treset.ridehud.render.HealthStatsRenderer;

import java.util.Map;

public class HealthStatsComponent extends VehicleStatsComponent {
    private final LivingEntity entity;

    public HealthStatsComponent(LivingEntity entity, double min, double max) {
        super(entity.getAttribute(Attributes.MAX_HEALTH), min, max, new HealthStatsRenderer());
        this.entity = entity;
    }

    @Override
    public Map.Entry<VehicleStatsType, VehicleStatsComponent> asMapEntry() {
        return Map.entry(VehicleStatsType.HEALTH, this);
    }

    @Override
    protected double calculateValue(double raw) {
        return raw;
    }

    @Override
    protected double getUpdatedCurrent() {
        return entity.getHealth();
    }

    @Override
    public void update() {
        updateGeneral();
        if(getGeneral() % 2 == 1) {
            updateCurrent();
        }
    }
}
