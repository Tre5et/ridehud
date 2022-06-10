package net.treset.ridehud.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.treset.ridehud.RideHudClient;
import net.treset.ridehud.hud.VehicleHudRenderer;
import net.treset.vanillaconfig.config.*;
import net.treset.vanillaconfig.config.managers.SaveLoadManager;
import net.treset.vanillaconfig.config.version.ConfigVersion;
import net.treset.vanillaconfig.screen.ConfigScreen;
import net.treset.vanillaconfig.tools.FileTools;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Config {

    public static final PageConfig mainPage = new PageConfig("config.ridehud");

    private static final String[] displayOpt = new String[] {"config.ridehud.display_mode.full", "config.ridehud.display_mode.deop"};
    public static final ListConfig displayMode = new ListConfig(displayOpt, 0, "config.ridehud.display_mode", "config.ridehud.display_mode.comment");
    public static final BooleanConfig displayText = new BooleanConfig(false, "config.ridehud.display_text", "config.ridehud.display_text.comment");
    public static final IntegerConfig barOffset = new IntegerConfig(0, -1000, 10000, "config.ridehud.bar_offset", "config.ridehud.bar_offset.comment");
    public static final IntegerConfig heartOffset = new IntegerConfig(0, -1000, 10000, "config.ridehud.heart_offset", "config.ridehud.heart_offset.comment");
    public static final KeybindConfig openGui = new KeybindConfig(new int[]{35} /*H*/, 0, 5, "config.ridehud.open_gui");

    public static void init() {
        mainPage.addOption(displayMode);
        mainPage.addOption(displayText);
        mainPage.addOption(barOffset);
        mainPage.addOption(heartOffset);
        mainPage.addOption(openGui);

        if(!mainPage.loadVersion())
                if(!mainPage.loadVersionOf("ridehud"))
                        mainPage.loadVersionOf("ridhud");

        if(!mainPage.hasVersion()) migrateFromMalilib();
        else if(mainPage.getVersion().matches(new ConfigVersion("1.0.0"))) mainPage.migrateFileFrom("ridehud/ridhud.json");

        mainPage.setVersion(new ConfigVersion(1, 0,1));

        mainPage.setSaveName("ridehud");
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

        if(!FileTools.fileExists(mainPage.getFile(true))) return;

        JsonElement oldConfig = FileTools.readJsonFile(mainPage.getFile(true));
        if(oldConfig != null && oldConfig.isJsonObject()) {
            JsonElement general = oldConfig.getAsJsonObject().get("config.ridehud.general");
            if (general != null && general.isJsonObject()) {
                JsonElement displayMode = general.getAsJsonObject().get("config.ridehud.general.display_mode");
                if (displayMode != null && displayMode.isJsonPrimitive()) {
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


        File optionsFile = new File("./options.txt"); //migrate keybinding
        if(optionsFile.exists() && optionsFile.isFile() && optionsFile.canRead()) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(optionsFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.startsWith("key_key.ridehud.config_gui:")) {
                        String keyName = null;
                        try {
                            keyName = line.substring(27);
                        } catch (IndexOutOfBoundsException e) {
                            return;
                        }

                        if(keyName.isEmpty()) return;
                        InputUtil.Key key = null;
                        try {
                            key = InputUtil.fromTranslationKey(keyName);
                        } catch(IllegalArgumentException e) {
                            return;
                        }
                        if(key == null) return;

                        if(!GLFW.glfwInit()) return;

                        int keyCode = key.getCode();
                        int scanCode = GLFW.glfwGetKeyScancode(keyCode);

                        openGui.setKeys(new int[] {scanCode});
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
