package net.treset.ridehud.entity_stats.components;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;
import net.treset.ridehud.entity_stats.VehicleStatsType;
import net.treset.ridehud.render.JumpStatsRenderer;

import java.util.Map;

public class JumpStatsComponent extends VehicleStatsComponent {
    private final LivingEntity entity;
    private double groundHeight = 0;

    public JumpStatsComponent(LivingEntity entity, double min, double max) {
        super(entity.getAttribute(Attributes.JUMP_STRENGTH), min, max, new JumpStatsRenderer());
        this.entity = entity;
    }

    @Override
    public Map.Entry<VehicleStatsType, VehicleStatsComponent> asMapEntry() {
        return Map.entry(VehicleStatsType.JUMP, this);
    }

    @Override
    protected double calculateValue(double raw) {
        return Math.pow(raw, 1.7) * 5.293;
    }

    @Override
    protected double getUpdatedCurrent() {
        if(entity.onGround()) {
            groundHeight = entity.getY();
            return 0;
        } else {
            return entity.getY() - groundHeight;
        }
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
