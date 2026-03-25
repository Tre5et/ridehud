package net.treset.ridehud.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;

public class HealthStatsRenderer implements VehicleStatsRenderer {
    private static final Identifier HEART_VEHICLE_CONTAINER = Identifier.fromNamespaceAndPath("minecraft", "hud/heart/vehicle_container");
    private static final Identifier HEART_VEHICLE_HALF = Identifier.fromNamespaceAndPath("minecraft", "hud/heart/vehicle_half");
    private static final Identifier HEART_VEHICLE_UNAVAILABLE = Identifier.fromNamespaceAndPath("ridehud", "hud/heart/vehicle_unavailable");
    private static final int[][] HEART_POSITIONS = new int[][] {
            new int[] {26, 39},
            new int[] {18, 39},
            new int[] {10, 39},
            new int[] {82, 49},
            new int[] {74, 49},
            new int[] {66, 49},
            new int[] {58, 49},
            new int[] {50, 49}
    };

    @Override
    public void render(GuiGraphicsExtractor ctx, VehicleStatsComponent stats) {
        int maxHealth = (int)stats.getValue(false);
        int maxHearts = maxHealth / 2;
        for(int i = maxHearts - (int)stats.getMin() / 2; i < HEART_POSITIONS.length; i++) {
            if(i < 0) continue;
            int[] pos = VehicleStatsRenderer.getBottomCenterCoord(HEART_POSITIONS[i][0], HEART_POSITIONS[i][1] + Config.heartOffset.getInteger());

            int heartOverlapFix = 1;
            if(i == 3) heartOverlapFix = 0;

            //render half hearts
            if (maxHealth % 2 != 0 && i == maxHearts - (int)(stats.getMin() / 2)) {
                ctx.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_UNAVAILABLE, 9, 9, 0, 0, pos[0], pos[1],4, 9);
                ctx.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_CONTAINER, 9, 9, 4, 0,pos[0] + 4, pos[1], 5 - heartOverlapFix, 9);
                if (stats.getValue(true) == maxHealth) {
                    ctx.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_HALF, 9, 9, 4, 0, pos[0] + 4, pos[1], 5 - heartOverlapFix, 9);
                }

            } else {
                //render unavailable hearts
                ctx.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_UNAVAILABLE, 9, 9, 0, 0, pos[0], pos[1], 9 - heartOverlapFix, 9);
            }
        }

        if(Config.displayText.getBoolean()) {
            //render text
            Font font = Minecraft.getInstance().font;

            String str = VehicleStatsRenderer.assembleText(maxHealth, stats.getMax(), "", stats.getScore(false));
            int textWidth = font.width(str);
            int[] textPos = VehicleStatsRenderer.getBottomCenterCoord(50 - textWidth, 49 + Config.heartOffset.getInteger());
            ctx.text(font, Component.literal(str), textPos[0], textPos[1], 0xFFFFFFFF);
        }
    }
}
