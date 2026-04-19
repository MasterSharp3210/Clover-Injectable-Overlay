package com.itelcan3.cloverclient.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class CloverConfigGui extends GuiScreen {
    private final GuiScreen parent;

    public CloverConfigGui(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 40, "Return"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 50, "Change Color"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 25, "Increase Scale"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2, "Decrease Scale"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) this.mc.displayGuiScreen(parent);
        if (button.id == 1) {
            if (HUDOverlay.color == 0x55FF55) HUDOverlay.color = 0xFF5555;
            else if (HUDOverlay.color == 0xFF5555) HUDOverlay.color = 0x5555FF;
            else if (HUDOverlay.color == 0x5555FF) HUDOverlay.color = 0xFFFFFF;
            else HUDOverlay.color = 0x55FF55;
        }
        if (button.id == 2 && HUDOverlay.scale < 5.0f) HUDOverlay.scale += 0.1f;
        if (button.id == 3 && HUDOverlay.scale > 0.5f) HUDOverlay.scale -= 0.1f;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Clover Settings", this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}