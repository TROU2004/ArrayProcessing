package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.client.render.Shape;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractShapeBuilder {
      private final boolean _autoReset;

      public abstract Shape build();

      public abstract void reset();

      protected AbstractShapeBuilder(boolean autoReset) {
            this._autoReset = autoReset;
      }

      protected boolean autoReset() {
            return this._autoReset;
      }

      protected static class PolygonalFaceData {
            public final int VERTICES_COUNT;
            public final Vector3d[] NORMALS;
            public final UV[] UV_MAP;
            public final Colour[] COLOURS;
            public final LightMap[] LIGHT_MAPS;
            private final EnumSet _filledElements;

            public PolygonalFaceData(int vertexCount) {
                  this.VERTICES_COUNT = vertexCount;
                  this.NORMALS = new Vector3d[vertexCount];
                  this.UV_MAP = new UV[vertexCount];
                  this.COLOURS = new Colour[vertexCount];
                  this.LIGHT_MAPS = new LightMap[vertexCount];
                  this._filledElements = EnumSet.noneOf(VertexElementType.class);
            }

            public void reset() {
                  this._filledElements.clear();

                  for(int idx = 0; idx < this.VERTICES_COUNT; ++idx) {
                        this.NORMALS[idx] = null;
                        this.UV_MAP[idx] = null;
                        this.COLOURS[idx] = null;
                        this.LIGHT_MAPS[idx] = null;
                  }

            }

            @Nullable
            public Vector3d getNormalAt(int vertexIndex) {
                  return this.checkElement(VertexElementType.Normal) ? this.NORMALS[vertexIndex] : null;
            }

            @Nullable
            public UV getUvAt(int vertexIndex) {
                  return this.checkElement(VertexElementType.Texture) ? this.UV_MAP[vertexIndex] : null;
            }

            @Nullable
            public Colour getColourlAt(int vertexIndex) {
                  return this.checkElement(VertexElementType.Colour) ? this.COLOURS[vertexIndex] : null;
            }

            @Nullable
            public LightMap getLightMapAt(int vertexIndex) {
                  return this.checkElement(VertexElementType.LightMap) ? this.LIGHT_MAPS[vertexIndex] : null;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setNormal(@Nonnull Vector3d normal) {
                  for(int vertexIndex = 0; vertexIndex < this.NORMALS.length; ++vertexIndex) {
                        this.NORMALS[vertexIndex] = normal;
                  }

                  this.addElement(VertexElementType.Normal);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setNormal(int vertexIndex, @Nonnull Vector3d normal) {
                  this.NORMALS[vertexIndex] = normal;
                  this.addElement(VertexElementType.Normal);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setTexture(@Nonnull UV texture) {
                  for(int vertexIndex = 0; vertexIndex < this.UV_MAP.length; ++vertexIndex) {
                        this.UV_MAP[vertexIndex] = texture;
                  }

                  this.addElement(VertexElementType.Texture);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setTexture(int vertexIndex, @Nonnull UV texture) {
                  this.UV_MAP[vertexIndex] = texture;
                  this.addElement(VertexElementType.Texture);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setTexture(@Nonnull TextureAtlasSprite sprite) {
                  if (4 != this.VERTICES_COUNT) {
                        throw new IllegalArgumentException("This polygonal face does not have 4 vertices");
                  } else {
                        UV a = new UV((double)sprite.getMinU(), (double)sprite.getMinV());
                        UV b = new UV((double)sprite.getMinU(), (double)sprite.getMaxV());
                        UV c = new UV((double)sprite.getMaxU(), (double)sprite.getMaxV());
                        UV d = new UV((double)sprite.getMaxU(), (double)sprite.getMinV());
                        this.setTexture(0, a);
                        this.setTexture(1, b);
                        this.setTexture(2, c);
                        this.setTexture(3, d);
                        return this;
                  }
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setColour(@Nonnull Colour colour) {
                  for(int vertexIndex = 0; vertexIndex < this.COLOURS.length; ++vertexIndex) {
                        this.COLOURS[vertexIndex] = colour;
                  }

                  this.addElement(VertexElementType.Colour);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setColour(int vertexIndex, @Nonnull Colour colour) {
                  this.COLOURS[vertexIndex] = colour;
                  this.addElement(VertexElementType.Colour);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setLightMap(@Nonnull LightMap lightMap) {
                  for(int vertexIndex = 0; vertexIndex < this.LIGHT_MAPS.length; ++vertexIndex) {
                        this.LIGHT_MAPS[vertexIndex] = lightMap;
                  }

                  this.addElement(VertexElementType.LightMap);
                  return this;
            }

            @Nonnull
            public AbstractShapeBuilder.PolygonalFaceData setLightMap(int vertexIndex, @Nonnull LightMap lightMap) {
                  this.LIGHT_MAPS[vertexIndex] = lightMap;
                  this.addElement(VertexElementType.LightMap);
                  return this;
            }

            protected boolean checkElement(@Nonnull VertexElementType element) {
                  return this._filledElements.contains(element);
            }

            protected void addElement(@Nonnull VertexElementType element) {
                  this._filledElements.add(element);
            }
      }
}
