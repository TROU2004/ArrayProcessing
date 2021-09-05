package it.zerono.mods.zerocore.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class ItemHelper {
      @Nonnull
      public static ItemStack stackFrom(@Nonnull Item item, int amount, int meta) {
            return new ItemStack(item, amount, meta, (NBTTagCompound)null);
      }

      @Nonnull
      public static ItemStack stackFrom(@Nonnull Item item, int amount, int meta, @Nullable NBTTagCompound nbt) {
            return new ItemStack(item, amount, meta, nbt);
      }

      @Nonnull
      public static ItemStack stackFrom(@Nonnull Block block, int amount, int meta) {
            return new ItemStack(block, amount, meta);
      }

      @Nonnull
      public static ItemStack stackFrom(@Nonnull IBlockState state, int amount) {
            Block block = state.getBlock();
            Item item = Item.getItemFromBlock(block);
            return new ItemStack(item, amount, item.getHasSubtypes() ? block.getMetaFromState(state) : 0);
      }

      @Nonnull
      public static ItemStack stackFrom(@Nonnull NBTTagCompound nbt) {
            return new ItemStack(nbt);
      }

      @Nonnull
      public static ItemStack stackFrom(@Nonnull ItemStack stack) {
            return stack.copy();
      }

      @Nullable
      public static ItemStack stackFrom(@Nullable ItemStack stack, int amount) {
            ItemStack newStack = stackFrom(stack);
            if (stackIsEmpty(newStack)) {
                  return stackEmpty();
            } else {
                  stackSetSize(newStack, amount);
                  return newStack;
            }
      }

      public static boolean stackIsValid(@Nonnull ItemStack stack) {
            return !stack.isEmpty();
      }

      public static boolean stackIsEmpty(@Nonnull ItemStack stack) {
            return stack.isEmpty();
      }

      public static int stackGetSize(@Nonnull ItemStack stack) {
            return stack.getCount();
      }

      @Nonnull
      public static ItemStack stackSetSize(@Nonnull ItemStack stack, int amount) {
            if (amount <= 0) {
                  stack.setCount(0);
                  return stackEmpty();
            } else {
                  stack.setCount(amount);
                  return stack;
            }
      }

      @Nonnull
      public static ItemStack stackAdd(@Nonnull ItemStack stack, int amount) {
            stack.grow(amount);
            return stack;
      }

      @Nonnull
      public static ItemStack stackEmpty(@Nonnull ItemStack stack) {
            stack.setCount(0);
            return stack;
      }

      @Nonnull
      public static ItemStack stackEmpty() {
            return ItemStack.EMPTY;
      }

      private ItemHelper() {
      }
}
