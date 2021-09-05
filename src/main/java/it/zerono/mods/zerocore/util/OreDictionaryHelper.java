package it.zerono.mods.zerocore.util;

import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDictionaryHelper {
      public static String[] getOreNames(ItemStack stack) {
            if (ItemHelper.stackIsEmpty(stack)) {
                  return null;
            } else {
                  int[] oreIDs = OreDictionary.getOreIDs(stack);
                  int count;
                  if (oreIDs != null && (count = oreIDs.length) >= 1) {
                        String[] names = new String[count];

                        for(int i = 0; i < count; ++i) {
                              names[i] = OreDictionary.getOreName(oreIDs[i]);
                        }

                        return names;
                  } else {
                        return null;
                  }
            }
      }

      public static String[] getOreNames(@Nonnull IBlockState state) {
            return getOreNames(ItemHelper.stackFrom((IBlockState)state, 1));
      }

      public static String getFirstOreName(ItemStack stack) {
            String[] names = ItemHelper.stackIsValid(stack) ? getOreNames(stack) : null;
            return null != names && names.length > 0 ? names[0] : "";
      }

      public static String getFirstOreName(@Nonnull IBlockState state) {
            return getFirstOreName(ItemHelper.stackFrom((IBlockState)state, 1));
      }

      public static ItemStack getOre(String name) {
            return !doesOreNameExist(name) ? null : ItemHandlerHelper.copyStackWithSize((ItemStack)OreDictionary.getOres(name).get(0), 1);
      }

      public static boolean doesOreNameExist(String name) {
            return OreDictionary.doesOreNameExist(name);
      }

      private OreDictionaryHelper() {
      }
}
