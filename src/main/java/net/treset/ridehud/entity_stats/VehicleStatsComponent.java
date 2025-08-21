package net.treset.ridehud.entity_stats;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.treset.ridehud.render.VehicleStatsRenderer;

import java.util.Map;

public abstract class VehicleStatsComponent {
    private final EntityAttributeInstance attribute;
    private final double max;
    private final double min;
    private final VehicleStatsRenderer renderer;
    private double current = 0;

    public VehicleStatsComponent(
            EntityAttributeInstance attribute,
            double min,
            double max,
            VehicleStatsRenderer renderer
    ) {
        this.attribute = attribute;
        this.min = min;
        this.max = max;
        this.renderer = renderer;
    }

    public void render(DrawContext ctx) {
        renderer.render(ctx, this);
    }

    public double getValue(boolean current) {
        if(current) {
            return getCurrent();
        } else {
            if (attribute == null) {
                return -1;
            }
            return calculateValue(attribute.getValue());
        }
    }

    public int getScore(boolean current) {
        return (int)Math.round((getValue(current) - min) / (max - min) * 100);
    }

    public double getCurrent() {
        return current;
    }

    protected void setCurrent(double value) {
        this.current = value;
    }

    public void updateCurrent() {
        setCurrent(getUpdatedCurrent());
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public abstract Map.Entry<VehicleStatsType, VehicleStatsComponent> asMapEntry();

    protected abstract double calculateValue(double raw);

    protected abstract double getUpdatedCurrent();
}
