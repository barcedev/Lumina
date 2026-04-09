package com.barce.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class LuminaConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("lumina.json");
    public double globalDefault = 1.0;
    public double globalFullbright = 15.0;
    public boolean showToggleMessage = true;
    public boolean showDimensionChangeMessage = true;
    public boolean simpleMessage = false;
    public double overworldDefault    = -5;
    public double overworldFullbright = -5;
    public double netherDefault    = -5;
    public double netherFullbright = -5;
    public double endDefault    = -5;
    public double endFullbright = -5;
    public transient boolean fullbrightActive = false;
    public static LuminaConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                LuminaConfig cfg = GSON.fromJson(reader, LuminaConfig.class);
                return cfg != null ? cfg : new LuminaConfig();
            } catch (IOException e) {
                System.err.println("[Lumina] Failed to read config, using defaults: " + e.getMessage());
            }
        }
        return new LuminaConfig();
    }
    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[Lumina] Failed to save config: " + e.getMessage());
        }
    }
    /**
     * Returns the effective default brightness for the given dimension key,
     * falling back to the global default if no per-dim override is configured.
     *
     * @param dimensionKey  e.g. "minecraft:overworld", "minecraft:the_nether", "minecraft:the_end"
     */
    public double getEffectiveDefault(String dimensionKey) {
        return switch (dimensionKey) {
            case "minecraft:overworld"  -> overworldDefault  >= 0 ? overworldDefault  : globalDefault;
            case "minecraft:the_nether" -> netherDefault     >= 0 ? netherDefault     : globalDefault;
            case "minecraft:the_end"    -> endDefault        >= 0 ? endDefault        : globalDefault;
            default                     -> globalDefault;
        };
    }
    /**
     * Returns the effective fullbright brightness for the given dimension key,
     * falling back to the global fullbright if no per-dim override is configured.
     */
    public double getEffectiveFullbright(String dimensionKey) {
        return switch (dimensionKey) {
            case "minecraft:overworld"  -> overworldFullbright  >= 0 ? overworldFullbright  : globalFullbright;
            case "minecraft:the_nether" -> netherFullbright     >= 0 ? netherFullbright     : globalFullbright;
            case "minecraft:the_end"    -> endFullbright        >= 0 ? endFullbright        : globalFullbright;
            default                     -> globalFullbright;
        };
    }
    /** Clamp a brightness value to the legal range [0.0, 15.0]. */
    public static double clamp(double value) {
        return Math.max(0.0, Math.min(15.0, value));
    }
}