package net.treset.ridehud;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.treset.ridehud.config.Config;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class RideHudClient implements ClientModInitializer {
    public static ConfigScreen getConfigScreen() {
        return new ConfigScreen(Config.mainPage, Minecraft.getInstance().screen);
    }

    @Override
    public void onInitializeClient() {
        Config.init();
    }
}
