package it.zerono.mods.zerocore.lib.block;

import com.google.common.collect.UnmodifiableIterator;
import it.zerono.mods.zerocore.api.multiblock.tier.IMultiblockDescriptorProvider;
import it.zerono.mods.zerocore.api.multiblock.tier.MultiblockDescriptor;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import it.zerono.mods.zerocore.lib.item.ItemMultiblockTieredPart;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer.Builder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockMultiblockTieredPart extends BlockMultiblockPart implements IMultiblockDescriptorProvider {
      private List _subBlocks;
      private static IMultiblockTieredPartType s_preDescriptorProvider;

      public BlockMultiblockTieredPart(@Nonnull Enum type, @Nonnull String blockName, @Nonnull Material material) {
            super(type, blockName, material);
            s_preDescriptorProvider = null;
      }

      public static void preInitDescriptorProvider(@Nonnull IMultiblockTieredPartType provider) {
            s_preDescriptorProvider = provider;
      }

      @Nonnull
      public MultiblockDescriptor getMultiblockDescriptor() {
            IMultiblockTieredPartType provider = null != this._partType ? (IMultiblockTieredPartType)this._partType : s_preDescriptorProvider;
            MultiblockDescriptor descriptor = provider.getMultiblockDescriptor();
            return descriptor;
      }

      public void onRegisterItemBlocks(@Nonnull IForgeRegistry registry) {
            registry.register((new ItemMultiblockTieredPart(this)).setRegistryName(this.getRegistryName()));
      }

      @SideOnly(Side.CLIENT)
      public void onRegisterModels() {
            Item item = Item.getItemFromBlock(this);
            ResourceLocation location = this.getRegistryName();
            IBlockState defaultState = this.getDefaultState();
            StringBuilder sb = new StringBuilder(32);
            boolean first = true;

            for(UnmodifiableIterator var6 = defaultState.getProperties().keySet().iterator(); var6.hasNext(); first = false) {
                  IProperty prop = (IProperty)var6.next();
                  String name = prop.getName();
                  if (!first) {
                        sb.append(',');
                  }

                  if ("tier".equals(name)) {
                        sb.append("tier=%s");
                  } else {
                        sb.append(name);
                        sb.append('=');
                        sb.append(defaultState.getValue(prop));
                  }
            }

            String mapFormat = sb.toString();
            EnumSet activeTiers = this.getMultiblockDescriptor().getActiveTiers();
            Iterator var12 = activeTiers.iterator();

            while(var12.hasNext()) {
                  Enum tier = (Enum)var12.next();
                  ModelLoader.setCustomModelResourceLocation(item, ((IPropertyValue)tier).toMeta(), new ModelResourceLocation(location, String.format(mapFormat, ((IStringSerializable)tier).getName())));
            }

      }

      @Nonnull
      public Enum getTierFromState(@Nonnull IBlockState state) {
            return (Enum)state.getValue(this.getMultiblockDescriptor().getTierProperty());
      }

      public int getMetaFromState(IBlockState state) {
            return ((IPropertyValue)((Enum)state.getValue(this.getMultiblockDescriptor().getTierProperty()))).toMeta();
      }

      public IBlockState getStateFromMeta(int meta) {
            MultiblockDescriptor descriptor = this.getMultiblockDescriptor();
            return super.getStateFromMeta(meta).withProperty(descriptor.getTierProperty(), descriptor.getTierFromMeta(meta));
      }

      public int damageDropped(IBlockState state) {
            return ((IPropertyValue)((Enum)state.getValue(this.getMultiblockDescriptor().getTierProperty()))).toMeta();
      }

      @Nonnull
      public ItemStack createItemStack(@Nonnull Enum tier, int amount) {
            return new ItemStack(this, amount, ((IPropertyValue)tier).toMeta());
      }

      @SideOnly(Side.CLIENT)
      public void getSubBlocks(CreativeTabs tab, NonNullList list) {
            if (null == this._subBlocks) {
                  EnumSet tiers = this.getMultiblockDescriptor().getActiveTiers();
                  this._subBlocks = new ArrayList();
                  Iterator var4 = tiers.iterator();

                  while(var4.hasNext()) {
                        Enum tier = (Enum)var4.next();
                        this._subBlocks.add(this.createItemStack(tier, 1));
                  }
            }

            list.addAll(this._subBlocks);
      }

      protected void buildBlockState(@Nonnull Builder builder) {
            super.buildBlockState(builder);
            builder.add(new IProperty[]{this.getMultiblockDescriptor().getTierProperty()});
      }

      @Nonnull
      protected IBlockState buildDefaultState(@Nonnull IBlockState state) {
            MultiblockDescriptor descriptor = this.getMultiblockDescriptor();
            return super.buildDefaultState(state).withProperty(descriptor.getTierProperty(), descriptor.getDefaultTier());
      }
}
