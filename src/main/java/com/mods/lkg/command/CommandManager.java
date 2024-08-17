package com.mods.lkg.command;

import com.mods.lkg.LKGModMain;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleMod.java with `CommandManager.INSTANCE.registerCommand(new ExampleCommand());`
 *
 * @see Command
 * @see Main
 * @see LKGModMain
 */
@Command(value = LKGModMain.MODID, description = "Access the " + LKGModMain.NAME + " GUI.")
public class CommandManager {
    @Main
    private void handle() {
        LKGModMain.INSTANCE.config.openGui();
    }
}