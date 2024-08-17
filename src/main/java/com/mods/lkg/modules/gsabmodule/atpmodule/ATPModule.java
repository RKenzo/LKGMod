package com.mods.lkg.modules.gsabmodule.atpmodule;

import com.mods.lkg.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ATPModule extends Module {
    private final Minecraft mc = Minecraft.getMinecraft();
    private double x, y, z;
    private boolean enabled = false;

    //    public ATPModule() {}
    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public void onDisable() {
        enabled = false;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (enabled) {
                EntityPlayerSP player = mc.thePlayer;
                float pitch = calculatePitchToBlock(x, y, z);
                float yaw = calculateYawToBlock(x, z);
                player.rotationYaw = yaw;
                player.rotationPitch = pitch;
            }
        }
    }

    private float calculatePitchToBlock(double x, double y, double z) {
        if (mc.thePlayer != null) {
            double deltaX = x + 0.5 - mc.thePlayer.posX;
            double deltaY = y + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
            double deltaZ = z + 0.5 - mc.thePlayer.posZ;
            double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            return (float) Math.toDegrees(Math.atan2(-deltaY, horizontalDistance));
        }
        return 0.0f;
    }

    private float calculateYawToBlock(double x, double z) {
        if (mc.thePlayer != null) {
            double deltaX = x + 0.5 - mc.thePlayer.posX;
            double deltaZ = z + 0.5 - mc.thePlayer.posZ;
            return (float) Math.toDegrees(Math.atan2(-deltaX, deltaZ));
        }
        return 0.0f;
    }
}
