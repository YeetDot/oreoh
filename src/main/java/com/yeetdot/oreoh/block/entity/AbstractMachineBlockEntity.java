package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.recipe.MachineRecipe;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class AbstractMachineBlockEntity<I extends RecipeInput, R extends MachineRecipe<I>> extends AbstractEnergyContainerBlockEntity implements WorldlyContainer {
    protected NonNullList<ItemStack> items;
    protected int currentProgress = 0;
    protected int maxProgress = 200;
    protected final int outputSlots;
    private final RecipeManager.CachedCheck<I, R> quickCheck;
    protected final SideItemMode[] sideItemModes = new SideItemMode[6];
    protected final SideEnergyMode[] sideEnergyModes = new SideEnergyMode[6];
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case 0 -> AbstractMachineBlockEntity.this.currentProgress;
                case 1 -> AbstractMachineBlockEntity.this.maxProgress;
                case 2 -> {
                    long amount = AbstractMachineBlockEntity.this.getEnergyStorage().getAmount();
                    long capacity = AbstractMachineBlockEntity.this.getEnergyStorage().getCapacity();

                    if (capacity == 0) {
                        yield 0; 
                    } else {
                        yield (int) ((amount * 10000) / capacity); 
                    }
                }
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case 0 -> AbstractMachineBlockEntity.this.currentProgress = value;
                case 1 -> AbstractMachineBlockEntity.this.maxProgress = value;
                case 2 -> {}
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    protected AbstractMachineBlockEntity(final BlockEntityType<?> type, final BlockPos worldPosition, final BlockState blockState, int inputSlots, int outputSlots, long capacity) {
        super(type, worldPosition, blockState, capacity, 0);
        this.items = NonNullList.withSize(inputSlots + outputSlots, ItemStack.EMPTY);
        this.outputSlots = outputSlots;
        this.quickCheck = RecipeManager.createCheck(getRecipeType());
        Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        Arrays.fill(sideItemModes, SideItemMode.NONE);
        Arrays.fill(sideEnergyModes, SideEnergyMode.IN_OUT);
        Direction absoluteOutputSide = facing.getCounterClockWise();
        Direction absoluteCatalystSide = facing.getOpposite();
        Direction absoluteInputSide = facing.getClockWise();
        sideItemModes[absoluteInputSide.ordinal()] = SideItemMode.INPUT;
        sideItemModes[absoluteOutputSide.ordinal()] = SideItemMode.OUTPUT;
        sideItemModes[absoluteCatalystSide.ordinal()] = SideItemMode.CATALYST;

    }

    public static <I extends RecipeInput, R extends MachineRecipe<I>> void serverTick(
            ServerLevel level, 
            AbstractMachineBlockEntity<I, R> entity) 
    {
        entity.syncEnergyToOpenMenus(level);
        
        I recipeInput = entity.createRecipeInput();

        entity.getCurrentRecipe(level).ifPresent(recipe -> {
            if (recipe.value().getRecipeEnergy(recipeInput) <= entity.getEnergyStorage().getAmount() && entity.canAcceptRecipeOutput(recipe)) {
                entity.maxProgress = recipe.value().getRecipeDuration(recipeInput);
                try (Transaction transaction = Transaction.openOuter()) {
                    entity.getEnergyStorage().extract(recipe.value().getRecipeEnergy(recipeInput), transaction);
                    transaction.commit();
                }
                if (entity.currentProgress >= entity.maxProgress) {
                    entity.createOutputs(level, recipe);
                    entity.currentProgress = 0;
                    entity.setChanged();
                } else {
                    entity.currentProgress++;
                    entity.setChanged();
                }
            }
        });
    }

    protected Optional<RecipeHolder<R>> getCurrentRecipe(ServerLevel level) {
        return this.quickCheck.getRecipeFor(createRecipeInput(), level);
    }

    protected abstract I createRecipeInput();

    protected void createOutputs(ServerLevel level, RecipeHolder<? extends R> recipeHolder) {
        if (!canAcceptRecipeOutput(recipeHolder)) return;

        var recipeOutputs = recipeHolder.value().outputs();
        boolean changed = false;

        for (int i = recipeOutputs.size() - 1; i >= 0; i--) {
            var outputEntry = recipeOutputs.get(i);

            if (level.getRandom().nextFloat() < outputEntry.chance()) {
                int slotIndex = this.items.size() - this.outputSlots + i;

                ItemStack slotStack = items.get(slotIndex);
                ItemStack recipeStack = outputEntry.stack();

                if (slotStack.isEmpty()) {
                    items.set(slotIndex, recipeStack.copy());
                    changed = true;
                } else if (ItemStack.isSameItemSameComponents(slotStack, recipeStack)) {
                    slotStack.grow(recipeStack.getCount());
                    changed = true;
                }
            }
        }
        if (changed) setChanged(level, getBlockPos(), getBlockState());
    }

    // Removed 'static' and inventory parameter -> accesses 'this.items' and 'this.outputSlots' natively
    private boolean canAcceptRecipeOutput(RecipeHolder<? extends R> recipe) {
        List<ItemStack> recipeOutputs = recipe.value().assembleAll();

        if (recipeOutputs.size() > this.outputSlots) {
            return false;
        }

        for (int i = 0; i < recipeOutputs.size(); i++) {
            ItemStack recipeOutput = recipeOutputs.get(i);

            if (recipeOutput.isEmpty()) continue;

            int slotIndex = this.items.size() - this.outputSlots + i;
            ItemStack inventorySlot = this.items.get(slotIndex);

            boolean canPlaceInSlot = inventorySlot.isEmpty() || (
                    ItemStack.isSameItemSameComponents(inventorySlot, recipeOutput) &&
                            inventorySlot.getCount() + recipeOutput.getCount() <= inventorySlot.getMaxStackSize()
            );

            if (!canPlaceInSlot) {
                return false;
            }
        }
        
        return true;
    }


    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
        this.currentProgress = input.getShortOr("progress", (short) 0);
        this.maxProgress = input.getShortOr("maxProgress", (short) 200);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putShort("progress", (short) currentProgress);
        output.putShort("maxProgress", (short) maxProgress);
    }

    protected abstract RecipeType<R> getRecipeType();

    @Override
    protected  NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public int  [] getSlotsForFace(Direction direction) {
        SideItemMode mode = this.sideItemModes[direction.ordinal()];
        int[] inOutSlots = IntStream.concat(IntStream.of(getInputSlots()), IntStream.of(getOutputSlots())).toArray();
        return switch (mode) {
            case NONE -> new int[0];
            case INPUT -> getInputSlots();
            case OUTPUT -> getOutputSlots();
            case IN_OUT -> inOutSlots;
            case CATALYST -> getCatalystSlots();
            case ALL -> IntStream.concat(IntStream.of(inOutSlots), IntStream.of(getCatalystSlots())).toArray();
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot,  ItemStack itemStack, @Nullable Direction direction) {
        if (direction == null) return false;

        if (!this.canPlaceItem(slot, itemStack)) return false;
        
        SideItemMode mode = this.sideItemModes[direction.ordinal()];
        
        return switch(mode) {
            case INPUT, IN_OUT -> containsSlot(getInputSlots(), slot);
            case CATALYST -> containsSlot(getCatalystSlots(), slot);
            case ALL -> containsSlot(getInputSlots(), slot) || containsSlot(getCatalystSlots(), slot);
            default -> false;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot,  ItemStack itemStack,  Direction direction) {
        if (!this.canPlaceItem(slot, itemStack)) return false;

        SideItemMode mode = this.sideItemModes[direction.ordinal()];

        return switch(mode) {
            case OUTPUT, IN_OUT, ALL ->  containsSlot(getOutputSlots(), slot);
            default -> false;
        };
    }

    private boolean containsSlot(int[] slotArray, int targetSlot) {
        for (int slot : slotArray) {
            if (slot == targetSlot) return true;
        }
        return false;
    }

    protected abstract int[] getInputSlots();
    
    protected abstract int[] getOutputSlots();
    
    protected abstract int[] getCatalystSlots();

}
