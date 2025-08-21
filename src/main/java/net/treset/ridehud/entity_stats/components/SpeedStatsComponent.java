package net.treset.ridehud.entity_stats.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;
import net.treset.ridehud.entity_stats.VehicleStatsType;
import net.treset.ridehud.render.SpeedStatsRenderer;

import java.util.Map;

public class SpeedStatsComponent extends VehicleStatsComponent {
    private final LivingEntity entity;

    public SpeedStatsComponent(LivingEntity entity, double min, double max) {
        super(entity.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED), min, max, new SpeedStatsRenderer());
        this.entity = entity;
    }

    @Override
    public Map.Entry<VehicleStatsType, VehicleStatsComponent> asMapEntry() {
        return Map.entry(VehicleStatsType.SPEED, this);
    }

    @Override
    protected double calculateValue(double raw) {
        return raw * 43.17;
    }

    @Override
    public double getUpdatedCurrent() {
        return (Math.sqrt(Math.pow(entity.getX() - entity.lastX, 2) + Math.pow(entity.getZ() - entity.lastZ, 2))) * 20D;
    }
}
