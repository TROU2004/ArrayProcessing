package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.client.render.Vertex;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VertexBuilder {
      private final boolean _autoReset;
      private Vector3d _position;
      private Vector3d _normal;
      private UV _uv;
      private Colour _colour;
      private LightMap _lightMap;
      private static VertexBuilder s_defaultBuilder = null;

      @Nonnull
      public static VertexBuilder getDefaultBuilder() {
            if (null == s_defaultBuilder) {
                  s_defaultBuilder = new VertexBuilder(false);
            }

            return s_defaultBuilder;
      }

      public VertexBuilder(boolean autoReset) {
            this._autoReset = autoReset;
      }

      @Nonnull
      public Vertex build() {
            Vertex vertex = new Vertex(this._position, this._normal, this._uv, this._colour, this._lightMap);
            if (this._autoReset) {
                  this.reset();
            }

            return vertex;
      }

      public void reset() {
            this._position = this._normal = null;
            this._uv = null;
            this._colour = null;
            this._lightMap = null;
      }

      @Nonnull
      public VertexBuilder setTexture(@Nonnull UV uv) {
            this._uv = uv;
            return this;
      }

      @Nonnull
      public VertexBuilder setTexture(double u, double v) {
            return this.setTexture(new UV(u, v));
      }

      @Nonnull
      public VertexBuilder setTexture(@Nonnull TextureAtlasSprite sprite) {
            return this.setTexture(new UV((double)sprite.getMinU(), (double)sprite.getMinV()));
      }

      @Nonnull
      public VertexBuilder setLightMap(@Nonnull LightMap lightMap) {
            this._lightMap = lightMap;
            return this;
      }

      @Nonnull
      public VertexBuilder setLightMap(int skyLight, int blockLight) {
            return this.setLightMap(new LightMap(skyLight, blockLight));
      }

      @Nonnull
      public VertexBuilder setColour(@Nonnull Colour colour) {
            this._colour = colour;
            return this;
      }

      @Nonnull
      public VertexBuilder setColour(int red, int green, int blue, int alpha) {
            return this.setColour(new Colour(red, green, blue, alpha));
      }

      @Nonnull
      public VertexBuilder setColour(double red, double green, double blue, double alpha) {
            return this.setColour(new Colour(red, green, blue, alpha));
      }

      @Nonnull
      public VertexBuilder setPosition(@Nonnull Vector3d position) {
            this._position = position;
            return this;
      }

      @Nonnull
      public VertexBuilder setPosition(double x, double y, double z) {
            return this.setPosition(new Vector3d(x, y, z));
      }

      @Nonnull
      public VertexBuilder setNormal(@Nonnull Vector3d normal) {
            this._normal = normal;
            return this;
      }

      @Nonnull
      public VertexBuilder setNormal(double x, double y, double z) {
            return this.setNormal(new Vector3d(x, y, z));
      }
}
