package net.treset.ridehud.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import net.minecraft.client.resource.language.I18n;
import net.treset.ridehud.RideHudMod;
import net.treset.ridehud.config.custom_configs.ConfigIntegerPretty;
import net.treset.ridehud.config.lists.DisplayMode;
import net.treset.ridehud.hud.VehicleHudRenderer;
import net.treset.ridehud.tools.FileTools;

import java.io.File;

public class Config implements IConfigHandler {
    public static final int CONFIG_VERSION = 0;

    public static class General {
        public static final ConfigOptionList DISPLAY_MODE = new ConfigOptionList("config.ridehud.general.display_mode", DisplayMode.FULL, I18n.translate("config.ridehud.general.display_mode.comment"), "config.ridehud.general.display_mode");
        public static final ConfigBoolean DISPLAY_TEXT = new ConfigBoolean("config.ridehud.general.display_text", false, I18n.translate("config.ridehud.general.display_text.comment"), "config.ridehud.general.display_text");
        public static final ConfigIntegerPretty BAR_OFFSET = new ConfigIntegerPretty("config.ridehud.general.bar_offset", 0, I18n.translate("config.ridehud.general.bar_offset.comment"), "config.ridehud.general.bar_offset");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                DISPLAY_MODE,
                DISPLAY_TEXT,
                BAR_OFFSET
        );

    }

    public static void loadFromFile() {
        File configFile = new File(RideHudMod.CONFIG_DIR_NAME, RideHudMod.CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) { //does file exist and is readable

            JsonObject obj;
            if ((obj = FileTools.readJsonFile(configFile)) != null) { //read file
                ConfigUtils.readConfigBase(obj, "config.ridehud.general", Config.General.OPTIONS);
            }
        }

        VehicleHudRenderer.checkDisplayChange(true);
    }

    public static void saveToFile() {
        File dir = RideHudMod.CONFIG_DIR;

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) { //does config dir exist or create it

            JsonObject obj = new JsonObject();

        ConfigUtils.writeConfigBase(obj, "config.ridehud.general", Config.General.OPTIONS);
            obj.add("config_version", new JsonPrimitive(CONFIG_VERSION)); //add config file version option

            FileTools.writeJsonToFile(obj, new File(dir, RideHudMod.CONFIG_FILE_NAME)); //write file
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
