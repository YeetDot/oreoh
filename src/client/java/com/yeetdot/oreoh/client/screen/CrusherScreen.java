package com.yeetdot.oreoh.client.screen;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.screen.CrusherMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class CrusherScreen extends AbstractContainerScreen<CrusherMenu> {
    private static final Identifier CRUSHER_LOCATION = OreOh.id("textures/gui/container/crusher.png");
    private static final Identifier PROGRESS_SPRITE = OreOh.id("textures/gui/container/crusher_progress.png");
    private static final Identifier ENERGY_SPRITE = OreOh.id("textures/gui/container/crusher_energy.png");
    
    public CrusherScreen(CrusherMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.width - this.imageWidth) / 2;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CRUSHER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        int scaledProgress = this.menu.getScaledProgress(18);
        if (scaledProgress > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, 18, 4, 0, 0, xo + 60, yo + 44, scaledProgress, 9);
        }
        int scaledEnergy = this.menu.getScaledEnergy(36);
        if (scaledEnergy > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENERGY_SPRITE, 18, 4, 0, 0, xo + 60, yo + 44, 4, scaledEnergy);
        }
    }
}
