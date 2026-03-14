package net.treset.ridehud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.instances.HorseStats;
import net.treset.ridehud.entity_stats.instances.LlamaStats;

public class RideChecker {
    private static Entity prevVehicle = null;

    public static boolean onApplicableVehicle = false;

    public static void checkRideStatus(boolean force) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            VehicleStats.setInstance(null);
            return;
        }

        Entity vehicle = player.getVehicle();

        if(vehicle != prevVehicle || force) {
            prevVehicle = vehicle;
            switch (vehicle) {
                case Llama llama -> {
                    onApplicableVehicle = true;
                    VehicleStats.setInstance(new LlamaStats(llama));
                }
                case AbstractHorse horse -> {
                    onApplicableVehicle = true;
                    VehicleStats.setInstance(new HorseStats(horse));
                }
                case null, default -> {
                    onApplicableVehicle = false;
                    VehicleStats.setInstance(null);
                }
            }
        }

        if(VehicleStats.hasInstance()) {
            VehicleStats.getInstance().update();
            VehicleHudRenderer.updatePlayerHealthOffset();
        }
    }
}
