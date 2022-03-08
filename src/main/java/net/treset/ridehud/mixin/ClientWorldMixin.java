package net.treset.ridehud.mixin;

import net.minecraft.client.world.ClientWorld;
import net.treset.ridehud.RideChecker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        RideChecker.checkRideStatus();
    }

}
