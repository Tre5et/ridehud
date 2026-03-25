package net.treset.ridehud.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;

public interface VehicleStatsRenderer {
    void render(GuiGraphicsExtractor ctx, VehicleStatsComponent stats);

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
        Minecraft client = Minecraft.getInstance();
        int windowWidth = client.getWindow().getGuiScaledWidth();
        int windowHeight = client.getWindow().getGuiScaledHeight();

        int newX = windowWidth / 2 + x;
        int newY = windowHeight - y;

        return new int[]{newX, newY};
    }
}
