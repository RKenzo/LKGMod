package com.mods.lkg.modules.messagemanager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class MMModule {
    public static void addMessage(String msg) {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
            ChatComponentText part1 = new ChatComponentText("[LKGMod]");
            part1.getChatStyle().setColor(EnumChatFormatting.GOLD);
            ChatComponentText part2 = new ChatComponentText(msg);
            part2.getChatStyle().setColor(EnumChatFormatting.GRAY);
            ChatComponentText message = new ChatComponentText("");
            message.appendSibling(part1);
            message.appendText(" ");
            message.appendSibling(part2);
            Minecraft.getMinecraft().thePlayer.addChatMessage((message));
        }
    }
}
