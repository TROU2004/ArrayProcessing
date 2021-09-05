package it.zerono.mods.zerocore.lib.data.fixer;

import javax.annotation.Nonnull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;

@FunctionalInterface
public interface IGameObjectDataWalker {
      @Nonnull
      NBTTagCompound walkObject(@Nonnull IDataFixer var1, @Nonnull NBTTagCompound var2, int var3);
}
