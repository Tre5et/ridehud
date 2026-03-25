package net.treset.ridehud.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.treset.ridehud.VehicleHudRenderer;
import net.treset.ridehud.config.Config;
import net.treset.ridehud.entity_stats.VehicleStats;
import net.treset.ridehud.entity_stats.VehicleStatsType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    public void onRender(GuiGraphicsExtractor ctx, DeltaTracker deltaTracker, CallbackInfo info) {
        VehicleHudRenderer.render(ctx);
    }

    @ModifyVariable(method = "extractSelectedItemName(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At(value = "STORE"), ordinal = 2)
    private int injectedInt(int k) {
        return k - getTooltipBarOffset(true);
    }

    @ModifyConstant(method = "extractOverlayMessage(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", constant = @Constant(intValue = 68))
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
        if(Minecraft.getInstance().gameMode == null || !fixCreativeModeOffset) return offset;
        if (!Minecraft.getInstance().gameMode.canHurtPlayer()) {
            offset += 14;
        }
        return offset;
    }
}