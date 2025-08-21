package net.treset.ridehud.render;

import net.minecraft.util.Identifier;
import net.treset.ridehud.RideHudMod;

public class SpeedStatsRenderer extends BarRenderer {
    private static final Identifier SPEED_ABILITY_BAR_BACKGROUND = Identifier.of(RideHudMod.MOD_ID, "textures/gui/sprites/hud/speed_ability_bar_background.png");
    private static final Identifier SPEED_ABILITY_BAR_PROGRESS = Identifier.of(RideHudMod.MOD_ID, "textures/gui/sprites/hud/speed_ability_bar_progress.png");
    private static final Identifier SPEED_ABILITY_ICON = Identifier.of(RideHudMod.MOD_ID, "textures/gui/sprites/hud/speed_ability_icon.png");

    public SpeedStatsRenderer() {
        super(
                0, 55,
                91, 64,
                true,
                SPEED_ABILITY_BAR_BACKGROUND,
                SPEED_ABILITY_BAR_PROGRESS,
                SPEED_ABILITY_ICON
        );
    }
}
