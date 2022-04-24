package net.treset.ridehud;

import net.fabricmc.api.ClientModInitializer;
import net.treset.ridehud.config.Config;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class RideHudClient implements ClientModInitializer {

    public static ConfigScreen configScreen;

    @Override
    public void onInitializeClient() {
        Config.init();
    }
}
