package net.treset.ridehud.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.treset.ridehud.RideHudMod;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;

public class HealthStatsRenderer implements VehicleStatsRenderer {
    private static final Identifier HEART_CONTAINER = Identifier.ofVanilla("textures/gui/sprites/hud/heart/container.png");
    private static final Identifier HEART_VEHICLE_FULL = Identifier.ofVanilla("textures/gui/sprites/hud/heart/vehicle_full.png");
    private static final Identifier HEART_VEHICLE_UNAVAILABLE = Identifier.of(RideHudMod.MOD_ID, "textures/gui/sprites/hud/heart/vehicle_unavailable.png");
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
    public void render(DrawContext ctx, VehicleStatsComponent stats) {
        int maxHealth = (int)stats.getValue(false);
        int maxHearts = maxHealth / 2;
        for(int i = maxHearts - (int)stats.getMin() / 2; i < HEART_POSITIONS.length; i++) {
            if(i < 0) continue;
            int[] pos = VehicleStatsRenderer.getBottomCenterCoord(HEART_POSITIONS[i][0], HEART_POSITIONS[i][1] + Config.heartOffset.getInteger());

            int heartOverlapFix = 1;
            if(i == 3) heartOverlapFix = 0;

            //render half hearts
            if (maxHealth % 2 != 0 && i == maxHearts - (int)(stats.getMin() / 2)) {
                stats.updateCurrent();
                ctx.drawTexture(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_UNAVAILABLE, pos[0], pos[1], 0, 0, 4, 9, 9, 9);
                ctx.drawTexture(RenderPipelines.GUI_TEXTURED, HEART_CONTAINER, pos[0] + 4, pos[1], 4, 0, 5 - heartOverlapFix, 9, 9, 9);
                if (stats.getValue(true) == maxHealth) {
                    ctx.drawTexture(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_FULL, pos[0] + 4, pos[1], 4, 0, 5 - heartOverlapFix, 9, 9, 9);
                }

            } else {
                //render unavailable hearts
                ctx.drawTexture(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_UNAVAILABLE, pos[0], pos[1], 0, 0, 9 - heartOverlapFix, 9, 9, 9);
            }
        }

        if(Config.displayText.getBoolean()) {
            //render text
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = VehicleStatsRenderer.assembleText(maxHealth, stats.getMax(), "", stats.getScore(false));
            int textWidth = textRenderer.getWidth(str);
            int[] textPos = VehicleStatsRenderer.getBottomCenterCoord(50 - textWidth, 49 + Config.heartOffset.getInteger());
            ctx.drawTextWithShadow(textRenderer, Text.of(str), textPos[0], textPos[1], ColorHelper.fromAbgr(0xFFFFFFFF));
        }
    }
}
