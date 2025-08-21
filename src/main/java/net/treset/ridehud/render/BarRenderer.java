package net.treset.ridehud.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.treset.ridehud.VehicleHudRenderer;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStatsComponent;

public abstract class BarRenderer implements VehicleStatsRenderer {
    private final int xBarOffset;
    private final int yBarOffset;
    private final int xIconOffset;
    private final int yIconOffset;
    private final boolean textRightAligned;
    private final Identifier barBackground;
    private final Identifier barProgress;
    private final Identifier icon;

    public BarRenderer(int xBarOffset, int yBarOffset, int xIconOffset, int yIconOffset, boolean textRightAligned, Identifier barBackground, Identifier barProgress, Identifier icon) {
        this.xBarOffset = xBarOffset;
        this.yBarOffset = yBarOffset;
        this.xIconOffset = xIconOffset;
        this.yIconOffset = yIconOffset;
        this.textRightAligned = textRightAligned;
        this.barBackground = barBackground;
        this.barProgress = barProgress;
        this.icon = icon;
    }

    @Override
    public void render(DrawContext ctx, VehicleStatsComponent stats) {
        int totalOffset = Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();

        int[] pos = VehicleStatsRenderer.getBottomCenterCoord(xBarOffset, yBarOffset + totalOffset);

        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, barBackground, pos[0], pos[1], 0, 0, 91, 5, 91, 5);

        int overlayWidth = Math.round(91f * stats.getScore(Config.displayMode.getOptionIndex() == 1) / 100f);
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, barProgress, pos[0], pos[1], 0, 0, overlayWidth, 5, 91, 5);

        //render icon
        int[] icoPos = VehicleStatsRenderer.getBottomCenterCoord(xIconOffset, yIconOffset + totalOffset);
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, icon, icoPos[0], icoPos[1], 0, 0, 18, 18, 18, 18);

        //render text
        if(Config.displayText.getBoolean()) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if(textRenderer == null) return;

            String str = VehicleStatsRenderer.assembleText(stats.getValue(false), stats.getMax(), I18n.translate("ridehud.unit.blocks_per_second"), stats.getScore(false));
            int[] textPos = VehicleStatsRenderer.getBottomCenterCoord(xBarOffset + (textRightAligned ? 91 - textRenderer.getWidth(str) : 0), yBarOffset + 9 + totalOffset);
            ctx.drawTextWithShadow(textRenderer, Text.of(str), textPos[0], textPos[1], ColorHelper.fromAbgr(0xFFFFFFFF));
        }
    }
}
