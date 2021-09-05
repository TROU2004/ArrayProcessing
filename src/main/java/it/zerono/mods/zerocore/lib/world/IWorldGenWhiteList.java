package it.zerono.mods.zerocore.lib.world;

import net.minecraft.world.World;

public interface IWorldGenWhiteList {
      boolean shouldGenerateIn(World var1);

      boolean shouldGenerateIn(int var1);

      void whiteListDimension(int var1);

      void whiteListDimensions(int[] var1);

      void clearWhiteList();
}
