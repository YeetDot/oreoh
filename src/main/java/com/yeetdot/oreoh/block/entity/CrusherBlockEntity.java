package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.recipe.CrusherRecipe;
import com.yeetdot.oreoh.recipe.MachineRecipeInput;
import com.yeetdot.oreoh.recipe.ModRecipes;
import com.yeetdot.oreoh.menu.CrusherMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CrusherBlockEntity extends AbstractMachineBlockEntity<MachineRecipeInput, CrusherRecipe> {
    public CrusherBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.CRUSHER_BLOCK_ENTITY, worldPosition, blockState, 1, 2, 20000, 400);
    }

    @Override
    protected MachineRecipeInput createRecipeInput() {
        return new MachineRecipeInput(this.getItems());
    }
    

    @Override
    protected RecipeType<CrusherRecipe> getRecipeType() {
        return ModRecipes.CRUSHER_TYPE;
    }

    @Override
    protected int[] getInputSlots() {
        return new int[] {0};
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[] {1, 2};
    }

    @Override
    protected int[] getCatalystSlots() {
        return new int[0];
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.translatable("container.oreoh.crusher");
    }

    @Override
    protected @NonNull AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory) {
        return new CrusherMenu(containerId, inventory, this, dataAccess, this.getBlockPos());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory, @NonNull Player player) {
        return new CrusherMenu(containerId, inventory, this, dataAccess, this.getBlockPos());
    }
}
