package net.treset.ridehud.entity_stats;

import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.Map;

public class VehicleStats {
    private final HashMap<VehicleStatsType, VehicleStatsComponent> statComponents = new HashMap<>();

    public VehicleStats(
            Map<VehicleStatsType, VehicleStatsComponent> statComponents
    ) {
        this.statComponents.putAll(statComponents);
    }

    public void render(DrawContext ctx) {
        for(VehicleStatsComponent statsComponent : this.statComponents.values()) {
            statsComponent.render(ctx);
        }
    }

    public boolean has(VehicleStatsType vehicleStatsType) {
        return statComponents.containsKey(vehicleStatsType);
    }

    public VehicleStatsComponent get(VehicleStatsType vehicleStatsType) {
        return statComponents.get(vehicleStatsType);
    }

    public void update() {
        for(VehicleStatsComponent statsComponent : this.statComponents.values()) {
            statsComponent.update();
        }
    }

    public double getTotalScore(boolean current) {
        int size = statComponents.size();
        if(size == 0) {
            return 0;
        }
        double score = statComponents.values().stream().mapToDouble(s -> s.getScore(current)).sum();
        return score / size;
    }

    private static VehicleStats instance;

    public static VehicleStats getInstance() {
        return instance;
    }

    public static void setInstance(VehicleStats instance) {
        VehicleStats.instance = instance;
    }

    public static boolean hasInstance() {
        return instance != null;
    }
}
