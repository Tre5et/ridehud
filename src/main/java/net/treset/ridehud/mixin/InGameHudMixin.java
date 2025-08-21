package net.treset.ridehud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.VehicleHudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    public void onRender(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo info) {

        VehicleHudRenderer.render(ctx);
    }

    @ModifyVariable(method = "renderHeldItemTooltip(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "STORE"), ordinal = 2)
    private int injectedInt(int k) {
        int value = k;
        if(VehicleStats.hasInstance()) {
            // Move item tooltip over bars
            if(Config.displayText.getBoolean()) {
                value -= 18 + Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();
            } else {
                value -= 8 + Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();
            }

            // Fix creative mode item tooltip inconsistency
            if(MinecraftClient.getInstance().interactionManager == null) return value;
            if (!MinecraftClient.getInstance().interactionManager.hasStatusBars()) {
                value -= 14;
            }
        }
        return value;
    }

    @ModifyConstant(method = "renderOverlayMessage(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", constant = @Constant(intValue = 68))
    private int injectedArg(int k) {
        int value = k;
        if(VehicleStats.hasInstance()) {
            // Move Overlay tooltips (notable "Press Shift to Dismount") over bars
            if(Config.displayText.getBoolean()) {
                value += 18 + Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();
            } else {
                value += 8 + Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();
            }
        }
        return value;
    }
}