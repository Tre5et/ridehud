package net.treset.ridehud.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.MinecraftClient;
import net.treset.ridehud.RideHudClient;
import net.treset.ridehud.hud.VehicleHudRenderer;
import net.treset.ridehud.tools.FileTools;
import net.treset.vanillaconfig.config.*;
import net.treset.vanillaconfig.config.managers.SaveLoadManager;
import net.treset.vanillaconfig.config.version.ConfigVersion;
import net.treset.vanillaconfig.screen.ConfigScreen;

public class Config {

    public static final PageConfig mainPage = new PageConfig("config.ridehud");

    private static final String[] displayOpt = new String[] {"config.ridehud.display_mode.full", "config.ridehud.display_mode.deop"};
    public static final ListConfig displayMode = new ListConfig(displayOpt, 0, "config.ridehud.display_mode", "config.ridehud.display_mode.comment");
    public static final BooleanConfig displayText = new BooleanConfig(false, "config.ridehud.display_text", "config.ridehud.display_text.comment");
    public static final IntegerConfig barOffset = new IntegerConfig(0, -1000, 10000, "config.ridehud.bar_offset", "config.ridehud.bar_offset.comment");
    public static final IntegerConfig heartOffset = new IntegerConfig(0, -1000, 10000, "config.ridehud.heart_offset", "config.ridehud.heart_offset.comment");
    public static final KeybindConfig openGui = new KeybindConfig(new int[]{35}, 0, 5, "config.ridehud.open_gui");

    public static void init() {
        mainPage.addOption(displayMode);
        mainPage.addOption(displayText);
        mainPage.addOption(barOffset);
        mainPage.addOption(heartOffset);
        mainPage.addOption(openGui);

        mainPage.loadVersion();
        if(!mainPage.hasVersion()) migrateFromMalilib();

        mainPage.setVersion(new ConfigVersion(1, 0,0));

        mainPage.setSaveName("ridhud");
        mainPage.setPath("ridehud");
        SaveLoadManager.globalSaveConfig(mainPage);

        RideHudClient.configScreen = new ConfigScreen(mainPage, MinecraftClient.getInstance().currentScreen);

        displayMode.setFullWidth(false);
        displayText.setFullWidth(false);
        barOffset.setFullWidth(false);
        heartOffset.setFullWidth(false);

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
