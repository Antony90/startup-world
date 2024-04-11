package io.yent.startupworld;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

import org.slf4j.Logger;

@Mod(StartupWorld.MODID)
public class StartupWorld {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "startupworld";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public StartupWorld() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, Config.SPEC);
    }

    public static boolean loadWorld() {

        Minecraft client = Minecraft.getInstance();
        Screen screen = client.screen;

        LevelStorageSource lvlStorageSrc = client.getLevelSource();
        WorldOpenFlows worldOpenFlows = client.createWorldOpenFlows();

        // Use config world name if exists
        if (Config.worldName.length() > 0) {
            LOGGER.info("Loading world \"" + Config.worldName + "\" from configuration");
            worldOpenFlows.loadLevel(screen, Config.worldName);
            return true;
        }

        // Find and load the last modified world
        LOGGER.info("Finding last modified world");
        long latestLastPlayedTime = Long.MIN_VALUE;
        String lastPlayedWorldName = null;

        for (LevelStorageSource.LevelDirectory lvl : lvlStorageSrc.findLevelCandidates()) {
            long lastModified = lvl.path().toFile().lastModified();

            if (lastModified > latestLastPlayedTime) {
                latestLastPlayedTime = lastModified;
                lastPlayedWorldName = lvl.directoryName();
            }
        }

        if (lastPlayedWorldName == null) {
            LOGGER.info("No saves found, no world will be loaded");
        } else {
            LOGGER.info("Loading last modified world \"" + lastPlayedWorldName + "\"");
            worldOpenFlows.loadLevel(screen, lastPlayedWorldName);
        }

        return lastPlayedWorldName != null;
    }
}
