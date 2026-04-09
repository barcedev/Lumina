package com.barce.client;

import com.barce.config.LuminaConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class LuminaClient implements ClientModInitializer {
	private static LuminaConfig config;
	private static KeyMapping toggleKey;
	private static String lastDimension = null;
	public static LuminaConfig getConfig() {
		return config;
	}

	@Override
	public void onInitializeClient() {
		config = LuminaConfig.load();
		toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.lumina.toggle",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_G,
				new KeyMapping.Category(Identifier.fromNamespaceAndPath("lumina", "category"))
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.consumeClick()) {
				config.fullbrightActive = !config.fullbrightActive;
				applyBrightness(client, config.showToggleMessage);
			}
			if (client.level != null) {
				String currentDim = getDimensionKey(client);
				if (!currentDim.equals(lastDimension)) {
					lastDimension = currentDim;
					applyBrightness(client, config.showDimensionChangeMessage);
				}
			}
		});
	}
	public static void applyBrightness(Minecraft client, boolean showMessage) {
		if (client.level == null) return;
		String dim = getDimensionKey(client);
		double target = config.fullbrightActive
				? config.getEffectiveFullbright(dim)
				: config.getEffectiveDefault(dim);
		client.options.gamma().set(target);
		if (showMessage) {
			Component message;
			if (config.simpleMessage) {
				//Component message;
				if (config.fullbrightActive) {
					message = Component.literal("Fullbright ")
							.append(Component.literal("ON").withStyle(ChatFormatting.GREEN))
							.withStyle(Style::withoutShadow);
				} else {
					message = Component.literal("Fullbright ")
							.append(Component.literal("OFF").withStyle(ChatFormatting.RED))
							.withStyle(Style::withoutShadow);
				}
			} else {
				int percent = (int) Math.round(target * 100);
				if (config.fullbrightActive) {
					message = Component.literal("☀ " + percent + "%")
							.withStyle(style -> style.withColor(0xffe1c0)) // on
							.withoutShadow();
				} else {
					message = Component.literal("☀ " + percent + "%")
							.withStyle(style -> style.withColor(0x2e2d2c)) // off
							.withoutShadow();
				}
			}
			client.gui.setOverlayMessage(message, false);
		}
	}
	private static String getDimensionKey(Minecraft client) {
		if (client.level == null) return "minecraft:overworld";
		return client.level.dimensionTypeRegistration().getRegisteredName();
	}
	public static String getCurrentDimension() {
		Minecraft client = Minecraft.getInstance();
		if (client.level == null) return "minecraft:overworld";
		return client.level.dimensionTypeRegistration().getRegisteredName();
	}
}