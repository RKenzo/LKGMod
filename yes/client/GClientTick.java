package cc.polyfrost.example.modules.gsabmodule;

import cc.polyfrost.example.messageManager;
import cc.polyfrost.example.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.block.state.IBlockState;

public class GlassBreaker extends Module {

    private final int GLASS_BLOCK_ID = 95;
    private final int GLASS_META = 12;
    private final float SMOOTHNESS = 0.05f; // Ajuste conforme necessário

    @Override
    public void onEnable() {
        super.onEnable();
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        messageManager.addMessage("Breaker Ativado!");
        startBreakingGlass();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), false);
        messageManager.addMessage("Breaker Desativado!");
    }

    private void startBreakingGlass() {
        new Thread(() -> {
            while (isToggled()) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.theWorld != null && mc.thePlayer != null) {
                    BlockPos targetPos = findNearestGlassBlock();
                    if (targetPos != null) {
                        if (smoothLookAtBlock(targetPos)) {
                            mc.playerController.onPlayerDamageBlock(targetPos, mc.thePlayer.getHorizontalFacing());
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                        }
                    } else {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                    }
                }
                try {
                    Thread.sleep(100); // Ajuste o tempo conforme necessário
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private BlockPos findNearestGlassBlock() {
        Minecraft mc = Minecraft.getMinecraft();
        Vec3 playerPos = mc.thePlayer.getPositionVector();
        double reachDistance = mc.playerController.getBlockReachDistance();
        BlockPos nearestBlockPos = null;
        double nearestDistance = reachDistance;

        for (int x = - (int) reachDistance; x <= reachDistance; x++) {
            for (int y = - (int) reachDistance; y <= reachDistance; y++) {
                for (int z = - (int) reachDistance; z <= reachDistance; z++) {
                    BlockPos currentPos = new BlockPos(playerPos.xCoord + x, playerPos.yCoord + y, playerPos.zCoord + z);
                    IBlockState blockState = mc.theWorld.getBlockState(currentPos);
                    if (blockState.getBlock().getIdFromBlock(blockState.getBlock()) == GLASS_BLOCK_ID && blockState.getBlock().getMetaFromState(blockState) == GLASS_META) {
                        double distance = playerPos.distanceTo(new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()));
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestBlockPos = currentPos;
                        }
                    }
                }
            }
        }
        return nearestBlockPos;
    }

    private boolean smoothLookAtBlock(BlockPos blockPos) {
        Minecraft mc = Minecraft.getMinecraft();
        double dx = blockPos.getX() + 0.5 - mc.thePlayer.posX;
        double dy = blockPos.getY() + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double dz = blockPos.getZ() + 0.5 - mc.thePlayer.posZ;
        double distance = Math.sqrt(dx * dx + dz * dz);
        float targetYaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
        float targetPitch = (float) -(Math.atan2(dy, distance) * 180.0 / Math.PI);

        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;

        // Suavização das rotações
        float yawDiff = wrapDegrees(targetYaw - currentYaw);
        float pitchDiff = wrapDegrees(targetPitch - currentPitch);

        if (Math.abs(yawDiff) > SMOOTHNESS || Math.abs(pitchDiff) > SMOOTHNESS) {
            mc.thePlayer.rotationYaw = currentYaw + yawDiff * SMOOTHNESS;
            mc.thePlayer.rotationPitch = currentPitch + pitchDiff * SMOOTHNESS;
            return false; // Ainda não está mirando exatamente no bloco
        } else {
            mc.thePlayer.rotationYaw = targetYaw;
            mc.thePlayer.rotationPitch = targetPitch;
            return true; // Já está mirando exatamente no bloco
        }
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

    @Override
    public void onToggled() {
        super.onToggled();
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        if (isToggled()) {
            startBreakingGlass();
        }
    }
}
