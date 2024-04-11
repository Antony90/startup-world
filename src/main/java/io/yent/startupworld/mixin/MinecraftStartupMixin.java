package io.yent.startupworld.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.realmsclient.client.RealmsClient;

import io.yent.startupworld.StartupWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadInstance;

@Mixin(Minecraft.class)
public abstract class MinecraftStartupMixin {
    /**
     * The setInitialScreen method transitions the screen from a loading screen
     * to the title screen. We want to load our world instead of transitioning to
     * the title screen.
     * 
     * Inject our method for loading a world before Minecraft transitions
     * from the loading screen to the title screen.
     * 
     * If we successfully load a world, cancel the original method which follows
     * If we fail to load a world, the original method is executed and
     * we transition to the title screen as usual
     * 
     * This method is called once on startup
     */
    @Inject(method = "setInitialScreen", at = @At("HEAD"), cancellable = true)
    private void setInitialScreen(RealmsClient rClient, ReloadInstance reloadInst, GameConfig.QuickPlayData gameCfg, CallbackInfo ci) {
        boolean success = StartupWorld.loadWorld();

        // If a world was loaded successfully, cancel the callback
        // To not execute the original setInitialScreen method
        // Otherwise, if a world was not loaded, the original method is called
        // And Minecraft transitions to the title screen as usual
        if (success) {
            ci.cancel();
            StartupWorld.LOGGER.debug("Skipping remaining Minecraft load sequence");
        } else {
            StartupWorld.LOGGER.warn("No world loaded, resuming default Minecraft load sequence");
        }
    }
}
