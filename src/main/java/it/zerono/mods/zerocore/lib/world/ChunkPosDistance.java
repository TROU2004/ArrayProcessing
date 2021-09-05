package it.zerono.mods.zerocore.lib.world;

import it.zerono.mods.zerocore.util.Shape;
import java.util.HashSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ChunkPosDistance extends ChunkPos {
      public final float distance;

      public ChunkPosDistance(int x, int z, float distance) {
            super(x, z);
            this.distance = distance;
      }

      public ChunkPosDistance(BlockPos pos, float distance) {
            super(pos);
            this.distance = distance;
      }

      public static HashSet getChunksInRange(BlockPos startingPoint, Shape area, int radiusX, int radiusZ) {
            return getChunksInRange(startingPoint.getX() >> 4, startingPoint.getZ() >> 4, area, radiusX, radiusZ);
      }

      public static HashSet getChunksInRange(int startingChunkX, int startingChunkZ, Shape area, int radiusX, int radiusZ) {
            HashSet chunks = new HashSet();
            int x;
            int z;
            int deltaX;
            int deltaZ;
            double distance;
            switch(area) {
            case LineEastWest:
                  chunks.add(new ChunkPosDistance(startingChunkX, startingChunkZ, 0.0F));

                  for(x = 1; x <= radiusX; ++x) {
                        chunks.add(new ChunkPosDistance(startingChunkX - x, startingChunkZ, (float)x));
                        chunks.add(new ChunkPosDistance(startingChunkX + x, startingChunkZ, (float)x));
                  }

                  return chunks;
            case LineUpDown:
                  chunks.add(new ChunkPosDistance(startingChunkX, startingChunkZ, 0.0F));
                  break;
            case LineSouthNorth:
                  chunks.add(new ChunkPosDistance(startingChunkX, startingChunkZ, 0.0F));

                  for(x = 1; x <= radiusZ; ++x) {
                        chunks.add(new ChunkPosDistance(startingChunkX, startingChunkZ - x, (float)x));
                        chunks.add(new ChunkPosDistance(startingChunkX, startingChunkZ + x, (float)x));
                  }

                  return chunks;
            case Square:
            case Rectangle:
                  x = Shape.Rectangle == area ? radiusZ : radiusX;

                  for(z = startingChunkZ - x; z <= startingChunkZ + x; ++z) {
                        for(deltaX = startingChunkX - radiusX; deltaX <= startingChunkX + radiusX; ++deltaX) {
                              chunks.add(new ChunkPosDistance(deltaX, z, (float)Math.sqrt((double)(deltaX * deltaX + z * z))));
                        }
                  }

                  return chunks;
            case Circle:
                  for(x = startingChunkX - radiusX; x <= startingChunkX + radiusX; ++x) {
                        for(z = startingChunkZ - radiusX; z <= startingChunkZ + radiusX; ++z) {
                              deltaX = x - startingChunkX;
                              deltaZ = z - startingChunkZ;
                              distance = Math.sqrt((double)(deltaX * deltaX + deltaZ * deltaZ));
                              if (distance < (double)radiusX) {
                                    chunks.add(new ChunkPosDistance(x, z, (float)distance));
                              }
                        }
                  }

                  return chunks;
            case Ellipse:
                  for(x = startingChunkX - radiusX; x <= startingChunkX + radiusX; ++x) {
                        for(z = startingChunkZ - radiusZ; z <= startingChunkZ + radiusZ; ++z) {
                              if (x * x / (radiusX * radiusX) + z * z / (radiusZ * radiusZ) <= 1) {
                                    deltaX = x - startingChunkX;
                                    deltaZ = z - startingChunkZ;
                                    distance = Math.sqrt((double)(deltaX * deltaX + deltaZ * deltaZ));
                                    chunks.add(new ChunkPosDistance(x, z, (float)distance));
                              }
                        }
                  }
            }

            return chunks;
      }
}
