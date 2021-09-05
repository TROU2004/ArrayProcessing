package it.zerono.mods.zerocore.lib.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeHelper {
      public static void addShapedRecipe(@Nonnull ItemStack output, @Nonnull Object... inputs) {
            addShapedRecipe((ResourceLocation)null, output, inputs);
      }

      public static void addShapedRecipe(@Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ResourceLocation name = output.getItem().getRegistryName();
            if (null == name) {
                  throw new IllegalArgumentException("Invalid output item registry name");
            } else {
                  addShapedRecipe(name, group, output, inputs);
            }
      }

      public static void addShapedRecipe(@Nonnull ResourceLocation name, @Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ShapedPrimer primer = CraftingHelper.parseShaped(inputs);
            ForgeRegistries.RECIPES.register((new ShapedRecipes(groupName(group), primer.width, primer.height, primer.input, output)).setRegistryName(name));
      }

      public static void addShapelessRecipe(@Nonnull ItemStack output, @Nonnull Object... inputs) {
            addShapelessRecipe((ResourceLocation)null, output, inputs);
      }

      public static void addShapelessRecipe(@Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ResourceLocation name = output.getItem().getRegistryName();
            if (null == name) {
                  throw new IllegalArgumentException("Invalid output item registry name");
            } else {
                  addShapelessRecipe(name, group, output, inputs);
            }
      }

      public static void addShapelessRecipe(@Nonnull ResourceLocation name, @Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            NonNullList ingredients = NonNullList.create();
            Object[] var5 = inputs;
            int var6 = inputs.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                  Object input = var5[var7];
                  ingredients.add(asIngredient(input));
            }

            ForgeRegistries.RECIPES.register((new ShapelessRecipes(groupName(group), output, ingredients)).setRegistryName(name));
      }

      public static void addShapedOreDictRecipe(@Nonnull ItemStack output, @Nonnull Object... inputs) {
            addShapedOreDictRecipe((ResourceLocation)null, output, inputs);
      }

      public static void addShapedOreDictRecipe(@Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ResourceLocation name = output.getItem().getRegistryName();
            if (null == name) {
                  throw new IllegalArgumentException("Invalid output item registry name");
            } else {
                  addShapedOreDictRecipe(name, group, output, inputs);
            }
      }

      public static void addShapedOreDictRecipe(@Nonnull ResourceLocation name, @Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ShapedPrimer primer = CraftingHelper.parseShaped(inputs);
            ForgeRegistries.RECIPES.register((new ShapedOreRecipe(groupResourceLocation(group), output, primer)).setRegistryName(name));
      }

      public static void addShapelessOreDictRecipe(@Nonnull ItemStack output, @Nonnull Object... inputs) {
            addShapelessOreDictRecipe((ResourceLocation)null, output, inputs);
      }

      public static void addShapelessOreDictRecipe(@Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ResourceLocation name = output.getItem().getRegistryName();
            if (null == name) {
                  throw new IllegalArgumentException("Invalid output item registry name");
            } else {
                  addShapelessOreDictRecipe(name, group, output, inputs);
            }
      }

      public static void addShapelessOreDictRecipe(@Nonnull ResourceLocation name, @Nullable ResourceLocation group, @Nonnull ItemStack output, @Nonnull Object... inputs) {
            ForgeRegistries.RECIPES.register((new ShapelessOreRecipe(groupResourceLocation(group), output, inputs)).setRegistryName(name));
      }

      @Nonnull
      private static String groupName(@Nullable ResourceLocation group) {
            return null == group ? "" : group.toString();
      }

      @Nonnull
      private static ResourceLocation groupResourceLocation(@Nullable ResourceLocation group) {
            return null == group ? new ResourceLocation("") : group;
      }

      private static Ingredient asIngredient(Object object) {
            if (object instanceof Item) {
                  return Ingredient.fromItems(new Item[]{(Item)object});
            } else if (object instanceof Block) {
                  return Ingredient.fromStacks(new ItemStack[]{new ItemStack((Block)object)});
            } else if (object instanceof ItemStack) {
                  return Ingredient.fromStacks(new ItemStack[]{(ItemStack)object});
            } else if (object instanceof String) {
                  return new OreIngredient((String)object);
            } else {
                  throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().toString() + " to an Ingredient!");
            }
      }
}
