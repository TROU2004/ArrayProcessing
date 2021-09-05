package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.lib.block.BlockMultiblockTieredPart;
import it.zerono.mods.zerocore.lib.init.IGameObject;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemMultiblockTieredPart extends ItemBlock implements IGameObject {
      public ItemMultiblockTieredPart(BlockMultiblockTieredPart block) {
            super(block);
            this.setHasSubtypes(true);
            this.setMaxDamage(0);
      }

      public String getTranslationKey(ItemStack stack) {
            BlockMultiblockTieredPart block = (BlockMultiblockTieredPart)this.getBlock();
            return super.getTranslationKey() + "." + ((IStringSerializable)block.getMultiblockDescriptor().getTierFromMeta(stack.getMetadata())).getName();
      }

      public int getMetadata(int meta) {
            return meta;
      }

      public void onRegisterItemBlocks(@Nonnull IForgeRegistry registry) {
      }

      public void onRegisterOreDictionaryEntries() {
      }

      public void onRegisterRecipes(@Nonnull IForgeRegistry registry) {
      }

      public void onRegisterModels() {
      }
}
