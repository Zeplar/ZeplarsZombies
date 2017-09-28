package com.zeplar.zeplarszombies;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModInfo.MODID)
@Config.LangKey("zeplarszombies.config.title")
public class ModConfig {

    @Config.Comment("This is an example boolean property.")
    public static boolean fooBar = false;

    public static final Client client = new Client();

    public static class Client {

        @Config.Comment("This is an example int property.")
        public int baz = -100;

        public final HUDPos chunkEnergyHUDPos = new HUDPos(0, 0);

        public static class HUDPos {
            public HUDPos(final int x, final int y) {
                this.x = x;
                this.y = y;
            }

            @Config.Comment("The x coordinate")
            public int x;

            @Config.Comment("The y coordinate")
            public int y;
        }
    }

    @Mod.EventBusSubscriber(modid = ModInfo.MODID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ModInfo.MODID)) {
                ConfigManager.sync(ModInfo.MODID, Config.Type.INSTANCE);
            }
        }
    }
}