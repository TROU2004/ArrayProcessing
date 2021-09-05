package it.zerono.mods.zerocore.api.multiblock;

import net.minecraft.world.World;

public interface IMultiblockRegistry {
      void onPartAdded(World var1, IMultiblockPart var2);

      void onPartRemovedFromWorld(World var1, IMultiblockPart var2);

      void addDeadController(World var1, MultiblockControllerBase var2);

      void addDirtyController(World var1, MultiblockControllerBase var2);
}
