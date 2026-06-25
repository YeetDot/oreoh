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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class AbstractMachineBlockEntity<I extends RecipeInput, T extends MachineRecipe<I>> extends BaseContainerBlockEntity implements WorldlyContainer {
    protected SimpleEnergyStorage energyStorage;
    protected NonNullList<ItemStack> items;
    protected int currentProgress = 0;
    protected int maxProgress = 200;
    protected int inputSlots;
    protected int outputSlots;
    private final RecipeManager.CachedCheck<I, T> quickCheck;
    protected final SideItemMode[] sideItemModes = new SideItemMode[6];
    protected final SideEnergyMode[] sideEnergyModes = new SideEnergyMode[6];
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case 0 -> AbstractMachineBlockEntity.this.currentProgress;
                case 1 -> AbstractMachineBlockEntity.this.maxProgress;
                case 2 -> (int) (AbstractMachineBlockEntity.this.energyStorage.getAmount() >> 32);
                case 3 -> (int) (AbstractMachineBlockEntity.this.energyStorage.getAmount() & 0xFFFFFFFFL);
                case 4 -> (int) (AbstractMachineBlockEntity.this.energyStorage.getCapacity() >> 32);
                case 5 -> (int) (AbstractMachineBlockEntity.this.energyStorage.getCapacity() & 0xFFFFFFFFL);
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case 0 -> AbstractMachineBlockEntity.this.currentProgress = value;
                case 1 -> AbstractMachineBlockEntity.this.maxProgress = value;
                case 2 -> AbstractMachineBlockEntity.this.energyStorage.amount =
                        (AbstractMachineBlockEntity.this.energyStorage.amount & 0x00000000FFFFFFFFL) | ((long) value << 32);
                case 3 -> AbstractMachineBlockEntity.this.energyStorage.amount =
                        (AbstractMachineBlockEntity.this.energyStorage.amount & 0xFFFFFFFF00000000L) | (value & 0xFFFFFFFFL);
                case 4,5 -> {
                    // Energy Capacity is final
                }
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    protected AbstractMachineBlockEntity(final BlockEntityType<?> type, final BlockPos worldPosition, final BlockState blockState, int inputSlots, int outputSlots, int capacity, int insertPerTick) {
        super(type, worldPosition, blockState);
        this.items = NonNullList.withSize(inputSlots + outputSlots, ItemStack.EMPTY);
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.energyStorage = new SimpleEnergyStorage(capacity, insertPerTick, 0);
        this.quickCheck = RecipeManager.createCheck(getRecipeType());
        Arrays.fill(sideItemModes, SideItemMode.NONE);
        Arrays.fill(sideEnergyModes, SideEnergyMode.IN_OUT);
        sideItemModes[Direction.WEST.ordinal()] = SideItemMode.INPUT;
        sideItemModes[Direction.EAST.ordinal()] = SideItemMode.OUTPUT;
        sideItemModes[Direction.NORTH.ordinal()] = SideItemMode.CATALYST;
    }

    public static <In extends RecipeInput, R extends MachineRecipe<In>> void serverTick(ServerLevel level, AbstractMachineBlockEntity<In, R> entity) {
        In recipeInput = entity.createRecipeInput();

        entity.getCurrentRecipe(level).ifPresent(recipe -> {
            if (recipe.value().getRecipeEnergy(recipeInput) <= entity.energyStorage.getAmount() && entity.canAcceptRecipeOutput(recipe)) {
                entity.maxProgress = recipe.value().getRecipeDuration(recipeInput);
                try (Transaction transaction = Transaction.openOuter()) {
                    entity.energyStorage.extract(recipe.value().getRecipeEnergy(recipeInput), transaction);
                    transaction.commit();
                }
                if (entity.currentProgress >= entity.maxProgress) {
                    entity.createOutputs(level, recipe);
                    entity.setChanged();
                } else {
                    entity.currentProgress++;
                    entity.setChanged();
                }
            }
        });
    }

    protected Optional<RecipeHolder<T>> getCurrentRecipe(ServerLevel level) {
        return this.quickCheck.getRecipeFor(createRecipeInput(), level);
    }

    protected abstract I createRecipeInput();

    protected void createOutputs(ServerLevel level, RecipeHolder<? extends T> recipeHolder) {
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
    private boolean canAcceptRecipeOutput(RecipeHolder<? extends T> recipe) {
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
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
        this.currentProgress = input.getShortOr("progress", (short) 0);
        this.maxProgress = input.getShortOr("maxProgress", (short) 200);
        energyStorage.amount = input.getLongOr("energy", 0L);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putShort("progress", (short) currentProgress);
        output.putShort("maxProgress", (short) maxProgress);
        output.putLong("energy", energyStorage.getAmount());
    }

    protected abstract RecipeType<T> getRecipeType();

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public int @NonNull [] getSlotsForFace(Direction direction) {
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
    public boolean canPlaceItemThroughFace(int slot, @NonNull ItemStack itemStack, @Nullable Direction direction) {
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
    public boolean canTakeItemThroughFace(int slot, @NonNull ItemStack itemStack, @NonNull Direction direction) {
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
    
    public SimpleEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    protected abstract int[] getInputSlots();
    
    protected abstract int[] getOutputSlots();
    
    protected abstract int[] getCatalystSlots();
}
