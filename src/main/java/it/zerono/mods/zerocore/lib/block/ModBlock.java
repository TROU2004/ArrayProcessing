package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.util.ItemHelper;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer.Builder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class ModBlock extends Block implements IGameObject {
      private final String _oreDictionaryName;

      @Nonnull
      public ItemStack createItemStack() {
            return ItemHelper.stackFrom((Block)this, 1, 0);
      }

      @Nonnull
      public ItemStack createItemStack(int amount) {
            return ItemHelper.stackFrom((Block)this, amount, 0);
      }

      @Nonnull
      public ItemStack createItemStack(int amount, int meta) {
            return ItemHelper.stackFrom((Block)this, amount, meta);
      }

      @Nullable
      public String getOreDictionaryName() {
            return this._oreDictionaryName;
      }

      public void onRegisterItemBlocks(@Nonnull IForgeRegistry registry) {
            registry.register((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
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
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
      }

      public int getMetaFromState(IBlockState state) {
            return 0;
      }

      protected ModBlock(@Nonnull String blockName, @Nonnull Material material, @Nullable String oreDictionaryName) {
            super(material);
            this._oreDictionaryName = oreDictionaryName;
            this.setRegistryName(blockName);
            this.setTranslationKey(this.getRegistryName().toString());
            this.setDefaultState(this.buildDefaultState(this.blockState.getBaseState()));
      }

      protected ModBlock(@Nonnull String blockName, @Nonnull Material material) {
            this(blockName, material, (String)null);
      }

      protected BlockStateContainer createBlockState() {
            Builder builder = new Builder(this);
            this.buildBlockState(builder);
            return builder.build();
      }

      protected void buildBlockState(@Nonnull Builder builder) {
      }

      @Nonnull
      protected IBlockState buildDefaultState(@Nonnull IBlockState state) {
            return state;
      }
}
