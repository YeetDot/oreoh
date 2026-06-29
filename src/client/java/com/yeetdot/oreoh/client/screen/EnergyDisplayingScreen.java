package com.yeetdot.oreoh.client.screen;

import com.yeetdot.oreoh.menu.AbstractEnergyContainerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public abstract class EnergyDisplayingScreen <Menu extends AbstractEnergyContainerMenu> extends AbstractContainerScreen<Menu> {
    private final int energyX;
    private final int energyY;
    private final int energyWidth;
    private final int energyHeight;
    private long displayedEnergy = -1;

    public EnergyDisplayingScreen(Menu menu, Inventory inventory, Component title, int x, int y, int width, int height) {
        super(menu, inventory, title);
        this.energyX = x;
        this.energyY = y;
        this.energyWidth = width;
        this.energyHeight = height;
    }

    @Override
    protected void init() {
        super.init();
        this.displayedEnergy = menu.getClientEnergyAmount();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        
        long actual = menu.getClientEnergyAmount();
        
        if (actual < 0) return;

        if (displayedEnergy < 0) {
            displayedEnergy = actual;
            return;
        }

        displayedEnergy += (long) ((actual - displayedEnergy) * 0.15);

        double diff = actual - displayedEnergy;
        
        double denom = Math.max(1.0, (double) actual);
        
        double relativeError = Math.abs(diff) / denom;

        if (relativeError < 1e-6 || Math.abs(diff) < 20) { // 0.0001% error threshold (tweakable)
            displayedEnergy = actual;
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, getBackgroundTexture(), xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        int scaledEnergy = this.menu.getScaledEnergy(displayedEnergy, energyHeight);
        if (scaledEnergy > 0) {
            int yOffset = energyHeight - scaledEnergy;
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    getEnergyDisplayTexture(),
                    xo + energyX,    
                    yo + energyY + yOffset,
                    0.0F,
                    0.0F + yOffset,
                    energyWidth,
                    scaledEnergy,
                    energyWidth,
                    energyHeight
            );
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;

        if (mouseX >= xo + energyX && mouseX < xo + energyX + energyWidth && mouseY >= yo + energyY && mouseY < yo + energyY + energyHeight) {
            
            long energyCapacity = this.menu.getClientEnergyCapacity();

            Component tooltipText = Component.literal(String.format("%,d / %,d E", displayedEnergy, energyCapacity));

            var a = ClientTooltipComponent.create(tooltipText.getVisualOrderText());

            graphics.tooltip(this.font, List.of(a), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }
    
    protected abstract Identifier getBackgroundTexture();
    
    protected abstract Identifier getEnergyDisplayTexture();
}
