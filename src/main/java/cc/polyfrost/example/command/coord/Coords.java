package cc.polyfrost.example.command.coord;

import net.minecraft.util.BlockPos;

public class Coords {
    private final String nome;
    private final BlockPos posicao;

    public Coords(String nome, BlockPos posicao) {
        this.nome = nome;
        this.posicao = posicao;
    }

    public String getNome() {
        return nome;
    }

    public BlockPos getPosicao() {
        return posicao;
    }
}