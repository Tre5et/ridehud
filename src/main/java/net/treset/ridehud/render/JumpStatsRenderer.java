package net.treset.ridehud.render;

import net.minecraft.util.Identifier;
import net.treset.ridehud.RideHudMod;

public class JumpStatsRenderer extends BarRenderer {
    private static final Identifier JUMP_ABILITY_BAR_BACKGROUND = Identifier.of(RideHudMod.MOD_ID, "hud/jump_ability_bar_background");
    private static final Identifier JUMP_ABILITY_BAR_PROGRESS = Identifier.of(RideHudMod.MOD_ID, "hud/jump_ability_bar_progress");
    private static final Identifier JUMP_ABILITY_ICON = Identifier.of(RideHudMod.MOD_ID, "hud/jump_ability_icon");

    public JumpStatsRenderer() {
        super(
                -91, 55,
                -109, 64,
                false,
                JUMP_ABILITY_BAR_BACKGROUND,
                JUMP_ABILITY_BAR_PROGRESS,
                JUMP_ABILITY_ICON
        );
    }
}
