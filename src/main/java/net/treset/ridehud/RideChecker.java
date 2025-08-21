package net.treset.ridehud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.instances.HorseStats;
import net.treset.ridehud.entity_stats.instances.LlamaStats;
import net.treset.vanillaconfig.tools.ClientTools;

public class RideChecker {
    private static Entity prevVehicle = null;

    private static MinecraftClient cli;

    public static boolean requestUpdate = false;
    public static boolean onApplicableVehicle = false;

    public static void checkRideStatus(boolean force) {

        if(cli == null && !ClientTools.isInGame()) {
            cli = MinecraftClient.getInstance();
            return;
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if(player == null) return;

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

        if(VehicleStats.hasInstance() && requestUpdate) VehicleStats.getInstance().updateCurrent();
    }

    public static boolean getUpdateReq() { return requestUpdate; }
}
