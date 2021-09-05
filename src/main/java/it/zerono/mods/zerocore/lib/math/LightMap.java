package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class LightMap {
      public int SKY_LIGHT;
      public int BLOCK_LIGHT;

      public LightMap(int skyLight, int blockLight) {
            this.set(skyLight, blockLight);
      }

      public LightMap(@Nonnull LightMap other) {
            this.set(other);
      }

      @Nonnull
      public static LightMap fromCombinedLight(int combinedLight) {
            return new LightMap(getSkyLightFromCombined(combinedLight), getBlockLightFromCombined(combinedLight));
      }

      public static int getSkyLightFromCombined(int combinedLight) {
            return combinedLight >> 16 & '\uffff';
      }

      public static int getBlockLightFromCombined(int combinedLight) {
            return combinedLight & '\uffff';
      }

      public static int getCombinedLight(int skyLight, int blockLight) {
            return skyLight << 20 | blockLight << 4;
      }

      @Nonnull
      public LightMap set(int skyLight, int blockLight) {
            this.SKY_LIGHT = skyLight;
            this.BLOCK_LIGHT = blockLight;
            return this;
      }

      @Nonnull
      public LightMap set(@Nonnull LightMap other) {
            return this.set(other.SKY_LIGHT, other.BLOCK_LIGHT);
      }

      @Nonnull
      public LightMap set(int combinedLight) {
            return this.set(getSkyLightFromCombined(combinedLight), getBlockLightFromCombined(combinedLight));
      }

      public boolean equals(Object other) {
            if (!(other instanceof LightMap)) {
                  return false;
            } else {
                  LightMap map = (LightMap)other;
                  return this.SKY_LIGHT == map.SKY_LIGHT && this.BLOCK_LIGHT == map.BLOCK_LIGHT;
            }
      }

      public String toString() {
            return String.format("LightMap (0x%08x, 0x%08x)", this.SKY_LIGHT, this.BLOCK_LIGHT);
      }

      private LightMap() {
      }
}
