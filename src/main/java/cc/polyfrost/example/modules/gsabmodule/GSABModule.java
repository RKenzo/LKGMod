package cc.polyfrost.example.modules.gsabmodule;

import cc.polyfrost.example.config.Config;
import cc.polyfrost.example.modules.gsabmodule.atpmodule.ATPModule;
import cc.polyfrost.example.modules.messagemanager.MMModule;
import cc.polyfrost.example.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GSABModule extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<String, Integer> blockIDs = new HashMap<>();
    private BlockPos lastBlockPos = null;
    private ATPModule atpModule = new ATPModule();

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.theWorld == null || mc.thePlayer == null) return;
        MMModule.addMessage("Breaker Ativado!");

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
        initializeBlockIDs();
        startBreakingGlass();
    }


    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
               if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
                   atpModule.toggle();
                   System.out.println(atpModule.isToggled());
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.theWorld == null || mc.thePlayer == null) return;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        MMModule.addMessage("Breaker Desativado!");
        blockIDs.clear();
    }

    private void initializeBlockIDs() {
        if (Config.gemRuby) {
            addBlockIDs("14");
        }
        if (Config.gemAmber) {
            addBlockIDs("1");
        }
        if (Config.gemAmethyst) {
            addBlockIDs("10");
        }
        if (Config.gemJade) {
            addBlockIDs("5");
        }
        if (Config.gemSapphire) {
            addBlockIDs("3");
        }
        if (Config.gemTopaz) {
            addBlockIDs("4");
        }
        if (Config.gemJasper) {
            addBlockIDs("2");
        }
        if (Config.gemOpal) {
            addBlockIDs("0");
        }
        if (Config.gemOnyx) {
            addBlockIDs("15");
        }
        if (Config.gemAquamarine) {
            addBlockIDs("11");
        }
        if (Config.gemCitrine) {
            addBlockIDs("12");
        }
        if (Config.gemPeridot) {
            addBlockIDs("13");
        }
    }

    private void addBlockIDs(String meta) {
        blockIDs.put("95:" + meta, Block.getIdFromBlock(Blocks.stained_glass));
        if (Config.isPanelOn) {
            blockIDs.put("160:" + meta, Block.getIdFromBlock(Blocks.stained_glass_pane));
        }
    }


    private static final Random RANDOM = new Random();

    public static int getRandomInt(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    private void startBreakingGlass() {
        new Thread(() -> {
            while (isToggled()) {
                if (mc.theWorld != null && mc.thePlayer != null) {
                    BlockPos targetPos = findNearestGlassBlock();
                    if (targetPos != null) {
                        lookAtBlock(targetPos);
                        lastBlockPos = targetPos;
                    }
                }
                try {
                    Thread.sleep(getRandomInt(10, 80)); // Ajuste o tempo conforme necessário
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private BlockPos findNearestGlassBlock() {
        Vec3 playerPos = mc.thePlayer.getPositionVector();
        double reachDistance = mc.playerController.getBlockReachDistance() + Config.radiusDefault;
        BlockPos nearestBlockPos = null;
        double nearestDistance = Double.MAX_VALUE;

        // Lista para armazenar os blocos de vidro encontrados
        Map<BlockPos, Double> glassBlocks = new HashMap<>();

        for (int x = -(int) reachDistance; x <= reachDistance; x++) {
            for (int y = -(int) reachDistance; y <= reachDistance; y++) {
                for (int z = -(int) reachDistance; z <= reachDistance; z++) {
                    BlockPos currentPos = new BlockPos(playerPos.xCoord + x, playerPos.yCoord + y, playerPos.zCoord + z);
                    IBlockState blockState = mc.theWorld.getBlockState(currentPos);
                    int blockId = Block.getIdFromBlock(blockState.getBlock());
                    int meta = blockState.getBlock().getMetaFromState(blockState);

                    if (blockIDs.containsKey(blockId + ":" + meta)) {
                        double[] closestPoint = findClosestPointInBlock(currentPos);
                        double distance = getDistance(playerPos.xCoord, playerPos.yCoord + mc.thePlayer.getEyeHeight(), playerPos.zCoord,
                                closestPoint[0], closestPoint[1], closestPoint[2]);

                        // Verifica se o bloco está dentro do alcance e do campo de visão
                        float targetYaw = calculateYawToPoint(closestPoint[0], closestPoint[2]);
                        float targetPitch = calculatePitchToPoint(closestPoint[0], closestPoint[1], closestPoint[2]);

                        if (distance <= reachDistance && isBlockInFieldOfView(targetYaw, targetPitch)) {
                            glassBlocks.put(currentPos, distance); // Adiciona o bloco à lista
                        }
                    }
                }
            }
        }

        // Verifica se há um bloco anterior para considerar
        if (lastBlockPos != null) {
            // Verifica se o bloco mais próximo da mira ou o bloco mais próximo ao bloco anterior
            BlockPos closestToLastBlock = glassBlocks.keySet().stream()
                    .min((p1, p2) -> Double.compare(getDistance(p1.getX(), p1.getY(), p1.getZ(), lastBlockPos.getX(), lastBlockPos.getY(), lastBlockPos.getZ()),
                            getDistance(p2.getX(), p2.getY(), p2.getZ(), lastBlockPos.getX(), lastBlockPos.getY(), lastBlockPos.getZ())))
                    .orElse(null);

            if (closestToLastBlock != null) {
                nearestBlockPos = closestToLastBlock;
                nearestDistance = glassBlocks.get(nearestBlockPos);
            }
        } else {
            // Caso não haja bloco anterior, apenas encontre o mais próximo da mira
            nearestBlockPos = glassBlocks.entrySet().stream()
                    .min((e1, e2) -> Double.compare(e1.getValue(), e2.getValue()))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

        return nearestBlockPos;
    }


    private double[] findClosestPointInBlock(BlockPos blockPos) {
        double[] closestPoint = new double[3];
        double closestDistance = Double.MAX_VALUE;

        double playerX = mc.thePlayer.posX;
        double playerY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double playerZ = mc.thePlayer.posZ;

        for (double x = blockPos.getX(); x <= blockPos.getX() + 1; x += 1) {
            for (double y = blockPos.getY(); y <= blockPos.getY() + 1; y += 1) {
                for (double z = blockPos.getZ(); z <= blockPos.getZ() + 1; z += 1) {
                    double cornerX = MathHelper.clamp_double(x, blockPos.getX(), blockPos.getX() + 1);
                    double cornerY = MathHelper.clamp_double(y, blockPos.getY(), blockPos.getY() + 1);
                    double cornerZ = MathHelper.clamp_double(z, blockPos.getZ(), blockPos.getZ() + 1);

                    double distance = getDistance(playerX, playerY, playerZ, cornerX, cornerY, cornerZ);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPoint[0] = cornerX;
                        closestPoint[1] = cornerY;
                        closestPoint[2] = cornerZ;
                    }
                }
            }
        }

        return closestPoint;
    }


    private double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double deltaZ = z2 - z1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    private boolean isBlockInFieldOfView(float targetYaw, float targetPitch) {
        if (mc.thePlayer != null) {
            float playerYaw = mc.thePlayer.rotationYaw;
            float playerPitch = mc.thePlayer.rotationPitch;

            // Defina um ângulo limite para o campo de visão (ajuste conforme necessário)
            float fovLimit = Config.fovLimit; // Por exemplo, 30 graus

            // Calcule a diferença entre os ângulos do jogador e o alvo
            float yawDifference = Math.abs(MathHelper.wrapAngleTo180_float(targetYaw - playerYaw));
            float pitchDifference = Math.abs(targetPitch - playerPitch);

            // Verifique se os ângulos estão dentro do limite de campo de visão
            return yawDifference <= fovLimit && pitchDifference <= fovLimit;
        }
        return false;
    }

    private void lookAtBlock(BlockPos blockPos) {
        EntityPlayerSP player = mc.thePlayer;

        double centerX = blockPos.getX() + 0.5;
        double centerY = blockPos.getY() + 0.5;
        double centerZ = blockPos.getZ() + 0.5;

        float pitch = calculatePitchToPoint(centerX, centerY, centerZ);
        float yaw = calculateYawToPoint(centerX, centerZ);

        float prevYaw = player.rotationYaw;
        float prevPitch = player.rotationPitch;

        float yawDiff = wrapDegrees(yaw - prevYaw);
        float pitchDiff = wrapDegrees(pitch - prevPitch);

        float smoothness = Config.smoothinessDefault;

        player.rotationYaw = prevYaw + yawDiff * smoothness;
        player.rotationPitch = prevPitch + pitchDiff * smoothness;
    }


    private float wrapDegrees(float degrees) {
        degrees %= 360.0f;
        if (degrees >= 180.0f) {
            degrees -= 360.0f;
        }
        if (degrees < -180.0f) {
            degrees += 360.0f;
        }
        return degrees;
    }

    private float calculatePitchToPoint(double x, double y, double z) {
        if (mc.thePlayer != null) {
            double deltaX = x - mc.thePlayer.posX;
            double deltaY = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
            double deltaZ = z - mc.thePlayer.posZ;
            double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            return (float) Math.toDegrees(Math.atan2(-deltaY, horizontalDistance));
        }
        return 0.0f;
    }

    private float calculateYawToPoint(double x, double z) {
        if (mc.thePlayer != null) {
            double deltaX = x - mc.thePlayer.posX;
            double deltaZ = z - mc.thePlayer.posZ;
            return (float) Math.toDegrees(Math.atan2(-deltaX, deltaZ));
        }
        return 0.0f;
    }
}
