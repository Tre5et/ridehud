package net.treset.ridehud.hud.vehicle_huds;

import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.hud.VehicleHudRenderer;

public class VehicleHud {

    public boolean active = false;
    public boolean hasHealth = false;
    public boolean hasJump = false;
    public boolean hasSpeed = false;
    public VehicleStats stats = null;

    public void setActive(boolean active) {
        this.active = active;
        if(active) {
            if(VehicleHudRenderer.hud != null) {
                VehicleHudRenderer.hud.setActive(false);
            }
            VehicleHudRenderer.hud = this;
        } else {
            VehicleHudRenderer.hud = null;
        }
    }
}
