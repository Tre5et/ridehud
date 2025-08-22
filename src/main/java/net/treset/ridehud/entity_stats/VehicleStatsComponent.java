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

    private double general = 0;
    private double generalScore = 0;
    private double current = 0;
    private double currentScore = 0;


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
            return getGeneral();
        }
    }

    public double getScore(boolean current) {
        if(current) {
            return getCurrentScore();
        } else {
            return getGeneralScore();
        }
    }

    public abstract void update();

    public double getGeneral() {
        return general;
    }

    protected void setGeneral(double general) {
        this.general = general;
        generalScore = calculateScore(general);
    }

    public void updateGeneral() {
        if (attribute == null) {
            return;
        }
        setGeneral(calculateValue(attribute.getValue()));
    }

    public double getGeneralScore() {
        return generalScore;
    }

    public double getCurrent() {
        return current;
    }

    protected void setCurrent(double value) {
        this.current = value;
        currentScore = calculateScore(value);
    }

    public void updateCurrent() {
        setCurrent(getUpdatedCurrent());
    }

    public double getCurrentScore() {
        return currentScore;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    private double calculateScore(double value) {
        return Math.max(0, Math.min(1, (value - min) / (max - min)));
    }

    public abstract Map.Entry<VehicleStatsType, VehicleStatsComponent> asMapEntry();

    protected abstract double calculateValue(double raw);

    protected abstract double getUpdatedCurrent();
}
