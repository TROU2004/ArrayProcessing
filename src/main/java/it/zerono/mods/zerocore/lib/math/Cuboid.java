package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public class Cuboid {
      public Vector3d MIN;
      public Vector3d MAX;

      public Cuboid(@Nonnull Vector3d min, @Nonnull Vector3d max) {
            this.MIN = min;
            this.MAX = max;
      }

      public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            this(new Vector3d(minX, minY, minZ), new Vector3d(maxX, maxY, maxZ));
      }

      public Cuboid(@Nonnull Cuboid other) {
            this(new Vector3d(other.MIN), new Vector3d(other.MAX));
      }

      public Cuboid(@Nonnull Vec3i min, @Nonnull Vec3i max) {
            this(new Vector3d(min), new Vector3d(max));
      }

      public Cuboid(@Nonnull AxisAlignedBB boundingBox) {
            this(new Vector3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ), new Vector3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ));
      }

      @Nonnull
      public static Cuboid from(@Nonnull NBTTagCompound data) {
            return (new Cuboid()).loadFrom(data);
      }

      @Nonnull
      public AxisAlignedBB toBoundingBox() {
            return new AxisAlignedBB(this.MIN.X, this.MIN.Y, this.MIN.Z, this.MAX.X, this.MAX.Y, this.MAX.Z);
      }

      @Nonnull
      public Cuboid loadFrom(@Nonnull NBTTagCompound data) {
            this.MIN = Vector3d.from(data.getCompoundTag("min"));
            this.MAX = Vector3d.from(data.getCompoundTag("max"));
            return this;
      }

      @Nonnull
      public NBTTagCompound saveTo(@Nonnull NBTTagCompound data) {
            data.setTag("min", this.MIN.saveTo(new NBTTagCompound()));
            data.setTag("max", this.MAX.saveTo(new NBTTagCompound()));
            return data;
      }

      @Nonnull
      public Cuboid set(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            this.MIN.set(minX, minY, minZ);
            this.MAX.set(maxX, maxY, maxZ);
            return this;
      }

      @Nonnull
      public Cuboid set(@Nonnull Cuboid data) {
            this.MIN.set(data.MIN);
            this.MAX.set(data.MAX);
            return this;
      }

      @Nonnull
      public Cuboid set(@Nonnull Vector3d min, @Nonnull Vector3d max) {
            this.MIN.set(min);
            this.MAX.set(max);
            return this;
      }

      @Nonnull
      public Cuboid set(@Nonnull Vec3i min, @Nonnull Vec3i max) {
            this.MIN.set(min);
            this.MAX.set(max);
            return this;
      }

      @Nonnull
      public Cuboid add(double offsetX, double offsetY, double offsetZ) {
            this.MIN.add(offsetX, offsetY, offsetZ);
            this.MAX.add(offsetX, offsetY, offsetZ);
            return this;
      }

      @Nonnull
      public Cuboid add(double offset) {
            return this.add(offset, offset, offset);
      }

      @Nonnull
      public Cuboid add(@Nonnull Vector3d data) {
            return this.add(data.X, data.Y, data.Z);
      }

      @Nonnull
      public Cuboid add(@Nonnull Vec3i data) {
            return this.add((double)data.getX(), (double)data.getY(), (double)data.getZ());
      }

      @Nonnull
      public Cuboid subtract(double offsetX, double offsetY, double offsetZ) {
            this.MIN.subtract(offsetX, offsetY, offsetZ);
            this.MAX.subtract(offsetX, offsetY, offsetZ);
            return this;
      }

      @Nonnull
      public Cuboid subtract(double offset) {
            return this.subtract(offset, offset, offset);
      }

      @Nonnull
      public Cuboid subtract(@Nonnull Vector3d data) {
            return this.subtract(data.X, data.Y, data.Z);
      }

      @Nonnull
      public Cuboid subtract(@Nonnull Vec3i data) {
            return this.subtract((double)data.getX(), (double)data.getY(), (double)data.getZ());
      }

      @Nonnull
      public Cuboid expand(double deltaX, double deltaY, double deltaZ) {
            this.MIN.subtract(deltaX, deltaY, deltaZ);
            this.MAX.add(deltaX, deltaY, deltaZ);
            return this;
      }

      @Nonnull
      public Cuboid expand(double delta) {
            return this.expand(delta, delta, delta);
      }

      @Nonnull
      public Cuboid expand(@Nonnull Vector3d delta) {
            return this.expand(delta.X, delta.Y, delta.Z);
      }

      @Nonnull
      public Cuboid expand(@Nonnull EnumFacing side, int amount) {
            switch(side.getAxisDirection()) {
            case NEGATIVE:
                  this.MIN.add(Vector3d.from(side.getDirectionVec()).multiply((double)amount));
                  break;
            case POSITIVE:
                  this.MAX.add(Vector3d.from(side.getDirectionVec()).multiply((double)amount));
            }

            return this;
      }

      public boolean contains(double x, double y, double z) {
            return this.MIN.X - 1.0E-5D <= x && this.MIN.Y - 1.0E-5D <= y && this.MIN.Z - 1.0E-5D <= z && this.MAX.X + 1.0E-5D >= x && this.MAX.Y + 1.0E-5D >= y && this.MAX.Z + 1.0E-5D >= z;
      }

      public boolean contains(@Nonnull Vector3d data) {
            return this.contains(data.X, data.Y, data.Z);
      }

      public boolean intersects(@Nonnull Cuboid other) {
            return this.MAX.X - 1.0E-5D > other.MIN.X && this.MAX.Y - 1.0E-5D > other.MIN.Y && this.MAX.Z - 1.0E-5D > other.MIN.Z && other.MAX.X - 1.0E-5D > this.MIN.X && other.MAX.Y - 1.0E-5D > this.MIN.Y && other.MAX.Z - 1.0E-5D > this.MIN.Z;
      }

      public double volume() {
            return (this.MAX.X - this.MIN.X + 1.0D) * (this.MAX.Y - this.MIN.Y + 1.0D) * (this.MAX.Z - this.MIN.Z + 1.0D);
      }

      @Nonnull
      public Vector3d center() {
            return (new Vector3d(this.MIN)).add(this.MAX).multiply(0.5D);
      }

      @Nonnull
      public Cuboid.Face getFace(@Nonnull EnumFacing facing) {
            return new Cuboid.Face(this, facing);
      }

      public double getWidth() {
            return this.MAX.X - this.MIN.X;
      }

      public double getHeight() {
            return this.MAX.Y - this.MIN.Y;
      }

      public double getDepth() {
            return this.MAX.Z - this.MIN.Z;
      }

      public boolean equals(Object other) {
            if (!(other instanceof Cuboid)) {
                  return false;
            } else {
                  Cuboid c = (Cuboid)other;
                  return this.MIN.equals(c.MIN) && this.MAX.equals(c.MAX);
            }
      }

      public String toString() {
            return String.format("Cuboid (%f, %f, %f), (%f, %f, %f)", this.MIN.X, this.MIN.Y, this.MIN.Z, this.MAX.X, this.MAX.Y, this.MAX.Z);
      }

      private Cuboid() {
      }

      public static class Face {
            public final Vector3d A;
            public final Vector3d B;
            public final Vector3d C;
            public final Vector3d D;
            public final EnumFacing FACING;

            private Face(@Nonnull Cuboid cuboid, EnumFacing facing) {
                  this.FACING = facing;
                  switch(facing) {
                  case UP:
                  default:
                        this.A = new Vector3d(cuboid.MIN.X, cuboid.MAX.Y, cuboid.MIN.Z);
                        this.B = new Vector3d(cuboid.MIN.X, cuboid.MAX.Y, cuboid.MAX.Z);
                        this.C = new Vector3d(cuboid.MAX.X, cuboid.MAX.Y, cuboid.MAX.Z);
                        this.D = new Vector3d(cuboid.MAX.X, cuboid.MAX.Y, cuboid.MIN.Z);
                        break;
                  case DOWN:
                        this.A = new Vector3d(cuboid.MIN.X, cuboid.MIN.Y, cuboid.MIN.Z);
                        this.B = new Vector3d(cuboid.MAX.X, cuboid.MIN.Y, cuboid.MIN.Z);
                        this.C = new Vector3d(cuboid.MAX.X, cuboid.MIN.Y, cuboid.MAX.Z);
                        this.D = new Vector3d(cuboid.MIN.X, cuboid.MIN.Y, cuboid.MAX.Z);
                        break;
                  case WEST:
                        this.A = new Vector3d(cuboid.MIN.X, cuboid.MAX.Y, cuboid.MIN.Z);
                        this.B = new Vector3d(cuboid.MIN.X, cuboid.MIN.Y, cuboid.MIN.Z);
                        this.C = new Vector3d(cuboid.MIN.X, cuboid.MIN.Y, cuboid.MAX.Z);
                        this.D = new Vector3d(cuboid.MIN.X, cuboid.MAX.Y, cuboid.MAX.Z);
                        break;
                  case EAST:
                        this.A = new Vector3d(cuboid.MAX.X, cuboid.MAX.Y, cuboid.MAX.Z);
                        this.B = new Vector3d(cuboid.MAX.X, cuboid.MIN.Y, cuboid.MAX.Z);
                        this.C = new Vector3d(cuboid.MAX.X, cuboid.MIN.Y, cuboid.MIN.Z);
                        this.D = new Vector3d(cuboid.MAX.X, cuboid.MAX.Y, cuboid.MIN.Z);
                        break;
                  case NORTH:
                        this.A = new Vector3d(cuboid.MAX.X, cuboid.MAX.Y, cuboid.MIN.Z);
                        this.B = new Vector3d(cuboid.MAX.X, cuboid.MIN.Y, cuboid.MIN.Z);
                        this.C = new Vector3d(cuboid.MIN.X, cuboid.MIN.Y, cuboid.MIN.Z);
                        this.D = new Vector3d(cuboid.MIN.X, cuboid.MAX.Y, cuboid.MIN.Z);
                        break;
                  case SOUTH:
                        this.A = new Vector3d(cuboid.MIN.X, cuboid.MAX.Y, cuboid.MAX.Z);
                        this.B = new Vector3d(cuboid.MIN.X, cuboid.MIN.Y, cuboid.MAX.Z);
                        this.C = new Vector3d(cuboid.MAX.X, cuboid.MIN.Y, cuboid.MAX.Z);
                        this.D = new Vector3d(cuboid.MAX.X, cuboid.MAX.Y, cuboid.MAX.Z);
                  }

            }

            @Nonnull
            public Vector3d getVertexByIndex(int index) {
                  switch(index) {
                  case 0:
                        return this.A;
                  case 1:
                        return this.B;
                  case 2:
                        return this.C;
                  case 3:
                        return this.D;
                  default:
                        throw new IllegalArgumentException("Invalid vertex index");
                  }
            }

            public boolean equals(Object other) {
                  if (!(other instanceof Cuboid.Face)) {
                        return false;
                  } else {
                        Cuboid.Face f = (Cuboid.Face)other;
                        return this.FACING == f.FACING && this.A.equals(f.A) && this.B.equals(f.B) && this.C.equals(f.C) && this.D.equals(f.D);
                  }
            }

            public String toString() {
                  return String.format("Cuboid.Face [%s]: %s, %s, %s, %s", this.FACING.getName(), this.A.toString(), this.B.toString(), this.C.toString(), this.D.toString());
            }

            // $FF: synthetic method
            Face(Cuboid x0, EnumFacing x1, Object x2) {
                  this(x0, x1);
            }
      }
}
