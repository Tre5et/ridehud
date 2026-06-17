package net.treset.ridehud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;
import net.treset.ridehud.entity_stats.VehicleStats;

public class VehicleHudRenderer {
    private static int playerHealthOffset = 0;

    public static void render(GuiGraphicsExtractor ctx) {
        if(!VehicleStats.hasInstance() || Minecraft.getInstance().gui.hud.isHidden()) return;
        VehicleStats.getInstance().render(ctx);
    }

    public static int getPlayerHealthOffset() {
        return playerHealthOffset;
    }

    public static void updatePlayerHealthOffset() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        if(player.gameMode() == GameType.CREATIVE || player.gameMode() == GameType.SPECTATOR) {
            playerHealthOffset = 0;
            return;
        }
        int totalHealth = (int)player.getMaxHealth() + (int)player.getAbsorptionAmount();
        int heartHeight = 9;
        if(totalHealth > 60) heartHeight = 8;
        if(totalHealth > 80) heartHeight = 7;
        if(totalHealth > 100) heartHeight = 6;
        if(totalHealth > 120) heartHeight = 5;
        if(totalHealth > 140) heartHeight = 4;
        if(totalHealth > 160) heartHeight = 3;
        playerHealthOffset = (totalHealth - 1) / 20 * heartHeight;
    }
}
