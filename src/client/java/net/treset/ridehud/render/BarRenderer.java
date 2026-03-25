package net.treset.ridehud.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
    public void render(GuiGraphicsExtractor ctx, VehicleStatsComponent stats) {
        int totalOffset = Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();

        int[] pos = VehicleStatsRenderer.getBottomCenterCoord(xBarOffset, yBarOffset + totalOffset);

        ctx.blitSprite(RenderPipelines.GUI_TEXTURED, barBackground, pos[0], pos[1], 91, 5);

        int overlayWidth = (int)Math.round(91d * stats.getScore(Config.displayMode.getOptionIndex() == 1));
        ctx.blitSprite(RenderPipelines.GUI_TEXTURED, barProgress, 91, 5, 0, 0, pos[0], pos[1], overlayWidth, 5);

        //render icon
        int[] icoPos = VehicleStatsRenderer.getBottomCenterCoord(xIconOffset, yIconOffset + totalOffset);
        ctx.blitSprite(RenderPipelines.GUI_TEXTURED, icon, icoPos[0], icoPos[1], 18, 18);

        //render text
        if(Config.displayText.getBoolean()) {
            Font font = Minecraft.getInstance().font;

            String str = VehicleStatsRenderer.assembleText(stats.getValue(false), stats.getMax(), I18n.get("ridehud.unit.blocks_per_second"), stats.getScore(false));
            int[] textPos = VehicleStatsRenderer.getBottomCenterCoord(xBarOffset + (textRightAligned ? 91 - font.width(str) : 0), yBarOffset + 9 + totalOffset);
            ctx.text(font, Component.literal(str), textPos[0], textPos[1], 0xFFFFFFFF);
        }
    }
}
