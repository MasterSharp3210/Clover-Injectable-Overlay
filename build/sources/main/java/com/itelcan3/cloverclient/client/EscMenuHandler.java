package com.itelcan3.cloverclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EscMenuHandler {

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu) {
            int posX = event.gui.width - 105;
            int posY = event.gui.height - 25;
            event.buttonList.add(new GuiButton(9902, posX, posY, 100, 20, "Clover Options"));
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu && event.button.id == 9902) {
            Minecraft.getMinecraft().displayGuiScreen(new CloverConfigGui(event.gui));
        }
    }
}