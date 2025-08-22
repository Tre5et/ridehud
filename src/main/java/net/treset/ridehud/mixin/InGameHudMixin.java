package net.treset.ridehud.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.VehicleHudRenderer;
import net.treset.ridehud.entity_stats.VehicleStatsType;
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
        return k - getTooltipBarOffset(true);
    }

    @ModifyConstant(method = "renderOverlayMessage(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", constant = @Constant(intValue = 68))
    private int injectedArg(int k) {
        return k + getTooltipBarOffset(false);
    }

    private static int getTooltipBarOffset(boolean fixCreativeModeOffset) {
        if(!VehicleStats.hasInstance() || !(VehicleStats.getInstance().has(VehicleStatsType.SPEED) || VehicleStats.getInstance().has(VehicleStatsType.SPEED))) {
            return 0;
        }

        int offset = 8 + Config.barOffset.getInteger() + VehicleHudRenderer.getPlayerHealthOffset();
        if(Config.displayText.getBoolean()) {
            offset += 10;
        }

        // Fix creative mode item tooltip inconsistency
        if(MinecraftClient.getInstance().interactionManager == null || !fixCreativeModeOffset) return offset;
        if (!MinecraftClient.getInstance().interactionManager.hasStatusBars()) {
            offset += 14;
        }
        return offset;
    }
}