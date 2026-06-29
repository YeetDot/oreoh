package com.yeetdot.oreoh.client.screen;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.menu.CrusherMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CrusherScreen extends EnergyDisplayingScreen<CrusherMenu> {
    public static final Identifier CRUSHER_LOCATION = OreOh.id("textures/gui/container/crusher.png");
    public static final Identifier PROGRESS_SPRITE = OreOh.id("textures/gui/container/crusher_progress.png");
    public static final Identifier ENERGY_SPRITE = OreOh.id("textures/gui/sprites/energy_sprite.png");
    
    public CrusherScreen(CrusherMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 150, 20, 4, 36);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        int scaledProgress = this.menu.getScaledProgress(18);
        if (scaledProgress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, PROGRESS_SPRITE, xo + 60, yo + 44, 0, 0, scaledProgress, 0, 18, 4);
        }
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return CRUSHER_LOCATION;
    }

    @Override
    protected Identifier getEnergyDisplayTexture() {
        return ENERGY_SPRITE;
    }
}
