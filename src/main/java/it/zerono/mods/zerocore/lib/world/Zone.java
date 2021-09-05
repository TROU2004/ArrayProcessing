package it.zerono.mods.zerocore.lib.world;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public abstract class Zone implements Comparable {
      public static Zone rectangular(BlockPos minCoords, BlockPos maxCoords) {
            return new Zone.RectangularCuboidZone(minCoords, maxCoords);
      }

      public static Zone sphere(BlockPos center, int radius) {
            return new Zone.SphereZone(center, radius);
      }

      public abstract boolean contains(BlockPos var1);

      public abstract AxisAlignedBB getBoundingBox();

      public int compareTo(Zone zone) {
            AxisAlignedBB myBB = this.getBoundingBox();
            AxisAlignedBB hisBB = zone.getBoundingBox();
            return 0;
      }

      private static class SphereZone extends Zone {
            private final BlockPos _center;
            private final int _radius;

            SphereZone(BlockPos center, int radius) {
                  this._center = center;
                  this._radius = radius;
            }

            public boolean contains(BlockPos position) {
                  return (position.getX() - this._center.getX()) * (position.getX() - this._center.getX()) + (position.getY() - this._center.getY()) * (position.getY() - this._center.getY()) + (position.getZ() - this._center.getZ()) * (position.getZ() - this._center.getZ()) <= this._radius * this._radius;
            }

            public AxisAlignedBB getBoundingBox() {
                  int centerX = this._center.getX();
                  int centerY = this._center.getY();
                  int centerZ = this._center.getZ();
                  return new AxisAlignedBB((double)(centerX - this._radius), (double)(centerY - this._radius), (double)(centerZ - this._radius), (double)(centerX + this._radius - 1), (double)(centerY + this._radius - 1), (double)(centerZ + this._radius - 1));
            }

            public boolean equals(Object o) {
                  if (this == o) {
                        return true;
                  } else if (!(o instanceof Zone.SphereZone)) {
                        return false;
                  } else {
                        Zone.SphereZone other = (Zone.SphereZone)o;
                        return this._radius == other._radius && this._center.equals(other._center);
                  }
            }

            @Override
            public int compareTo(Object o) {
                  return 0;
            }
      }

      private static class RectangularCuboidZone extends Zone {
            private final BlockPos _minCoords;
            private final BlockPos _maxCoords;

            RectangularCuboidZone(BlockPos minCoords, BlockPos maxCoords) {
                  this._minCoords = minCoords;
                  this._maxCoords = maxCoords;
            }

            public boolean contains(BlockPos position) {
                  return this._minCoords.getX() <= position.getX() && position.getX() <= this._maxCoords.getX() && this._minCoords.getY() <= position.getY() && position.getY() <= this._maxCoords.getY() && this._minCoords.getZ() <= position.getZ() && position.getZ() <= this._maxCoords.getZ();
            }

            public AxisAlignedBB getBoundingBox() {
                  return new AxisAlignedBB(this._minCoords, this._maxCoords);
            }

            public boolean equals(Object o) {
                  if (this == o) {
                        return true;
                  } else if (!(o instanceof Zone.RectangularCuboidZone)) {
                        return false;
                  } else {
                        Zone.RectangularCuboidZone other = (Zone.RectangularCuboidZone)o;
                        return this._minCoords.equals(other._minCoords) && this._maxCoords.equals(other._maxCoords);
                  }
            }

            @Override
            public int compareTo(Object o) {
                  return 0;
            }
      }
}
