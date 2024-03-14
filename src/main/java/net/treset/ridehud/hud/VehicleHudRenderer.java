package net.treset.ridehud.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.treset.ridehud.RideChecker;
import net.treset.ridehud.RideHudMod;
import net.treset.ridehud.hud.vehicle_huds.*;

public class VehicleHudRenderer {
    public static Identifier HEART_VEHICLE_UNAVAILABLE = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/heart/vehicle_unavailable.png");
    public static Identifier HEART_CONTAINER = new Identifier("textures/gui/sprites/hud/heart/container.png");
    public static Identifier HEART_VEHICLE_FULL = new Identifier("textures/gui/sprites/hud/heart/vehicle_full.png");
    public static Identifier JUMP_ABILITY_BAR_BACKGROUND = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/jump_ability_bar_background.png");
    public static Identifier JUMP_ABILITY_BAR_PROGRESS = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/jump_ability_bar_progress.png");
    public static Identifier JUMP_ABILITY_ICON = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/jump_ability_icon.png");
    public static Identifier SPEED_ABILITY_BAR_BACKGROUND = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/speed_ability_bar_background.png");
    public static Identifier SPEED_ABILITY_BAR_PROGRESS = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/speed_ability_bar_progress.png");
    public static Identifier SPEED_ABILITY_ICON = new Identifier(RideHudMod.MOD_ID, "textures/gui/sprites/hud/speed_ability_icon.png");

    public static VehicleHud hud = null;

    public static int displayMode = 0;
    public static boolean displayTexts = false;
    public static int barOffset = 0;
    public static int heartOffset = 0;

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

    public static void render(DrawContext ctx) {
        if(hud == null) return;

        if(hud.hasHealth) {
            drawHearts(ctx);
        }
        if(hud.hasSpeed) {
            drawSpeedbar(ctx);
        }
        if(hud.hasJump) {
            drawJumpbar(ctx);
        }

        requestUpdates();
    }

    public static void drawHearts(DrawContext ctx) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for(int i = hud.stats.healthHearts - hud.stats.healthMin / 2; i < HEART_POSITIONS.length; i++) {
            int[] pos = getBottomCenterCoord(HEART_POSITIONS[i][0], HEART_POSITIONS[i][1] + heartOffset);

            int heartOverlapFix = 1;
            if(i == 3) heartOverlapFix = 0;

            //render half hearts
            if (hud.stats.health % 2 != 0 && i == hud.stats.healthHearts - (hud.stats.healthMin / 2)) {
                updateCurrentHealth();
                ctx.drawTexture(HEART_VEHICLE_UNAVAILABLE, pos[0], pos[1], 0, 0, 4, 9, 9, 9);
                ctx.drawTexture(HEART_CONTAINER, pos[0] + 4, pos[1], 4, 0, 5 - heartOverlapFix, 9, 9, 9);
                if (hud.stats.healthCurrent == hud.stats.health) {
                    ctx.drawTexture(HEART_VEHICLE_FULL, pos[0] + 4, pos[1], 4, 0, 5 - heartOverlapFix, 9, 9, 9);
                }

            } else {
                //render unavailable hearts
                ctx.drawTexture(HEART_VEHICLE_UNAVAILABLE, pos[0], pos[1], 0, 0, 9 - heartOverlapFix, 9, 9, 9);
            }
        }

        RenderSystem.disableBlend();

        if(displayTexts) {
            //render text
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = assembleText(hud.stats.health, hud.stats.healthMax, "", hud.stats.healthScore);
            int textWidth = textRenderer.getWidth(str);
            int[] textPos = getBottomCenterCoord(50 - textWidth, 49 + heartOffset);
            ctx.drawTextWithShadow(textRenderer, Text.of(str), textPos[0], textPos[1], 0xffffff);
        }
    }

    public static void drawSpeedbar(DrawContext ctx) {
        int[] pos = getBottomCenterCoord(0, 55 + barOffset);

        ctx.drawTexture(SPEED_ABILITY_BAR_BACKGROUND, pos[0], pos[1], 0, 0, 91, 5, 91, 5);

        int overlayWidth;
        if(displayMode == 1) {
            overlayWidth = Math.round(91f * (float)(hud.stats.speedCurrent - hud.stats.speedMin) / (float)(hud.stats.speedMax - hud.stats.speedMin));
        } else {
            overlayWidth = Math.round(91f * hud.stats.speedScore / 100f);
        }
        ctx.drawTexture(SPEED_ABILITY_BAR_PROGRESS, pos[0], pos[1], 0, 0, overlayWidth, 5, 91, 5);

        //render icon
        int[] icoPos = getBottomCenterCoord(91, 64 + barOffset);
        ctx.drawTexture(SPEED_ABILITY_ICON, icoPos[0], icoPos[1], 0, 0, 18, 18, 18, 18);

        //render text
        if(displayTexts) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = assembleText(hud.stats.speed, hud.stats.speedMax , I18n.translate("ridehud.unit.blocks_per_second"), hud.stats.speedScore);
            int textWidth = textRenderer.getWidth(str);
            int[] textPos = getBottomCenterCoord(91 - textWidth, 64 + barOffset);
            ctx.drawTextWithShadow(textRenderer, Text.of(str), textPos[0], textPos[1], 0xffffff);
        }
    }

    public static void drawJumpbar(DrawContext ctx) {
        int[] pos = getBottomCenterCoord(-91, 55 + barOffset);

        ctx.drawTexture(JUMP_ABILITY_BAR_BACKGROUND, pos[0], pos[1], 0, 0, 91, 5, 91, 5);

        int overlayWidth;
        if(displayMode == 1) {
            overlayWidth = Math.round(91f * (float)(hud.stats.jumpCurrent - hud.stats.jumpHeightMin) / (float)(hud.stats.jumpHeightMax - hud.stats.jumpHeightMin));
        } else {
            overlayWidth = Math.round(91f * hud.stats.jumpScore / 100f);
        }
        ctx.drawTexture(JUMP_ABILITY_BAR_PROGRESS, pos[0], pos[1], 0, 0, overlayWidth, 5, 91, 5);

        //render icon
        int[] icoPos = getBottomCenterCoord(-109, 64 + barOffset);
        ctx.drawTexture(JUMP_ABILITY_ICON, icoPos[0], icoPos[1], 0, 0, 18, 18, 18, 18);

        //render text
        if(displayTexts) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = assembleText(hud.stats.jumpHeight, hud.stats.jumpHeightMax, I18n.translate("ridehud.unit.blocks"), hud.stats.jumpScore);
            int[] textPos = getBottomCenterCoord(-91, 64 + barOffset);
            ctx.drawTextWithShadow(textRenderer, Text.of(str), textPos[0], textPos[1], 0xffffff);
        }
    }

    public static void requestUpdates() {
        if(displayMode == 1 != RideChecker.getUpdateReq()) RideChecker.requestUpdate = displayMode == 1;
    }

    public static int[] getBottomCenterCoord(int x, int y) {
        MinecraftClient cli = MinecraftClient.getInstance();
        if(cli == null) return new int[]{0,0};
        int windowWidth = cli.getWindow().getScaledWidth();
        int windowHeight = cli.getWindow().getScaledHeight();

        int newX = windowWidth / 2 + x;
        int newY = windowHeight - y;

        return new int[]{newX, newY};
    }

    public static String assembleText(double value, double max, String unit, int score) {

        return String.format("%s/%s%s: %s%s",
                (roundToDecimalPlace((float)value) % 1 == 0) ? String.format("%.0f", value) : roundToDecimalPlace((float)value),
                (roundToDecimalPlace((float)max) % 1 == 0) ? String.format("%.0f", max) : roundToDecimalPlace((float)max),
                unit, score, "%");
    }

    public static float roundToDecimalPlace(float value) {
        return (float)Math.round(value * 10) / 10;
    }

    public static void updateCurrentHealth() {
        if(hud instanceof HorseHud) {
            ((HorseHud)hud).horseStats.updateCurrentHealth();
        } else if(hud instanceof DonkeyHud) {
            ((DonkeyHud)hud).donkeyStats.updateCurrentHealth();
        } else if(hud instanceof MuleHud) {
            ((MuleHud)hud).muleStats.updateCurrentHealth();
        } else if(hud instanceof LlamaHud) {
            ((LlamaHud)hud).llamaStats.updateCurrentHealth();
        }
    }

    public static void setDisplayTexts(boolean active) {
        displayTexts = active;
    }
    public static void setDisplayMode(int index) {
        displayMode = index;
    }
    public static void setBarOffset(int offset) { barOffset = offset; }
    public static void setHeartOffset(int offset) { heartOffset = offset; }
}
