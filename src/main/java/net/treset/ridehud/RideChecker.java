package net.treset.ridehud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.instances.HorseStats;
import net.treset.ridehud.entity_stats.instances.LlamaStats;

public class RideChecker {
    private static Entity prevVehicle = null;

    public static boolean requestUpdate = false;
    public static boolean onApplicableVehicle = false;

    public static void checkRideStatus(boolean force) {
        if(MinecraftClient.getInstance() == null) {
            return;
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) {
            VehicleStats.setInstance(null);
            return;
        }

        Entity vehicle = player.getVehicle();

        if(vehicle != prevVehicle || force) {
            prevVehicle = vehicle;
            if(vehicle == null) {
                onApplicableVehicle = false;
                VehicleStats.setInstance(null);
            } else if(vehicle instanceof LlamaEntity llama) {
                onApplicableVehicle = true;
                VehicleStats.setInstance(new LlamaStats(llama));
            } else if(vehicle instanceof AbstractHorseEntity horse) {
                onApplicableVehicle = true;
                VehicleStats.setInstance(new HorseStats(horse));
            } else {
                onApplicableVehicle = false;
                VehicleStats.setInstance(null);
            }
        }

        if(VehicleStats.hasInstance()) {
            VehicleStats.getInstance().update();
        }
    }

    public static boolean getUpdateReq() { return requestUpdate; }
}
