package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.BlockFacings;
import it.zerono.mods.zerocore.lib.client.render.Shape;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.Cuboid;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CuboidBuilder extends AbstractShapeBuilder {
      public static final int VERTICES_COUNT = 8;
      public static final int FACES_COUNT;
      private final AbstractShapeBuilder.PolygonalFaceData[] _cuboidData;
      private Cuboid _cuboid;
      private BlockFacings _facesToBeRendered;
      private static CuboidBuilder s_defaultBuilder;

      @Nonnull
      public static CuboidBuilder getDefaultBuilder() {
            if (null == s_defaultBuilder) {
                  s_defaultBuilder = new CuboidBuilder(false);
            }

            return s_defaultBuilder;
      }

      public CuboidBuilder(boolean autoReset) {
            super(autoReset);
            this._cuboidData = new AbstractShapeBuilder.PolygonalFaceData[FACES_COUNT];
            this._facesToBeRendered = BlockFacings.ALL;

            for(int faceIndex = 0; faceIndex < this._cuboidData.length; ++faceIndex) {
                  this._cuboidData[faceIndex] = new AbstractShapeBuilder.PolygonalFaceData(4);
            }

      }

      @Nonnull
      public Shape build() {
            if (null == this._cuboid) {
                  throw new IllegalStateException("No cuboid was provided!");
            } else {
                  Shape shape = new Shape(8);
                  VertexBuilder vertexBuilder = new VertexBuilder(true);
                  EnumFacing[] var3 = EnumFacing.VALUES;
                  int var4 = var3.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                        EnumFacing facing = var3[var5];
                        if (this._facesToBeRendered.isSet(facing)) {
                              AbstractShapeBuilder.PolygonalFaceData data = this._cuboidData[facing.getIndex()];

                              for(int vertexIndex = 0; vertexIndex < data.VERTICES_COUNT; ++vertexIndex) {
                                    shape.addVertex(QuadBuilder.buildSingleVertex(vertexIndex, vertexBuilder, this._cuboid.getFace(facing), data));
                              }
                        }
                  }

                  if (this.autoReset()) {
                        this.reset();
                  }

                  return shape;
            }
      }

      public void reset() {
            this._cuboid = null;
            this._facesToBeRendered = BlockFacings.ALL;

            for(int idx = 0; idx < this._cuboidData.length; ++idx) {
                  this._cuboidData[idx].reset();
            }

      }

      @Nonnull
      public CuboidBuilder setCuboid(@Nonnull Cuboid cuboid) {
            this._cuboid = cuboid;
            return this;
      }

      @Nonnull
      public CuboidBuilder setVisibleFaces(@Nonnull BlockFacings visibleFaces) {
            this._facesToBeRendered = visibleFaces;
            return this;
      }

      @Nonnull
      public CuboidBuilder setFaceVisibility(@Nonnull EnumFacing face, boolean visible) {
            this._facesToBeRendered = this._facesToBeRendered.set(face, visible);
            return this;
      }

      @Nonnull
      public CuboidBuilder setColour(@Nonnull Colour colour) {
            for(int idx = 0; idx < this._cuboidData.length; ++idx) {
                  this._cuboidData[idx].setColour(colour);
            }

            return this;
      }

      @Nonnull
      public CuboidBuilder setColour(@Nonnull EnumFacing facing, @Nonnull Colour colour) {
            this._cuboidData[facing.getIndex()].setColour(colour);
            return this;
      }

      @Nonnull
      public CuboidBuilder setColour(@Nonnull EnumFacing facing, int vertexIndex, @Nonnull Colour colour) {
            this._cuboidData[facing.getIndex()].setColour(vertexIndex, colour);
            return this;
      }

      @Nonnull
      public CuboidBuilder setTexture(@Nonnull UV a, @Nonnull UV b, @Nonnull UV c, @Nonnull UV d) {
            for(int faceIdx = 0; faceIdx < this._cuboidData.length; ++faceIdx) {
                  AbstractShapeBuilder.PolygonalFaceData data = this._cuboidData[faceIdx];
                  data.setTexture(0, a);
                  data.setTexture(1, b);
                  data.setTexture(2, c);
                  data.setTexture(3, d);
            }

            return this;
      }

      @Nonnull
      public CuboidBuilder setTexture(@Nonnull EnumFacing facing, @Nonnull UV a, @Nonnull UV b, @Nonnull UV c, @Nonnull UV d) {
            AbstractShapeBuilder.PolygonalFaceData data = this._cuboidData[facing.getIndex()];
            data.setTexture(0, a);
            data.setTexture(1, b);
            data.setTexture(2, c);
            data.setTexture(3, d);
            return this;
      }

      @Nonnull
      public CuboidBuilder setTexture(@Nonnull EnumFacing facing, int vertexIndex, @Nonnull UV uv) {
            AbstractShapeBuilder.PolygonalFaceData data = this._cuboidData[facing.getIndex()];
            data.setTexture(vertexIndex, uv);
            return this;
      }

      @Nonnull
      public CuboidBuilder setTexture(@Nonnull TextureAtlasSprite sprite) {
            for(int faceIdx = 0; faceIdx < this._cuboidData.length; ++faceIdx) {
                  this._cuboidData[faceIdx].setTexture(sprite);
            }

            return this;
      }

      @Nonnull
      public CuboidBuilder setTexture(@Nonnull EnumFacing facing, @Nonnull TextureAtlasSprite sprite) {
            this._cuboidData[facing.getIndex()].setTexture(sprite);
            return this;
      }

      @Nonnull
      public CuboidBuilder setLightMap(@Nonnull LightMap lightMap) {
            for(int idx = 0; idx < this._cuboidData.length; ++idx) {
                  this._cuboidData[idx].setLightMap(lightMap);
            }

            return this;
      }

      @Nonnull
      public CuboidBuilder setLightMap(@Nonnull EnumFacing facing, @Nonnull LightMap lightMap) {
            this._cuboidData[facing.getIndex()].setLightMap(lightMap);
            return this;
      }

      @Nonnull
      public CuboidBuilder setLightMap(@Nonnull EnumFacing facing, int vertexIndex, @Nonnull LightMap lightMap) {
            this._cuboidData[facing.getIndex()].setLightMap(vertexIndex, lightMap);
            return this;
      }

      static {
            FACES_COUNT = EnumFacing.VALUES.length;
            s_defaultBuilder = null;
      }
}
