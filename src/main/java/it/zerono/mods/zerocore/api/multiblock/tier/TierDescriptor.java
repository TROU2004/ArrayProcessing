package it.zerono.mods.zerocore.api.multiblock.tier;

import javax.annotation.Nonnull;

public abstract class TierDescriptor {
      public final Enum Tier;
      public final int MaxSizeX;
      public final int MaxSizeY;
      public final int MaxSizeZ;

      protected TierDescriptor(@Nonnull Enum tier, int maxX, int maxY, int maxZ) {
            this.Tier = tier;
            this.MaxSizeX = maxX;
            this.MaxSizeY = maxY;
            this.MaxSizeZ = maxZ;
      }
}
