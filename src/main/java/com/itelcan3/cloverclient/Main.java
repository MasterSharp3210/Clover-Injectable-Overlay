package com.itelcan3.cloverclient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import com.itelcan3.cloverclient.client.HUDOverlay;
import com.itelcan3.cloverclient.client.EscMenuHandler;

public class Main {

    public static void inject() {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                startClient();
            }
        });
    }

    public static void startClient() {
        try {
            MinecraftForge.EVENT_BUS.register(new HUDOverlay());
            MinecraftForge.EVENT_BUS.register(new EscMenuHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}