package com.barce.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin<T> {
    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void lumina$bypassGammaValidation(T value, CallbackInfo ci) {
        OptionInstance<T> self = (OptionInstance<T>)(Object) this;
        String key = "";
        if (self.caption != null) {
            if (self.caption.getContents() instanceof net.minecraft.network.chat.contents.TranslatableContents tc) {
                key = tc.getKey();
            }
        }
        if (!key.equals("options.gamma")) return;
        if (!Minecraft.getInstance().isRunning()) {
            self.value = value;
        } else if (!Objects.equals(self.value, value)) {
            self.value = value;
            self.onValueUpdate.accept(value);
        }
        ci.cancel();
    }
}