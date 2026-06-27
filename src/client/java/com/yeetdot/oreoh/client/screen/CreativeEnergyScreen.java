package com.yeetdot.oreoh.client.screen;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.menu.CreativeEnergyMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CreativeEnergyScreen extends AbstractContainerScreen<CreativeEnergyMenu> {
    public static final Identifier CREATIVE_ENERGY_LOCATION = OreOh.id("textures/gui/creative_energy.png");
    public static final Identifier ENERGY_SPRITE = OreOh.id("textures/gui/sprites/large_energy_sprite.png");
    
    public CreativeEnergyScreen(CreativeEnergyMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CREATIVE_ENERGY_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        int scaledEnergy = this.menu.getScaledEnergy(56);
        if (scaledEnergy > 0) {
            int yOffset = 56 - scaledEnergy;
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    ENERGY_SPRITE,        // Explicit file pointer
                    xo + 60,                       // Target Screen X position
                    yo + 15 + yOffset,              // Target Screen Y position (moves down as power drops)
                    0.0F,                           // Source U inside the PNG file
                    0.0F + yOffset,                 // Source V inside the PNG file (crops from the top down)
                    56,                              // Width of the box to render on the monitor
                    scaledEnergy,                   // Height of the box to render on the monitor
                    56,                             // TOTAL width of your individual PNG file asset
                    56                              // TOTAL height of your individual PNG file asset
            );
        }
    }

    @Override
    protected void extractTooltip(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;

        if (mouseX >= xo + 60 && mouseX < xo + 60 + 56 && mouseY >= yo + 15 && mouseY < yo + 15 + 56) {

            long energyAmount = this.menu.getClientEnergyAmount();
            long energyCapacity = this.menu.getClientEnergyCapacity();

            Component tooltipText = Component.literal(String.format("%,d / %,d E", energyAmount, energyCapacity));

            var a = ClientTooltipComponent.create(tooltipText.getVisualOrderText());

            graphics.tooltip(this.font, List.of(a), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }
}
