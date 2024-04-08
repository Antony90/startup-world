package io.yent.startupworld;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = StartupWorld.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> WORLD_NAME = BUILDER
            .comment("World name to load on startup, accepts spaces", "Use empty string \"\" to load the last played world instead")
            .define("worldName", "");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String worldName;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        worldName = WORLD_NAME.get();
    }
}
