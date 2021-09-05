package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.util.ItemHelper;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class ModItem extends Item implements IGameObject {
      private final String _oreDictionaryName;

      @Nonnull
      public ItemStack createItemStack() {
            return ItemHelper.stackFrom((Item)this, 1, 0);
      }

      @Nonnull
      public ItemStack createItemStack(int amount) {
            return ItemHelper.stackFrom((Item)this, amount, 0);
      }

      @Nonnull
      public ItemStack createItemStack(int amount, int meta) {
            return ItemHelper.stackFrom((Item)this, amount, meta);
      }

      @Nullable
      public String getOreDictionaryName() {
            return this._oreDictionaryName;
      }

      public void onRegisterItemBlocks(@Nonnull IForgeRegistry registry) {
      }

      public void onRegisterOreDictionaryEntries() {
            String name = this.getOreDictionaryName();
            if (!Strings.isNullOrEmpty(name)) {
                  OreDictionary.registerOre(name, this);
            }

      }

      public void onRegisterRecipes(@Nonnull IForgeRegistry registry) {
      }

      @SideOnly(Side.CLIENT)
      public void onRegisterModels() {
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
      }

      public int getMetadata(int metadata) {
            return metadata;
      }

      protected ModItem(@Nonnull String itemName, @Nullable String oreDictionaryName) {
            this._oreDictionaryName = oreDictionaryName;
            this.setRegistryName(itemName);
            this.setTranslationKey(this.getRegistryName().toString());
      }

      protected ModItem(@Nonnull String itemName) {
            this(itemName, (String)null);
      }
}
