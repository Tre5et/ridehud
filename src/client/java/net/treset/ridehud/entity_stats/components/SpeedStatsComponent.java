package net.treset.ridehud.entity_stats.components;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;
import net.treset.ridehud.entity_stats.VehicleStatsType;
import net.treset.ridehud.render.SpeedStatsRenderer;

import java.util.Map;

public class SpeedStatsComponent extends VehicleStatsComponent {
    private final LivingEntity entity;
    private boolean prevGroundCollision = true;
    private Vec3 prevPos = null;

    public SpeedStatsComponent(LivingEntity entity, double min, double max) {
        super(entity.getAttribute(Attributes.MOVEMENT_SPEED), min, max, new SpeedStatsRenderer());
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
        if(entity.onGround() != prevGroundCollision) {
            // Ignore first tick after starting and ending jump because speed values are funky
            prevGroundCollision = entity.onGround();
            return this.getCurrent();
        }
        Vec3 pos = entity.getPosition(0);
        if(prevPos == null) {
            prevPos = pos;
            return 0;
        }
        double distance = pos.subtract(prevPos).horizontalDistance();
        prevPos = pos;
        return distance * 20D;
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
