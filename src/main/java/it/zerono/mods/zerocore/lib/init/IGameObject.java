package it.zerono.mods.zerocore.lib.init;

import javax.annotation.Nonnull;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public interface IGameObject {
      void onRegisterItemBlocks(@Nonnull IForgeRegistry var1);

      void onRegisterOreDictionaryEntries();

      void onRegisterRecipes(@Nonnull IForgeRegistry var1);

      @SideOnly(Side.CLIENT)
      void onRegisterModels();
}
