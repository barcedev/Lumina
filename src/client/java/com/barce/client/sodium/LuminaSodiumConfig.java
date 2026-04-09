package com.barce.client.sodium;

import com.barce.client.LuminaClient;
import com.barce.config.LuminaConfig;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class LuminaSodiumConfig implements ConfigEntryPoint {
    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        LuminaConfig cfg = LuminaClient.getConfig();
        if (cfg == null) return;
        StorageEventHandler saveAndApply = () -> {
            cfg.save();
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) LuminaClient.applyBrightness(mc, false);
        };
        builder.registerOwnModOptions()
                .setIcon(Identifier.parse("lumina:icon.png"))
                // general page
                .setColorTheme(builder.createColorTheme().setBaseThemeRGB(0xffe1c0))
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("lumina.options.page"))

                        .addOptionGroup(builder.createOptionGroup()
                                .setName(Component.translatable("lumina.options.group.global"))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "global_default"))
                                        .setName(Component.translatable("lumina.options.global_default"))
                                        .setTooltip(Component.translatable("lumina.options.global_default.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(0, 100, 5)
                                        .setValueFormatter(v -> Component.literal(v + "%"))
                                        .setBinding(v -> cfg.globalDefault = v / 100.0, () -> (int) Math.round(cfg.globalDefault * 100))
                                        .setDefaultValue(50))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "global_fullbright"))
                                        .setName(Component.translatable("lumina.options.global_fullbright"))
                                        .setTooltip(Component.translatable("lumina.options.global_fullbright.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(0, 1500, 5)
                                        .setValueFormatter(v -> Component.literal(v + "%"))
                                        .setBinding(v -> cfg.globalFullbright = v / 100.0, () -> (int) Math.round(cfg.globalFullbright * 100))
                                        .setDefaultValue(1500)))
                        .addOptionGroup(builder.createOptionGroup()
                                .setName(Component.translatable("lumina.options.group.hud"))
                                .addOption(builder.createBooleanOption(
                                                Identifier.fromNamespaceAndPath("lumina", "show_toggle_message"))
                                        .setName(Component.translatable("lumina.options.show_toggle_message"))
                                        .setTooltip(Component.translatable("lumina.options.show_toggle_message.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setBinding(
                                                v -> cfg.showToggleMessage = v,
                                                () -> cfg.showToggleMessage)
                                        .setDefaultValue(true))

                                .addOption(builder.createBooleanOption(
                                                Identifier.fromNamespaceAndPath("lumina", "show_dimension_change_message"))
                                        .setName(Component.translatable("lumina.options.show_dimension_change_message"))
                                        .setTooltip(Component.translatable("lumina.options.show_dimension_change_message.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setBinding(
                                                v -> cfg.showDimensionChangeMessage = v,
                                                () -> cfg.showDimensionChangeMessage)
                                        .setDefaultValue(true))
                                .addOption(builder.createBooleanOption(
                                                Identifier.fromNamespaceAndPath("lumina", "simple_message"))
                                        .setName(Component.translatable("lumina.options.simple_message"))
                                        .setTooltip(Component.translatable("lumina.options.simple_message.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setBinding(
                                                v -> cfg.simpleMessage = v,
                                                () -> cfg.simpleMessage)
                                        .setDefaultValue(false)))
                // extras page
                )
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("lumina.options.page_extras"))
                        .addOptionGroup(builder.createOptionGroup()
                                .setName(Component.translatable("lumina.options.group.overworld"))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "overworld_default"))
                                        .setName(Component.translatable("lumina.options.dim_default"))
                                        .setTooltip(Component.translatable("lumina.options.dim_default.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(-5, 100, 5)
                                        .setValueFormatter(v -> Component.literal(v < 0 ? "Global" : v + "%"))
                                        .setBinding(v -> cfg.overworldDefault = v < 0 ? -1.0 : v / 100.0, () -> cfg.overworldDefault < 0 ? -5 : (int) Math.round(cfg.overworldDefault * 100))
                                        .setDefaultValue(-5))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "overworld_fullbright"))
                                        .setName(Component.translatable("lumina.options.dim_fullbright"))
                                        .setTooltip(Component.translatable("lumina.options.dim_fullbright.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(-5, 1500, 5)
                                        .setValueFormatter(v -> Component.literal(v < 0 ? "Global" : v + "%"))
                                        .setBinding(v -> cfg.overworldFullbright = v < 0 ? -1.0 : v / 100.0, () -> cfg.overworldFullbright < 0 ? -5 : (int) Math.round(cfg.overworldFullbright * 100))
                                        .setDefaultValue(-5)))

                        .addOptionGroup(builder.createOptionGroup()
                                .setName(Component.translatable("lumina.options.group.nether"))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "nether_default"))
                                        .setName(Component.translatable("lumina.options.dim_default"))
                                        .setTooltip(Component.translatable("lumina.options.dim_default.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(-5, 100, 5)
                                        .setValueFormatter(v -> Component.literal(v < 0 ? "Global" : v + "%"))
                                        .setBinding(v -> cfg.netherDefault = v < 0 ? -1.0 : v / 100.0, () -> cfg.netherDefault < 0 ? -5 : (int) Math.round(cfg.netherDefault * 100))
                                        .setDefaultValue(-5))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "nether_fullbright"))
                                        .setName(Component.translatable("lumina.options.dim_fullbright"))
                                        .setTooltip(Component.translatable("lumina.options.dim_fullbright.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(-5, 1500, 5)
                                        .setValueFormatter(v -> Component.literal(v < 0 ? "Global" : v + "%"))
                                        .setBinding(v -> cfg.netherFullbright = v < 0 ? -1.0 : v / 100.0, () -> cfg.netherFullbright < 0 ? -5 : (int) Math.round(cfg.netherFullbright * 100))
                                        .setDefaultValue(-5)))

                        .addOptionGroup(builder.createOptionGroup()
                                .setName(Component.translatable("lumina.options.group.the_end"))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "end_default"))
                                        .setName(Component.translatable("lumina.options.dim_default"))
                                        .setTooltip(Component.translatable("lumina.options.dim_default.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(-5, 100, 5)
                                        .setValueFormatter(v -> Component.literal(v < 0 ? "Global" : v + "%"))
                                        .setBinding(v -> cfg.endDefault = v < 0 ? -1.0 : v / 100.0, () -> cfg.endDefault < 0 ? -5 : (int) Math.round(cfg.endDefault * 100))
                                        .setDefaultValue(-5))
                                .addOption(builder.createIntegerOption(
                                                Identifier.fromNamespaceAndPath("lumina", "end_fullbright"))
                                        .setName(Component.translatable("lumina.options.dim_fullbright"))
                                        .setTooltip(Component.translatable("lumina.options.dim_fullbright.tooltip"))
                                        .setStorageHandler(saveAndApply)
                                        .setRange(-5, 1500, 5)
                                        .setValueFormatter(v -> Component.literal(v < 0 ? "Global" : v + "%"))
                                        .setBinding(v -> cfg.endFullbright = v < 0 ? -1.0 : v / 100.0, () -> cfg.endFullbright < 0 ? -5 : (int) Math.round(cfg.endFullbright * 100))
                                        .setDefaultValue(-5)))
                );
    }
}