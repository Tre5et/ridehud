package net.treset.ridehud;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RideHudMod implements ModInitializer {
	public static final String MOD_ID = "ridehud";
	public static final String CONFIG_DIR_NAME = MOD_ID;
	public static final String CONFIG_FILE_NAME = MOD_ID + ".json";
	public static File CONFIG_DIR;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
	}
}
