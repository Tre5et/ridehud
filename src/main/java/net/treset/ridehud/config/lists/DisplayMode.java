package net.treset.ridehud.config.lists;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import net.minecraft.client.resource.language.I18n;
import net.treset.ridehud.hud.VehicleHudRenderer;

public enum DisplayMode implements IConfigOptionListEntry {
    FULL    (   "full", "config.ridehud.general.display_mode.full"),
    DEOP    (   "deop", "config.ridehud.general.display_mode.deop");

    private final String configString;
    private final String displayName;

    private DisplayMode(String configString, String displayName)
    {
        this.configString = configString;
        this.displayName = displayName;
    }

    @Override
    public String getStringValue()
    {
        return this.configString;
    }

    @Override
    public String getDisplayName()
    {

        return I18n.translate(this.displayName);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();

        if (forward) {
            if (++id >= values().length) {
                id = 0;
            }
        } else {
            if (--id < 0) {
                id = values().length - 1;
            }
        }

        VehicleHudRenderer.setDisplayMode(values()[id % values().length]);
        return values()[id % values().length];
    }

    @Override
    public DisplayMode fromString(String name)
    {
        return fromStringStatic(name);
    }

    public static DisplayMode fromStringStatic(String name) {
        for (DisplayMode mode : DisplayMode.values()) {
            if (mode.configString.equalsIgnoreCase(name)) {
                return mode;
            }
        }

        return DisplayMode.FULL;
    }
}
