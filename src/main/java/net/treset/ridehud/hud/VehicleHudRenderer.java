package net.treset.ridehud.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.treset.ridehud.RideChecker;
import net.treset.ridehud.RideHudClient;
import net.treset.ridehud.RideHudMod;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.config.lists.DisplayMode;
import net.treset.ridehud.hud.vehicle_huds.DonkeyHud;
import net.treset.ridehud.hud.vehicle_huds.HorseHud;
import net.treset.ridehud.hud.vehicle_huds.MuleHud;
import net.treset.ridehud.hud.vehicle_huds.VehicleHud;

public class VehicleHudRenderer {
    public static VehicleHud hud = null;

    public static DisplayMode displayMode = DisplayMode.FULL;
    public static boolean displayTexts = false;
    public static int barOffset = 0;
    public static DisplayMode prevDisplayMode = displayMode;
    public static boolean prevDisplayTexts = displayTexts;
    public static int prevBarOffset = barOffset;

    public static boolean speedAccurate = false;

    private static MinecraftClient cli;

    private static final Identifier SPRITESHEET = new Identifier(RideHudMod.MOD_ID, "textures/hud/spritesheet.png");
    private static final int[] texSize = new int[]{91, 38};

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

    public static void render(MatrixStack matrices) {
        if(hud == null) return;

        if(hud.hasHealth) {
            drawHearts(matrices);
        }
        if(hud.hasSpeed) {
            drawSpeedbar(matrices);
        }
        if(hud.hasJump) {
            drawJumpbar(matrices);
        }
    }

    public static void drawHearts(MatrixStack matrices) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SPRITESHEET);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for(int i = hud.stats.healthHearts - (int)(hud.stats.healthMin / 2); i < HEART_POSITIONS.length; i++) {
            int[] pos = getBottomCentereCoord(HEART_POSITIONS[i][0], HEART_POSITIONS[i][1]);

            int heartOverlapFix = 1;
            if(i == 3) heartOverlapFix = 0;

            //render half hearts
            if (hud.stats.health % 2 != 0 && i == hud.stats.healthHearts - (hud.stats.healthMin / 2)) {
                updateCurrentHealth();
                if (hud.stats.healthCurrent == hud.stats.health) {
                    InGameHud.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.HEART_FULL.area.u, TextureCoordinate.HEART_FULL.area.v, TextureCoordinate.HEART_FULL.area.xSize - heartOverlapFix, TextureCoordinate.HEART_FULL.area.ySize, texSize[0], texSize[1]);
                } else {
                    InGameHud.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.HEART_EMPTY.area.u, TextureCoordinate.HEART_EMPTY.area.v, TextureCoordinate.HEART_EMPTY.area.xSize - heartOverlapFix, TextureCoordinate.HEART_EMPTY.area.ySize, texSize[0], texSize[1]);
                }

                //render unavailable hearts
            } else {
                InGameHud.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.HEART_UNAVAILABLE.area.u, TextureCoordinate.HEART_UNAVAILABLE.area.v, TextureCoordinate.HEART_UNAVAILABLE.area.xSize - heartOverlapFix, TextureCoordinate.HEART_UNAVAILABLE.area.ySize, texSize[0], texSize[1]);
            }
        }

        RenderSystem.disableBlend();

        //render text
        if(displayTexts) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = assembleText(hud.stats.health, hud.stats.healthMax, "", hud.stats.healthScore);
            int textWidth = textRenderer.getWidth(str);
            int[] textPos = getBottomCentereCoord(50 - textWidth, 49);
            DrawableHelper.drawTextWithShadow(matrices, textRenderer, Text.of(str), textPos[0], textPos[1], 0xffffff);
        }
    }

    public static void drawSpeedbar(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, SPRITESHEET);
        int[] pos = getBottomCentereCoord(0, 55 + barOffset);

        DrawableHelper.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.SPEED_BAR_EMPTY.area.u, TextureCoordinate.SPEED_BAR_EMPTY.area.v, TextureCoordinate.SPEED_BAR_EMPTY.area.xSize, TextureCoordinate.SPEED_BAR_EMPTY.area.ySize, texSize[0], texSize[1]);

        int overlayWidth = 0;
        if(displayMode == DisplayMode.DEOP) {
            overlayWidth = Math.round((float)TextureCoordinate.SPEED_BAR_FULL.area.xSize * (float)(hud.stats.speedCurrent - hud.stats.speedMin) / (float)(hud.stats.speedMax - hud.stats.speedMin));
        } else {
            overlayWidth = Math.round((float)TextureCoordinate.SPEED_BAR_FULL.area.xSize * (float)hud.stats.speedScore / 100);
        }
        DrawableHelper.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.SPEED_BAR_FULL.area.u, TextureCoordinate.SPEED_BAR_FULL.area.v, overlayWidth, TextureCoordinate.SPEED_BAR_FULL.area.ySize, texSize[0], texSize[1]);

        //render icon
        int[] icoPos = getBottomCentereCoord(91, 64 + barOffset);
        DrawableHelper.drawTexture(matrices, icoPos[0], icoPos[1], TextureCoordinate.SPEED_BAR_ICON.area.u, TextureCoordinate.SPEED_BAR_ICON.area.v, TextureCoordinate.SPEED_BAR_ICON.area.xSize, TextureCoordinate.SPEED_BAR_ICON.area.ySize, texSize[0], texSize[1]);

        //render text
        if(displayTexts) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = assembleText(hud.stats.speed, hud.stats.speedMax , I18n.translate("ridehud.unit.blocks_per_second"), hud.stats.speedScore);
            if(!speedAccurate) {
                str = "??/" +
                        ((roundToDecimalPlace((float)hud.stats.speedMax) % 1 == 0) ? String.format("%.0f", (float)hud.stats.speedMax) : roundToDecimalPlace((float)hud.stats.speedMax)) +
                        I18n.translate("ridehud.unit.blocks_per_second") + ": ??%";
            }
            int textWidth = textRenderer.getWidth(str);
            int[] textPos = getBottomCentereCoord(91 - textWidth, 64 + barOffset);
            DrawableHelper.drawTextWithShadow(matrices, textRenderer, Text.of(str), textPos[0], textPos[1], 0xffffff);
        }
    }

    public static void drawJumpbar(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, SPRITESHEET);
        int[] pos = getBottomCentereCoord(-91, 55 + barOffset);

        DrawableHelper.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.JUMP_BAR_EMPTY.area.u, TextureCoordinate.JUMP_BAR_EMPTY.area.v, TextureCoordinate.JUMP_BAR_EMPTY.area.xSize, TextureCoordinate.JUMP_BAR_EMPTY.area.ySize, texSize[0], texSize[1]);

        int overlayWidth = 0;
        if(displayMode == DisplayMode.DEOP) {
            overlayWidth = Math.round((float)TextureCoordinate.JUMP_BAR_FULL.area.xSize * (float)(hud.stats.jumpCurrent - hud.stats.jumpHeightMin) / (float)(hud.stats.jumpHeightMax - hud.stats.jumpHeightMin));
        } else {
            overlayWidth = Math.round((float)TextureCoordinate.JUMP_BAR_FULL.area.xSize * (float)hud.stats.jumpScore / 100);
        }
        DrawableHelper.drawTexture(matrices, pos[0], pos[1], TextureCoordinate.JUMP_BAR_FULL.area.u, TextureCoordinate.JUMP_BAR_FULL.area.v, overlayWidth, TextureCoordinate.JUMP_BAR_FULL.area.ySize, texSize[0], texSize[1]);

        //render icon
        int[] icoPos = getBottomCentereCoord(-109, 64 + barOffset);
        DrawableHelper.drawTexture(matrices, icoPos[0], icoPos[1], TextureCoordinate.JUMP_BAR_ICON.area.u, TextureCoordinate.JUMP_BAR_ICON.area.v, TextureCoordinate.JUMP_BAR_ICON.area.xSize, TextureCoordinate.JUMP_BAR_ICON.area.ySize, texSize[0], texSize[1]);

        //render text
        if(displayTexts) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = assembleText(hud.stats.jumpHeight, hud.stats.jumpHeightMax, I18n.translate("ridehud.unit.blocks"), hud.stats.jumpScore);
            int[] textPos = getBottomCentereCoord(-91, 64 + barOffset);
            DrawableHelper.drawTextWithShadow(matrices, textRenderer, Text.of(str), textPos[0], textPos[1], 0xffffff);
        }
    }

    public static int[] getBottomCentereCoord(int x, int y) {
        MinecraftClient cli = MinecraftClient.getInstance();
        if(cli == null) return new int[]{0,0};
        int windowWidth = cli.getWindow().getScaledWidth();
        int windowHeight = cli.getWindow().getScaledHeight();

        int newX = windowWidth / 2 + x;
        int newY = windowHeight - y;

        return new int[]{newX, newY};
    }

    public static String assembleText(double value, double max, String unit, int score) {

        String newText = String.format("%s/%s%s: %s%s",
                (roundToDecimalPlace((float)value) % 1 == 0) ? String.format("%.0f", value) : roundToDecimalPlace((float)value),
                (roundToDecimalPlace((float)max) % 1 == 0) ? String.format("%.0f", max) : roundToDecimalPlace((float)max),
                unit, score, "%");
        return newText;
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
        }
    }

    public static void updateSpeed() {
        if(hud instanceof HorseHud) {
            ((HorseHud)hud).horseStats.updateSpeed();
        } else if(hud instanceof DonkeyHud) {
            ((DonkeyHud)hud).donkeyStats.updateSpeed();
        } else if(hud instanceof MuleHud) {
            ((MuleHud)hud).muleStats.updateSpeed();
        }
    }

    public static void checkDisplayChange(boolean force) {
        if(cli == null) {
            cli = MinecraftClient.getInstance();
            return;
        }

        if(cli.currentScreen == RideHudClient.configScreen || force) {
            if(Config.General.DISPLAY_TEXT.getBooleanValue() != prevDisplayTexts) {
                setDisplayTexts(Config.General.DISPLAY_TEXT.getBooleanValue());
                prevDisplayTexts = Config.General.DISPLAY_TEXT.getBooleanValue();
            }
            if(displayMode != prevDisplayMode) {
                prevDisplayMode = displayMode;
                RideChecker.requestUpdate = displayMode == DisplayMode.DEOP;
            }
            if(Config.General.BAR_OFFSET.getIntegerValue() != prevBarOffset) {
                barOffset = prevBarOffset = Config.General.BAR_OFFSET.getIntegerValue();
            }
        }
    }

    public static void setDisplayTexts(boolean active) {
        displayTexts = active;
    }

    public static void setDisplayMode(DisplayMode mode) {
        displayMode = mode;
    }
}
