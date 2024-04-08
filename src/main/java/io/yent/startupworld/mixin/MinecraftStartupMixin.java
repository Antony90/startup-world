package io.yent.startupworld.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.realmsclient.client.RealmsClient;

import io.yent.startupworld.StartupWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadInstance;

@Mixin(Minecraft.class)
public abstract class MinecraftStartupMixin {
    /**
     * Overwrite default behaviour of transitioning to the title screen when the game loads
     * Instead, load the world
     * 
     * This only happens once
     */
    @Overwrite
    private void setInitialScreen(RealmsClient rClient, ReloadInstance reloadInst, GameConfig.QuickPlayData gameCfg) {
        StartupWorld.run();
    }
}
