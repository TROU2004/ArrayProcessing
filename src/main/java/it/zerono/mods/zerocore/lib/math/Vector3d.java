package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class Vector3d {
      public double X;
      public double Y;
      public double Z;

      public Vector3d(double x, double y, double z) {
            this.set(x, y, z);
      }

      public Vector3d(@Nonnull Vector3d other) {
            this.set(other);
      }

      public Vector3d(@Nonnull Vec3i other) {
            this.set(other);
      }

      @Nonnull
      public static Vector3d from(@Nonnull Vec3i data) {
            return new Vector3d((double)data.getX(), (double)data.getY(), (double)data.getZ());
      }

      @Nonnull
      public static Vector3d fromCenter(@Nonnull Vec3i data) {
            return from(data).add(0.5D);
      }

      @Nonnull
      public static Vector3d from(@Nonnull Entity data) {
            return new Vector3d(data.posX, data.posY, data.posZ);
      }

      @Nonnull
      public static Vector3d fromCenter(@Nonnull Entity data) {
            return from(data).add(0.5D);
      }

      @Nonnull
      public static Vector3d from(@Nonnull TileEntity data) {
            return new Vector3d(data.getPos());
      }

      @Nonnull
      public static Vector3d fromCenter(@Nonnull TileEntity data) {
            return from(data).add(0.5D);
      }

      @Nonnull
      public static Vector3d from(@Nonnull NBTTagCompound data) {
            return (new Vector3d()).loadFrom(data);
      }

      @Nonnull
      public Vec3i toVec3i() {
            return new Vec3i(this.X, this.Y, this.Z);
      }

      @Nonnull
      public BlockPos toBlockPos() {
            return new BlockPos(this.X, this.Y, this.Z);
      }

      @Nonnull
      public Vector3d loadFrom(@Nonnull NBTTagCompound data) {
            this.X = data.getDouble("vx");
            this.Y = data.getDouble("vy");
            this.Z = data.getDouble("vz");
            return this;
      }

      @Nonnull
      public NBTTagCompound saveTo(@Nonnull NBTTagCompound data) {
            data.setDouble("vx", this.X);
            data.setDouble("vy", this.Y);
            data.setDouble("vz", this.Z);
            return data;
      }

      @Nonnull
      public Vector3d set(double x, double y, double z) {
            this.X = x;
            this.Y = y;
            this.Z = z;
            return this;
      }

      @Nonnull
      public Vector3d set(@Nonnull Vector3d data) {
            return this.set(data.X, data.Y, data.Z);
      }

      @Nonnull
      public Vector3d set(@Nonnull Vec3i data) {
            return this.set((double)data.getX(), (double)data.getY(), (double)data.getZ());
      }

      @Nonnull
      public Vector3d add(double offsetX, double offsetY, double offsetZ) {
            this.X += offsetX;
            this.Y += offsetY;
            this.Z += offsetZ;
            return this;
      }

      @Nonnull
      public Vector3d add(double offset) {
            return this.add(offset, offset, offset);
      }

      @Nonnull
      public Vector3d add(@Nonnull Vector3d offset) {
            return this.add(offset.X, offset.Y, offset.X);
      }

      @Nonnull
      public Vector3d add(@Nonnull Vec3i offset) {
            return this.add((double)offset.getX(), (double)offset.getY(), (double)offset.getZ());
      }

      @Nonnull
      public Vector3d subtract(double offsetX, double offsetY, double offsetZ) {
            this.X -= offsetX;
            this.Y -= offsetY;
            this.Z -= offsetZ;
            return this;
      }

      @Nonnull
      public Vector3d subtract(double offset) {
            return this.subtract(offset, offset, offset);
      }

      @Nonnull
      public Vector3d subtract(@Nonnull Vec3i offset) {
            return this.subtract((double)offset.getX(), (double)offset.getY(), (double)offset.getZ());
      }

      @Nonnull
      public Vector3d multiply(double factorX, double factorY, double factorZ) {
            this.X *= factorX;
            this.Y *= factorY;
            this.Z *= factorZ;
            return this;
      }

      @Nonnull
      public Vector3d multiply(double factor) {
            return this.multiply(factor, factor, factor);
      }

      @Nonnull
      public Vector3d multiply(@Nonnull Vec3i factor) {
            return this.multiply((double)factor.getX(), (double)factor.getY(), (double)factor.getZ());
      }

      @Nonnull
      public Vector3d divide(double factorX, double factorY, double factorZ) {
            this.X /= factorX;
            this.Y /= factorY;
            this.Z /= factorZ;
            return this;
      }

      @Nonnull
      public Vector3d divide(double factor) {
            return this.divide(factor, factor, factor);
      }

      @Nonnull
      public Vector3d divide(@Nonnull Vec3i factor) {
            return this.divide((double)factor.getX(), (double)factor.getY(), (double)factor.getZ());
      }

      @Nonnull
      public Vector3d ceil() {
            this.X = (double)MathHelper.ceil(this.X);
            this.Y = (double)MathHelper.ceil(this.Y);
            this.Z = (double)MathHelper.ceil(this.Z);
            return this;
      }

      @Nonnull
      public Vector3d floor() {
            this.X = (double)MathHelper.floor(this.X);
            this.Y = (double)MathHelper.floor(this.Y);
            this.Z = (double)MathHelper.floor(this.Z);
            return this;
      }

      public double magnitude() {
            return Math.sqrt(this.X * this.X + this.Y * this.Y + this.Z * this.Z);
      }

      @Nonnull
      public Vector3d normalize() {
            double magnitude = this.magnitude();
            if (0.0D != magnitude) {
                  this.multiply(1.0D / magnitude);
            }

            return this;
      }

      public double scalarProduct(double x, double y, double z) {
            return this.X * x + this.Y * y + this.Z * z;
      }

      public double scalarProduct(@Nonnull Vector3d vector) {
            double product = this.X * vector.X + this.Y * vector.Y + this.Z * vector.Z;
            if (product > 1.0D && product < 1.00001D) {
                  product = 1.0D;
            } else if (product < -1.0D && product > -1.00001D) {
                  product = -1.0D;
            }

            return product;
      }

      public boolean equals(Object other) {
            if (!(other instanceof Vector3d)) {
                  return false;
            } else {
                  Vector3d v = (Vector3d)other;
                  return this.X == v.X && this.Y == v.Y && this.Z == v.Z;
            }
      }

      public String toString() {
            return String.format("Vector3d (%f, %f, %f)", this.X, this.Y, this.Z);
      }

      private Vector3d() {
      }
}
