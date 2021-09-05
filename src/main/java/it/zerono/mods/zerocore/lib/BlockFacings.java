package it.zerono.mods.zerocore.lib;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;

public final class BlockFacings {
      public static final BlockFacings NONE;
      public static final BlockFacings ALL;
      public static final BlockFacings DOWN;
      public static final BlockFacings UP;
      public static final BlockFacings NORTH;
      public static final BlockFacings SOUTH;
      public static final BlockFacings WEST;
      public static final BlockFacings EAST;
      public static final BlockFacings VERTICAL;
      public static final BlockFacings HORIZONTAL;
      public static final BlockFacings AXIS_X;
      public static final BlockFacings AXIS_Y;
      public static final BlockFacings AXIS_Z;
      public static final PropertyBool FACING_DOWN = PropertyBool.create("downFacing");
      public static final PropertyBool FACING_UP = PropertyBool.create("upFacing");
      public static final PropertyBool FACING_WEST = PropertyBool.create("westFacing");
      public static final PropertyBool FACING_EAST = PropertyBool.create("eastFacing");
      public static final PropertyBool FACING_NORTH = PropertyBool.create("northFacing");
      public static final PropertyBool FACING_SOUTH = PropertyBool.create("southFacing");
      private byte _value;
      private static Map s_cache = new HashMap(12);

      public boolean isSet(@Nonnull EnumFacing facing) {
            return 0 != (this._value & 1 << facing.getIndex());
      }

      public boolean none() {
            return 0 == this._value;
      }

      public boolean all() {
            return 63 == this._value;
      }

      public boolean down() {
            return this.isSet(EnumFacing.DOWN);
      }

      public boolean up() {
            return this.isSet(EnumFacing.UP);
      }

      public boolean north() {
            return this.isSet(EnumFacing.NORTH);
      }

      public boolean south() {
            return this.isSet(EnumFacing.SOUTH);
      }

      public boolean west() {
            return this.isSet(EnumFacing.WEST);
      }

      public boolean east() {
            return this.isSet(EnumFacing.EAST);
      }

      @Nonnull
      public IBlockState toBlockState(@Nonnull IBlockState state) {
            return state.withProperty(FACING_DOWN, this.isSet(EnumFacing.DOWN)).withProperty(FACING_UP, this.isSet(EnumFacing.UP)).withProperty(FACING_WEST, this.isSet(EnumFacing.WEST)).withProperty(FACING_EAST, this.isSet(EnumFacing.EAST)).withProperty(FACING_NORTH, this.isSet(EnumFacing.NORTH)).withProperty(FACING_SOUTH, this.isSet(EnumFacing.SOUTH));
      }

      @Nonnull
      public BlockFacings set(@Nonnull EnumFacing facing, boolean value) {
            byte newHash = this._value;
            if (value) {
                  newHash = (byte)(newHash | 1 << facing.getIndex());
            } else {
                  newHash = (byte)(newHash & ~(1 << facing.getIndex()));
            }

            return from(newHash);
      }

      public int countFacesIf(boolean areSet) {
            int checkFor = areSet ? 1 : 0;
            int mask = this._value;
            int faces = 0;

            for(int i = 0; i < 6; mask >>>= 1) {
                  if ((mask & 1) == checkFor) {
                        ++faces;
                  }

                  ++i;
            }

            return faces;
      }

      @Nonnull
      public PropertyBlockFacings toProperty() {
            PropertyBlockFacings[] values = PropertyBlockFacings.values();

            for(int i = 0; i < values.length; ++i) {
                  if (values[i]._hash == this._value) {
                        return values[i];
                  }
            }

            return PropertyBlockFacings.None;
      }

      @Nonnull
      public BlockPos offsetBlockPos(@Nonnull BlockPos originalPosition) {
            int x = 0;
            int y = 0;
            int z = 0;
            EnumFacing[] var5 = EnumFacing.VALUES;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                  EnumFacing facing = var5[var7];
                  if (this.isSet(facing)) {
                        x += facing.getXOffset();
                        y += facing.getYOffset();
                        z += facing.getZOffset();
                  }
            }

            return originalPosition.add(x, y, z);
      }

      @Nullable
      public EnumFacing firstIf(boolean isSet) {
            EnumFacing[] var2 = EnumFacing.VALUES;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                  EnumFacing facing = var2[var4];
                  if (isSet == this.isSet(facing)) {
                        return facing;
                  }
            }

            return null;
      }

      @Nonnull
      public static BlockFacings from(boolean down, boolean up, boolean north, boolean south, boolean west, boolean east) {
            return from(computeHash(down, up, north, south, west, east));
      }

      @Nonnull
      public static BlockFacings from(@Nonnull boolean[] facings) {
            return from(computeHash(facings));
      }

      @Nonnull
      public static BlockFacings from(@Nonnull Axis axis) {
            switch(axis) {
            case X:
            default:
                  return AXIS_X;
            case Y:
                  return AXIS_Y;
            case Z:
                  return AXIS_Z;
            }
      }

      @Nonnull
      public static BlockFacings from(@Nonnull Plane plane) {
            switch(plane) {
            case VERTICAL:
            default:
                  return VERTICAL;
            case HORIZONTAL:
                  return HORIZONTAL;
            }
      }

      public String toString() {
            return String.format("Facings: %s%s%s%s%s%s", this.isSet(EnumFacing.DOWN) ? "DOWN " : "", this.isSet(EnumFacing.UP) ? "UP " : "", this.isSet(EnumFacing.NORTH) ? "NORTH " : "", this.isSet(EnumFacing.SOUTH) ? "SOUTH " : "", this.isSet(EnumFacing.WEST) ? "WEST " : "", this.isSet(EnumFacing.EAST) ? "EAST " : "");
      }

      @Nonnull
      static BlockFacings from(@Nonnull Byte hash) {
            BlockFacings facings = (BlockFacings)s_cache.get(hash);
            if (null == facings) {
                  facings = new BlockFacings(hash);
                  s_cache.put(hash, facings);
            }

            return facings;
      }

      private BlockFacings(byte value) {
            this._value = value;
      }

      @Nonnull
      static Byte computeHash(boolean down, boolean up, boolean north, boolean south, boolean west, boolean east) {
            byte hash = 0;
            if (down) {
                  hash = (byte)(hash | 1 << EnumFacing.DOWN.getIndex());
            }

            if (up) {
                  hash = (byte)(hash | 1 << EnumFacing.UP.getIndex());
            }

            if (north) {
                  hash = (byte)(hash | 1 << EnumFacing.NORTH.getIndex());
            }

            if (south) {
                  hash = (byte)(hash | 1 << EnumFacing.SOUTH.getIndex());
            }

            if (west) {
                  hash = (byte)(hash | 1 << EnumFacing.WEST.getIndex());
            }

            if (east) {
                  hash = (byte)(hash | 1 << EnumFacing.EAST.getIndex());
            }

            return hash;
      }

      @Nonnull
      static Byte computeHash(@Nonnull boolean[] facings) {
            byte hash = 0;
            int len = null == facings ? -1 : facings.length;
            if (len >= 0 && len <= EnumFacing.VALUES.length) {
                  for(int i = 0; i < len; ++i) {
                        if (facings[i]) {
                              hash = (byte)(hash | 1 << EnumFacing.VALUES[i].getIndex());
                        }
                  }

                  return hash;
            } else {
                  throw new IllegalArgumentException("Invalid length of facings array");
            }
      }

      static {
            Byte hash = computeHash(false, false, false, false, false, false);
            s_cache.put(hash, NONE = new BlockFacings(hash));
            hash = computeHash(true, true, true, true, true, true);
            s_cache.put(hash, ALL = new BlockFacings(hash));
            hash = computeHash(true, false, false, false, false, false);
            s_cache.put(hash, DOWN = new BlockFacings(hash));
            hash = computeHash(false, true, false, false, false, false);
            s_cache.put(hash, UP = new BlockFacings(hash));
            hash = computeHash(false, false, true, false, false, false);
            s_cache.put(hash, NORTH = new BlockFacings(hash));
            hash = computeHash(false, false, false, true, false, false);
            s_cache.put(hash, SOUTH = new BlockFacings(hash));
            hash = computeHash(false, false, false, false, true, false);
            s_cache.put(hash, WEST = new BlockFacings(hash));
            hash = computeHash(false, false, false, false, false, true);
            s_cache.put(hash, EAST = new BlockFacings(hash));
            hash = computeHash(true, true, false, false, false, false);
            s_cache.put(hash, VERTICAL = new BlockFacings(hash));
            hash = computeHash(false, false, true, true, true, true);
            s_cache.put(hash, HORIZONTAL = new BlockFacings(hash));
            hash = computeHash(false, false, false, false, true, true);
            s_cache.put(hash, AXIS_X = new BlockFacings(hash));
            hash = computeHash(false, false, true, true, false, false);
            s_cache.put(hash, AXIS_Z = new BlockFacings(hash));
            AXIS_Y = VERTICAL;
      }
}
