package it.zerono.mods.zerocore.lib.world;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public abstract class ModWorldGeneratorBase implements IWorldGenerator {
      protected final IWorldGenWhiteList _whiteList;
      protected boolean _useBlackList;

      public final void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
            if (null != this._whiteList && this._useBlackList != this._whiteList.shouldGenerateIn(world)) {
                  this.generateChunk(random, chunkX << 4, chunkZ << 4, world, chunkGenerator, chunkProvider);
            }
      }

      public void setBehavior(boolean useBlackListLogic) {
            this._useBlackList = useBlackListLogic;
      }

      protected abstract void generateChunk(Random var1, int var2, int var3, World var4, IChunkGenerator var5, IChunkProvider var6);

      protected ModWorldGeneratorBase(IWorldGenWhiteList whiteList, boolean useBlackListLogic) {
            this._whiteList = whiteList;
            this._useBlackList = useBlackListLogic;
      }
}
