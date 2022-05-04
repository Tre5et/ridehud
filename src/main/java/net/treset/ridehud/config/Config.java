package net.treset.ridehud.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.MinecraftClient;
import net.treset.ridehud.RideHudClient;
import net.treset.ridehud.RideHudMod;
import net.treset.ridehud.hud.VehicleHudRenderer;
import net.treset.ridehud.tools.FileTools;
import net.treset.vanillaconfig.config.*;
import net.treset.vanillaconfig.config.managers.SaveLoadManager;
import net.treset.vanillaconfig.config.version.ConfigVersion;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class Config {

    public static final PageConfig mainPage = new PageConfig(RideHudMod.MOD_ID);

    private static final String[] displayOpt = new String[] {"config.ridehud.general.display_mode.full", "config.ridehud.general.display_mode.deop"};
    public static final ListConfig displayMode = new ListConfig(displayOpt, 0, "config.ridehud.general.display_mode", "config.ridehud.general.display_mode.comment");
    public static final BooleanConfig displayText = new BooleanConfig(false, "config.ridehud.general.display_text", "config.ridehud.general.display_text.comment");
    public static final IntegerConfig barOffset = new IntegerConfig(0, -1000, 10000, "config.ridehud.general.bar_offset", "config.ridehud.general.bar_offset.comment");
    public static final IntegerConfig heartOffset = new IntegerConfig(0, -1000, 10000, "config.ridehud.general.heart_offset", "config.ridehud.general.heart_offset.comment");
    public static final KeybindConfig openGui = new KeybindConfig(new int[]{35}, 0, 5, "key.ridehud.config_gui");

    public static void init() {
        mainPage.addOption(displayMode);
        mainPage.addOption(displayText);
        mainPage.addOption(barOffset);
        mainPage.addOption(heartOffset);
        mainPage.addOption(openGui);

        mainPage.loadVersion();
        if(!mainPage.hasVersion()) migrateFromMalilib();

        mainPage.setVersion(new ConfigVersion(1, 0,0));

        mainPage.setPath("ridehud");
        SaveLoadManager.globalSaveConfig(mainPage);

        RideHudClient.configScreen = new ConfigScreen(mainPage, MinecraftClient.getInstance().currentScreen);

        displayMode.onChange(Config::onDisplayModeChanged);
        displayText.onChange(Config::onDisplayTextChanged);
        barOffset.onChange(Config::onBarOffsetChanged);
        heartOffset.onChange(Config::onHeartOffsetChanged);
        openGui.onPressed(Config::onConfigHotkeyPressed);
    }

    public static void onDisplayModeChanged(int prevInt, String prevOption, String key) {
        VehicleHudRenderer.setDisplayMode(displayMode.getOptionIndex());
    }
    public static void onDisplayTextChanged(boolean prevBool, String key) {
        VehicleHudRenderer.setDisplayTexts(displayText.getBoolean());
    }
    public static void onBarOffsetChanged(int prevOffset, String key) {
        VehicleHudRenderer.setBarOffset(barOffset.getInteger());
    }
    public static void onHeartOffsetChanged(int prevOffset, String key) {
        VehicleHudRenderer.setHeartOffset(heartOffset.getInteger());
    }
    public static void onConfigHotkeyPressed(String key) {
        MinecraftClient.getInstance().setScreen(RideHudClient.configScreen);
    }

    private static void migrateFromMalilib() {
        mainPage.migrateFileFrom("ridehud/ridehud.json");

        JsonElement oldConfig = FileTools.readJsonFile(mainPage.getFile(true));
        if(oldConfig != null && oldConfig.isJsonObject()) {
            JsonElement general = oldConfig.getAsJsonObject().get("config.ridehud.general");
            if(general != null && general.isJsonObject()) {
                JsonElement displayMode = general.getAsJsonObject().get("config.ridehud.general.display_mode");
                if(displayMode != null && displayMode.isJsonPrimitive()) {
                    String newDisplayMode = (displayMode.getAsJsonPrimitive().getAsString().equals("full")) ? "config.ridehud.general.display_mode.full" : "config.ridehud.general.display_mode.deop";
                    general.getAsJsonObject().remove("config.ridehud.general.display_mode");
                    general.getAsJsonObject().add("config.ridehud.general.display_mode", new JsonPrimitive(newDisplayMode));
                    FileTools.writeJsonToFile(oldConfig.getAsJsonObject(), mainPage.getFile(true));
                }
            }
        }

        displayMode.migrateFrom("config.ridehud.general/config.ridehud.general.display_mode");
        displayText.migrateFrom("config.ridehud.general/config.ridehud.general.display_text");
        barOffset.migrateFrom("config.ridehud.general/config.ridehud.general.bar_offset");
    }
}
