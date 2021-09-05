package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.client.render.Shape;
import it.zerono.mods.zerocore.lib.client.render.Vertex;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.Cuboid;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuadBuilder extends AbstractShapeBuilder {
      public static final int VERTICES_COUNT = 4;
      private Cuboid.Face _face;
      private final AbstractShapeBuilder.PolygonalFaceData _faceData = new AbstractShapeBuilder.PolygonalFaceData(4);
      private static QuadBuilder s_defaultBuilder = null;

      @Nonnull
      public static QuadBuilder getDefaultBuilder() {
            if (null == s_defaultBuilder) {
                  s_defaultBuilder = new QuadBuilder(false);
            }

            return s_defaultBuilder;
      }

      public QuadBuilder(boolean autoReset) {
            super(autoReset);
      }

      @Nonnull
      public Shape build() {
            if (null == this._face) {
                  throw new IllegalStateException("No face was provided!");
            } else {
                  Shape shape = new Shape(4);
                  VertexBuilder vertexBuilder = new VertexBuilder(true);

                  for(int vertexIndex = 0; vertexIndex < 4; ++vertexIndex) {
                        shape.addVertex(buildSingleVertex(vertexIndex, vertexBuilder, this._face, this._faceData));
                  }

                  if (this.autoReset()) {
                        this.reset();
                  }

                  return shape;
            }
      }

      static Vertex buildSingleVertex(int vertexIndex, @Nonnull VertexBuilder vertexBuilder, @Nonnull Cuboid.Face face, @Nonnull AbstractShapeBuilder.PolygonalFaceData faceData) {
            UV uv = faceData.getUvAt(vertexIndex);
            Colour colour = faceData.getColourlAt(vertexIndex);
            LightMap lightMap = faceData.getLightMapAt(vertexIndex);
            Vector3d normal = faceData.getNormalAt(vertexIndex);
            vertexBuilder.setPosition(face.getVertexByIndex(vertexIndex));
            if (null != uv) {
                  vertexBuilder.setTexture(uv);
            }

            if (null != colour) {
                  vertexBuilder.setColour(colour);
            }

            if (null != lightMap) {
                  vertexBuilder.setLightMap(lightMap);
            }

            if (null != normal) {
                  vertexBuilder.setNormal(normal);
            }

            return vertexBuilder.build();
      }

      public void reset() {
            this._face = null;
            this._faceData.reset();
      }

      @Nonnull
      public QuadBuilder setFace(@Nonnull Cuboid.Face face) {
            this._face = face;
            return this;
      }

      @Nonnull
      public QuadBuilder setFace(@Nonnull Cuboid cuboid, @Nonnull EnumFacing facing) {
            return this.setFace(cuboid.getFace(facing));
      }

      @Nonnull
      public QuadBuilder setColour(@Nonnull Colour colour) {
            this._faceData.setColour(colour);
            return this;
      }

      @Nonnull
      public QuadBuilder setColour(int vertexIndex, @Nonnull Colour colour) {
            this._faceData.setColour(vertexIndex, colour);
            return this;
      }

      @Nonnull
      public QuadBuilder setTexture(@Nonnull UV a, @Nonnull UV b, @Nonnull UV c, @Nonnull UV d) {
            this._faceData.setTexture(0, a);
            this._faceData.setTexture(1, b);
            this._faceData.setTexture(2, c);
            this._faceData.setTexture(3, d);
            return this;
      }

      @Nonnull
      public QuadBuilder setTexture(int vertexIndex, @Nonnull UV uv) {
            this._faceData.setTexture(vertexIndex, uv);
            return this;
      }

      @Nonnull
      public QuadBuilder setTexture(@Nonnull TextureAtlasSprite sprite) {
            this._faceData.setTexture(sprite);
            return this;
      }

      @Nonnull
      public QuadBuilder setLightMap(LightMap lightMap) {
            this._faceData.setLightMap(lightMap);
            return this;
      }

      @Nonnull
      public QuadBuilder setLightMap(int vertexIndex, LightMap lightMap) {
            this._faceData.setLightMap(vertexIndex, lightMap);
            return this;
      }
}
