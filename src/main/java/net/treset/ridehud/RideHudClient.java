package net.treset.ridehud;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.config.gui.ConfigGui;
import net.treset.ridehud.tools.FileTools;
import net.treset.ridehud.tools.KeybindTools;
import org.lwjgl.glfw.GLFW;

public class RideHudClient implements ClientModInitializer {

    public static ConfigGui configScreen;

    @Override
    public void onInitializeClient() {

        KeybindTools.registerKeybind( //setup keybinds
                "key.ridehud.config_gui", GLFW.GLFW_KEY_H);
        ClientTickEvents.END_CLIENT_TICK.register(client -> { KeybindTools.resolveKeybinds(); });

        ConfigManager.getInstance().registerConfigHandler(RideHudMod.MOD_ID, new Config()); //register config

        if(!FileTools.loadOrCreateConfigDir()) return; //setup config directory
    }
}
