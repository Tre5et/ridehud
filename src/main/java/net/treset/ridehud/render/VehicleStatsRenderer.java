package net.treset.ridehud.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;

public interface VehicleStatsRenderer {
    void render(DrawContext ctx, VehicleStatsComponent stats);

    static String assembleText(double value, double max, String unit, double score) {
        return String.format("%s/%s%s: %s%s",
                (roundToDecimalPlace((float)value) % 1 == 0) ? String.format("%.0f", value) : roundToDecimalPlace((float)value),
                (roundToDecimalPlace((float)max) % 1 == 0) ? String.format("%.0f", max) : roundToDecimalPlace((float)max),
                unit,
                Math.round(score * 100d), "%");
    }

    static float roundToDecimalPlace(float value) {
        return (float)Math.round(value * 10) / 10;
    }

    static int[] getBottomCenterCoord(int x, int y) {
        MinecraftClient cli = MinecraftClient.getInstance();
        if(cli == null) return new int[]{0,0};
        int windowWidth = cli.getWindow().getScaledWidth();
        int windowHeight = cli.getWindow().getScaledHeight();

        int newX = windowWidth / 2 + x;
        int newY = windowHeight - y;

        return new int[]{newX, newY};
    }
}
