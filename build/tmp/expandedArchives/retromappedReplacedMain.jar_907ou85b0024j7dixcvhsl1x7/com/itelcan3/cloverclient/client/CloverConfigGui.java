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
    public void func_73866_w_() {
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 2 + 40, "Return"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 2 - 50, "Change Color"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 2 - 25, "Increase Scale"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 2, "Decrease Scale"));
    }

    @Override
    protected void func_146284_a(GuiButton button) throws IOException {
        if (button.field_146127_k == 0) this.field_146297_k.func_147108_a(parent);
        if (button.field_146127_k == 1) {
            if (HUDOverlay.color == 0x55FF55) HUDOverlay.color = 0xFF5555;
            else if (HUDOverlay.color == 0xFF5555) HUDOverlay.color = 0x5555FF;
            else if (HUDOverlay.color == 0x5555FF) HUDOverlay.color = 0xFFFFFF;
            else HUDOverlay.color = 0x55FF55;
        }
        if (button.field_146127_k == 2 && HUDOverlay.scale < 5.0f) HUDOverlay.scale += 0.1f;
        if (button.field_146127_k == 3 && HUDOverlay.scale > 0.5f) HUDOverlay.scale -= 0.1f;
    }

    @Override
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, "Clover Settings", this.field_146294_l / 2, 20, 0xFFFFFF);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}