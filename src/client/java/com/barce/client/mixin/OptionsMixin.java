package com.barce.client.mixin;

import com.barce.client.LuminaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Inject(method = "load", at = @At("RETURN"))
    private void lumina$onLoad(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (LuminaClient.getConfig() != null) {
            LuminaClient.applyBrightness(client, false);
        }
    }
    @Inject(method = "save", at = @At("HEAD"))
    private void lumina$preSave(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        // Temporarily clamp gamma to valid range for saving
        double current = mc.options.gamma().get();
        if (current > 1.0) {
            mc.options.gamma().value = Math.min(1.0, current);
        }
    }
    @Inject(method = "save", at = @At("RETURN"))
    private void lumina$postSave(CallbackInfo ci) {
        // Restore gamma after save
        LuminaClient.applyBrightness(Minecraft.getInstance(), false);
    }
}