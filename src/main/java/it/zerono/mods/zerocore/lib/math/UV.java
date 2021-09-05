package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class UV {
      public double U;
      public double V;

      public UV(double u, double v) {
            this.set(u, v);
      }

      public UV(@Nonnull UV other) {
            this.set(other);
      }

      @Nonnull
      public UV set(double u, double v) {
            this.U = u;
            this.V = v;
            return this;
      }

      @Nonnull
      public UV set(@Nonnull UV other) {
            return this.set(other.U, other.V);
      }

      @Nonnull
      public UV multiply(double factor) {
            this.U *= factor;
            this.V *= factor;
            return this;
      }

      public boolean equals(Object other) {
            if (!(other instanceof UV)) {
                  return false;
            } else {
                  UV uv = (UV)other;
                  return this.U == uv.U && this.V == uv.V;
            }
      }

      public String toString() {
            return String.format("UV (%f, %f)", this.U, this.V);
      }

      private UV() {
      }
}
