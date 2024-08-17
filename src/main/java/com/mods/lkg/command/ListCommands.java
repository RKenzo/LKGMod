package com.mods.lkg.command;

import com.mods.lkg.modules.messagemanager.MMModule;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ListCommands extends CommandBase {
    String[] Commands = new String[3];

    @Override
    public String getCommandName() {
        return "LKGCommands"; // Nome do comando
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/LKGCommands"; // Descrição de como usar o comando
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
//        if (args.length < 1) {
//            throw new CommandException("Uso incorreto. Use /LKGComandos");
//        }
//    }

        Commands[0] = "/LKGCommands";
        Commands[1] = "/LKGSave <nome>";
        Commands[2] = "/LKGDel <nome>";

        for (int i = 0; i < Commands.length; i++) {
            MMModule.addMessage(Commands[i]);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Nível de permissão necessário para usar o comando (0 para todos)
    }

}

