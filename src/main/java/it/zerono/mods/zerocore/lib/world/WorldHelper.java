package it.zerono.mods.zerocore.lib.world;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

public final class WorldHelper {
      public static boolean calledByLogicalServer(World world) {
            return !world.isRemote;
      }

      public static boolean calledByLogicalClient(World world) {
            return world.isRemote;
      }

      public static String getWorldSideName(World world) {
            return world.isRemote ? "CLIENT" : "SERVER";
      }

      public static boolean isEntityInRange(Entity entity, double x, double y, double z, double range) {
            return entity.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) < range * range;
      }

      public static boolean isEntityInRange(Entity entity, BlockPos position, double range) {
            return entity.getDistanceSq((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D) < range * range;
      }

      public static void spawnVanillaParticles(World world, EnumParticleTypes particle, int minCount, int maxCount, int x, int y, int z, int offsetX, int offsetY, int offsetZ) {
            if (null != world) {
                  Random rand = world.rand;
                  int howMany = MathHelper.getInt(rand, minCount, maxCount);
                  double px1 = (double)(x - offsetX) + 0.5D;
                  double px2 = (double)(x + offsetX) + 0.5D;
                  double py1 = (double)y;
                  double py2 = (double)(y + offsetY);
                  double pz1 = (double)(z - offsetZ) + 0.5D;
                  double pz2 = (double)(z + offsetZ) + 0.5D;
                  double motionX;
                  double motionY;
                  double motionZ;
                  double pX;
                  double pY;
                  double pZ;
                  if (world instanceof WorldServer) {
                        WorldServer ws = (WorldServer)world;
                        motionX = rand.nextGaussian() * 0.02D;
                        motionY = rand.nextGaussian() * 0.02D;
                        motionZ = rand.nextGaussian() * 0.02D;
                        pX = MathHelper.nextDouble(rand, px1, px2);
                        pY = MathHelper.nextDouble(rand, py1, py2);
                        pZ = MathHelper.nextDouble(rand, pz1, pz2);
                        ws.spawnParticle(particle, pX, pY, pZ, howMany, motionX, motionY, motionZ, rand.nextGaussian() * 0.02D, new int[0]);
                  } else {
                        for(int i = 0; i < howMany; ++i) {
                              motionX = rand.nextGaussian() * 0.02D;
                              motionY = rand.nextGaussian() * 0.02D;
                              motionZ = rand.nextGaussian() * 0.02D;
                              pX = MathHelper.nextDouble(rand, px1, px2);
                              pY = MathHelper.nextDouble(rand, py1, py2);
                              pZ = MathHelper.nextDouble(rand, pz1, pz2);
                              world.spawnParticle(particle, pX, pY, pZ, motionX, motionY, motionZ, new int[0]);
                        }
                  }

            }
      }

      public static void spawnItemStack(ItemStack stack, World world, double x, double y, double z, boolean withMomentum) {
            float x2;
            float y2;
            float z2;
            if (withMomentum) {
                  x2 = world.rand.nextFloat() * 0.8F + 0.1F;
                  y2 = world.rand.nextFloat() * 0.8F + 0.1F;
                  z2 = world.rand.nextFloat() * 0.8F + 0.1F;
            } else {
                  x2 = 0.5F;
                  y2 = 0.0F;
                  z2 = 0.5F;
            }

            EntityItem entity = new EntityItem(world, x + (double)x2, y + (double)y2, z + (double)z2, stack.copy());
            if (withMomentum) {
                  entity.motionX = (double)((float)world.rand.nextGaussian() * 0.05F);
                  entity.motionY = (double)((float)world.rand.nextGaussian() * 0.05F + 0.2F);
                  entity.motionZ = (double)((float)world.rand.nextGaussian() * 0.05F);
            } else {
                  entity.motionY = -0.05000000074505806D;
                  entity.motionX = 0.0D;
                  entity.motionZ = 0.0D;
            }

            world.spawnEntity(entity);
      }

      public static int getChunkXFromBlock(int blockX) {
            return blockX >> 4;
      }

      public static int getChunkXFromBlock(BlockPos position) {
            return position.getX() >> 4;
      }

      public static int getChunkZFromBlock(int blockZ) {
            return blockZ >> 4;
      }

      public static int getChunkZFromBlock(BlockPos position) {
            return position.getZ() >> 4;
      }

      public static long getChunkXZHashFromBlock(int blockX, int blockZ) {
            return ChunkPos.asLong(getChunkXFromBlock(blockX), getChunkZFromBlock(blockZ));
      }

      public static long getChunkXZHashFromBlock(BlockPos position) {
            return ChunkPos.asLong(getChunkXFromBlock(position), getChunkZFromBlock(position));
      }

      /** @deprecated */
      @Deprecated
      public static boolean blockChunkExists(IChunkProvider chunkProvider, BlockPos position) {
            return null != chunkProvider.getLoadedChunk(getChunkXFromBlock(position), getChunkZFromBlock(position));
      }

      public static void notifyBlockUpdate(World world, BlockPos position, IBlockState oldState, IBlockState newState) {
            if (null == oldState) {
                  oldState = world.getBlockState(position);
            }

            if (null == newState) {
                  newState = oldState;
            }

            world.notifyBlockUpdate(position, oldState, newState, 3);
      }

      public static int getDimensionId(@Nonnull World world) {
            return world.provider.getDimension();
      }

      public static void notifyNeighborsOfStateChange(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block blockType) {
            world.notifyNeighborsOfStateChange(pos, blockType, false);
      }

      @Nullable
      public static TileEntity getTile(@Nonnull IBlockAccess access, @Nonnull BlockPos origin) {
            return access instanceof ChunkCache ? ((ChunkCache)access).getTileEntity(origin, EnumCreateEntityType.CHECK) : access.getTileEntity(origin);
      }

      @Nullable
      public static TileEntity getTile(@Nonnull IBlockAccess access, @Nonnull BlockPos origin, @Nonnull EnumFacing facing) {
            return getTile(access, origin.offset(facing));
      }

      @Nullable
      public static TileEntity getTile(@Nonnull TileEntity origin, @Nonnull EnumFacing facing) {
            return getTile((IBlockAccess)origin.getWorld(), (BlockPos)origin.getPos().offset(facing));
      }

      private WorldHelper() {
      }
}
