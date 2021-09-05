package it.zerono.mods.zerocore.lib.world;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import net.minecraft.world.World;

public class WorldGenWhiteList implements IWorldGenWhiteList {
      private final Set _dimensionsWhiteList = new CopyOnWriteArraySet();

      public boolean shouldGenerateIn(World world) {
            return this._dimensionsWhiteList.contains(world.provider.getDimension());
      }

      public boolean shouldGenerateIn(int dimensionId) {
            return this._dimensionsWhiteList.contains(dimensionId);
      }

      public void whiteListDimension(int id) {
            this._dimensionsWhiteList.add(id);
      }

      public void whiteListDimensions(int[] ids) {
            for(int i = 0; i < ids.length; ++i) {
                  this._dimensionsWhiteList.add(ids[i]);
            }

      }

      public void clearWhiteList() {
            this._dimensionsWhiteList.clear();
      }
}
