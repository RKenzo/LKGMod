package cc.polyfrost.example.command.coord;

import cc.polyfrost.example.modules.messagemanager.MMModule;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCoords extends CommandBase {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final List<Coords> coordenadasAlvo = new ArrayList();
    private boolean looked = false;
    private int coordenadaAtual = -1; // Comece com -1 para indicar que nenhuma coordenada está ativa
    private boolean enabled = false;
    double x, y, z;

    @Override
    public String getCommandName() {
        return "LKGCoords"; // Nome do comando
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/LKGCoords"; // Descrição de como usar o comando
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        lerCoordenadas();
//        for (Coords coordenada : coordenadasAlvo) {
//            System.out.println(coordenada);
//        }
    }

    public void lerCoordenadas() {
        String diretorio = "config/LKGMod/";
        String nomeArquivo = "Coordenadas.txt";

        File arquivoCoordenadas = new File(diretorio, nomeArquivo);

        if (arquivoCoordenadas.exists()) {
            BufferedReader leitor = null;

            try {
                leitor = new BufferedReader(new FileReader(arquivoCoordenadas));
                String linha;

                while ((linha = leitor.readLine()) != null) {
                    String[] partes = linha.split(";");
                    if (partes.length == 2) {
                        String nome = partes[0];
                        String[] coordenadasStr = partes[1].split(":");
                        if (coordenadasStr.length == 3) {
                            double x = Double.parseDouble(coordenadasStr[0]);
                            double y = Double.parseDouble(coordenadasStr[1]);
                            double z = Double.parseDouble(coordenadasStr[2]);

                            // Crie uma Coordenada com as coordenadas lidas e adicione à lista
//                            Coords coordenada = new Coords(nome, new BlockPos(x, y, z));
//                            coordenadasAlvo.add(coordenada);

                            MMModule.addMessage("Coordenadas: Nome = " + nome + ", X = " + x + ", Y = " + y + ", Z = " + z);
                        } else {
                            System.err.println("Formato de coordenadas inválido na linha: " + linha);
                        }
                    } else {
                        System.err.println("Formato de linha inválido: " + linha);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (leitor != null) {
                    try {
                        leitor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("O arquivo de coordenadas não existe.");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Nível de permissão necessário para usar o comando (0 para todos)
    }
}

