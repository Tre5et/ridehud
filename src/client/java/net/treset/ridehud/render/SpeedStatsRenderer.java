package net.treset.ridehud.render;

import net.minecraft.resources.Identifier;
import net.treset.ridehud.RideHudMod;

public class SpeedStatsRenderer extends BarRenderer {
    private static final Identifier SPEED_ABILITY_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(RideHudMod.MOD_ID, "hud/speed_ability_bar_background");
    private static final Identifier SPEED_ABILITY_BAR_PROGRESS = Identifier.fromNamespaceAndPath(RideHudMod.MOD_ID, "hud/speed_ability_bar_progress");
    private static final Identifier SPEED_ABILITY_ICON = Identifier.fromNamespaceAndPath(RideHudMod.MOD_ID, "hud/speed_ability_icon");

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
