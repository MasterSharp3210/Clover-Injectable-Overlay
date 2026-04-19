package com.itelcan3.cloverclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDOverlay extends Gui {
    public static boolean enabled = true;
    public static int x = 4;
    public static int y = 4;
    public static int color = 0x55FF55;
    public static float scale = 1.0f;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL && enabled) {
            Minecraft mc = Minecraft.func_71410_x();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(scale, scale, scale);
            mc.field_71466_p.func_175063_a("Clover Client v1.0", x / scale, y / scale, color);
            GlStateManager.func_179121_F();
        }
    }
}