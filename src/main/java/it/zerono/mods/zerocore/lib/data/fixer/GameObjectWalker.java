package it.zerono.mods.zerocore.lib.data.fixer;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public class GameObjectWalker implements IDataWalker {
      private final Map _walkers = Maps.newHashMap();

      public void addObjectWalker(@Nonnull ResourceLocation objectId, @Nonnull IGameObjectDataWalker objectWalker) {
            this._walkers.put(objectId.toString(), objectWalker);
      }

      public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int version) {
            if (null != compound && compound.hasKey("id")) {
                  IGameObjectDataWalker walker = (IGameObjectDataWalker)this._walkers.get(compound.getString("id"));
                  if (null != walker) {
                        compound = walker.walkObject(fixer, compound, version);
                  }
            }

            return compound;
      }
}
