package net.treset.ridehud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;
import net.treset.ridehud.entity_stats.VehicleStats;

public class VehicleHudRenderer {
    private static int playerHealthOffset = 0;

    public static void render(DrawContext ctx) {
        if(!VehicleStats.hasInstance() || MinecraftClient.getInstance().options.hudHidden) return;
        VehicleStats.getInstance().render(ctx);
    }

    public static int getPlayerHealthOffset() {
        return playerHealthOffset;
    }

    public static void updatePlayerHealthOffset() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
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
