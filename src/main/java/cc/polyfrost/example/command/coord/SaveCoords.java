package cc.polyfrost.example.command.coord;


import cc.polyfrost.example.modules.messagemanager.MMModule;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCoords extends CommandBase {
    @Override
    public String getCommandName() {
        return "LKGSave"; // Nome do comando
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/LKGSave <nome>"; // Descrição de como usar o comando
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException("Uso incorreto. Use /LKGSave <nome>");
        }

        String nome = args[0];

        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            BlockPos posicao = player.getPosition();
            double x = posicao.getX();
            double y = posicao.getY();
            double z = posicao.getZ();

            File configDir = new File("config/LKGMod");
            if (!configDir.exists()) {
                configDir.mkdirs(); // Cria o diretório se ele não existir
            }

            File configFile = new File(configDir, "Coordenadas.txt");

            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(configFile, true);
                // Adicione as coordenadas da posição atual do jogador ao arquivo no formato "Nome;x:y:z"
                fileWriter.write(nome + ";" + x + ":" + y + ":" + z + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                throw new CommandException("Erro ao salvar o arquivo.");
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            MMModule.addMessage("Coordenadas salvas: " + nome + " em (" + x + ", " + y + ", " + z + ")");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Nível de permissão necessário para usar o comando (0 para todos)
    }
}