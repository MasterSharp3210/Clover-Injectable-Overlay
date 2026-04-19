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
            int posX = event.gui.field_146294_l - 105;
            int posY = event.gui.field_146295_m - 25;
            event.buttonList.add(new GuiButton(9902, posX, posY, 100, 20, "Clover Options"));
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiIngameMenu && event.button.field_146127_k == 9902) {
            Minecraft.func_71410_x().func_147108_a(new CloverConfigGui(event.gui));
        }
    }
}