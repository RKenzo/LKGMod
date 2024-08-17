package cc.polyfrost.example.command.coord;

import cc.polyfrost.example.modules.messagemanager.MMModule;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DelCoords extends CommandBase {
    @Override
    public String getCommandName() {
        return "LKGDel"; // Nome do comando
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/LKGDel <nome>"; // Descrição de como usar o comando
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException("Uso incorreto. Use /LKGDel <nome>");
        }

        String nomeParaRemover = args[0];
        File configDir = new File("config/LKGMod");
        File configFile = new File(configDir, "Coordenadas.txt");

        if (!configFile.exists()) {
            MMModule.addMessage("O arquivo de coordenadas não existe.");
            return;
        }

        List<String> linhas = new ArrayList<>();
        boolean encontrado = false; // Variável para verificar se o nome foi encontrado

        try {
            BufferedReader leitor = new BufferedReader(new FileReader(configFile));
            String linha;

            while ((linha = leitor.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 1 && !partes[0].equals(nomeParaRemover)) {
                    // Adicione apenas as linhas que não correspondem ao nome fornecido
                    linhas.add(linha);
                } else if (partes.length >= 1 && partes[0].equals(nomeParaRemover)) {
                    encontrado = true; // O nome foi encontrado no arquivo
                }
            }
            leitor.close();

            if (!encontrado) {
                MMModule.addMessage(nomeParaRemover + " não encontrado.");
                return;
            }

            // Reescreva o arquivo com as linhas restantes
            BufferedWriter escritor = new BufferedWriter(new FileWriter(configFile));
            for (String linhaRemanescente : linhas) {
                escritor.write(linhaRemanescente + "\n");
            }
            escritor.close();

            MMModule.addMessage("Coordenadas removidas: " + nomeParaRemover);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommandException("Erro ao remover as coordenadas.");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Nível de permissão necessário para usar o comando (0 para todos)
    }
}