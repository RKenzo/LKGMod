package com.mods.lkg;

import com.mods.lkg.command.CommandManager;
import com.mods.lkg.command.ListCommands;
import com.mods.lkg.command.coord.ListCoords;
import com.mods.lkg.config.Config;
import com.mods.lkg.command.coord.DelCoords;
import com.mods.lkg.command.coord.SaveCoords;
//import cc.polyfrost.example.command.startCommands;
import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = LKGModMain.MODID, name = LKGModMain.NAME, version = LKGModMain.VERSION)
public class LKGModMain {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    @Mod.Instance(MODID)
    public static LKGModMain INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static Config config;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new Config();
//        startCommands = new startCommands();
        cc.polyfrost.oneconfig.utils.commands.CommandManager.INSTANCE.registerCommand(new CommandManager());
//        startCommands.onInit();
        ClientCommandHandler.instance.registerCommand(new SaveCoords());
        ClientCommandHandler.instance.registerCommand(new DelCoords());
        ClientCommandHandler.instance.registerCommand(new ListCommands());
        ClientCommandHandler.instance.registerCommand(new ListCoords());
    }
}
