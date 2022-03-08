package net.treset.ridehud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.treset.ridehud.hud.VehicleHudRenderer;
import net.treset.ridehud.hud.vehicle_huds.DonkeyHud;
import net.treset.ridehud.hud.vehicle_huds.HorseHud;
import net.treset.ridehud.hud.vehicle_huds.MuleHud;

public class RideChecker {
    private static Entity prevVehicle = null;

    private static MinecraftClient cli;

    public static boolean requestUpdate = false;
    public static boolean onApplicableVehicle = false;

    public static void checkRideStatus() {

        if(cli == null) {
            cli = MinecraftClient.getInstance();
            return;
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if(player == null) return;

        Entity vehicle = player.getVehicle();

        if(vehicle != prevVehicle) {
            prevVehicle = vehicle;
            if(vehicle == null) {
                onApplicableVehicle = false;
                if(VehicleHudRenderer.hud != null) VehicleHudRenderer.hud.setActive(false);

            } else if(vehicle instanceof HorseEntity horse) {
                onApplicableVehicle = true;
                HorseHud hud = new HorseHud(horse);
                hud.setActive(true);
            } else if(vehicle instanceof DonkeyEntity donkey) {
                onApplicableVehicle = true;
                DonkeyHud hud = new DonkeyHud(donkey);
                hud.setActive(true);
            } else if(vehicle instanceof MuleEntity mule) {
                onApplicableVehicle = true;
                MuleHud hud = new MuleHud(mule);
                hud.setActive(true);

            } else {
                onApplicableVehicle = false;
                if(VehicleHudRenderer.hud != null) VehicleHudRenderer.hud.setActive(false);
            }
        }

        VehicleHudRenderer.checkDisplayChange(false);

        if(VehicleHudRenderer.hud == null) return;

        if(requestUpdate) updateCurrentOptStats();

        if(VehicleHudRenderer.hud.stats.speedScore < 0) {
            VehicleHudRenderer.speedAccurate = false;
            VehicleHudRenderer.updateSpeed();
        } else if(!VehicleHudRenderer.speedAccurate) VehicleHudRenderer.speedAccurate = true;
    }

    public static void updateCurrentOptStats() {
        VehicleHudRenderer.hud.stats.updateCurrentSpeed();
        VehicleHudRenderer.hud.stats.updateCurrentJumpHeight();
    }
}
