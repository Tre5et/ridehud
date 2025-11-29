package net.treset.ridehud;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.treset.ridehud.config.Config;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class RideHudClient implements ClientModInitializer {
    public static ConfigScreen getConfigScreen() {
        return new ConfigScreen(Config.mainPage, MinecraftClient.getInstance().currentScreen);
    }

    @Override
    public void onInitializeClient() {
        Config.init();
    }
}
