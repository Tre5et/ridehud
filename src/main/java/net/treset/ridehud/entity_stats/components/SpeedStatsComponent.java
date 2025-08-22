package net.treset.ridehud.entity_stats.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;
import net.treset.ridehud.entity_stats.VehicleStatsType;
import net.treset.ridehud.render.SpeedStatsRenderer;

import java.util.Map;

public class SpeedStatsComponent extends VehicleStatsComponent {
    private final LivingEntity entity;
    private boolean prevGroundCollision = true;

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
        if(entity.groundCollision != prevGroundCollision) {
            // Ignore first tick after starting and ending jump because speed values are funky
            prevGroundCollision = entity.groundCollision;
            return this.getCurrent();
        }
        return (Math.sqrt(Math.pow(entity.getX() - entity.lastX, 2) + Math.pow(entity.getZ() - entity.lastZ, 2))) * 20D;
    }

    @Override
    public void update() {
        if(Config.displayMode.getOptionIndex() == 0) {
            updateGeneral();
        } else {
            updateCurrent();
            if(Config.displayText.getBoolean()) {
                updateGeneral();
            }
        }
    }
}
