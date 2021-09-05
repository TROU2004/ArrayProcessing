package it.zerono.mods.zerocore.lib.client.render;

import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Vertex implements IVertexSource {
      public final Vector3d POSITION;
      public final Vector3d NORMAL;
      public final UV UV;
      public final Colour COLOUR;
      public final LightMap LIGHT_MAP;

      public Vertex(@Nonnull Vector3d position, @Nonnull Colour colour) {
            this.POSITION = position;
            this.NORMAL = null;
            this.UV = null;
            this.COLOUR = colour;
            this.LIGHT_MAP = null;
      }

      public Vertex(@Nonnull Vector3d position, @Nonnull UV uv) {
            this.POSITION = position;
            this.NORMAL = null;
            this.UV = uv;
            this.COLOUR = null;
            this.LIGHT_MAP = null;
      }

      public Vertex(@Nonnull Vector3d position, @Nullable Vector3d normal, @Nullable UV uv, @Nullable Colour colour, @Nullable LightMap lightMap) {
            this.POSITION = position;
            this.NORMAL = normal;
            this.UV = uv;
            this.COLOUR = colour;
            this.LIGHT_MAP = lightMap;
      }

      public void uploadVertexData(@Nonnull BufferBuilder buffer) {
            Iterator var2 = buffer.getVertexFormat().getElements().iterator();

            while(var2.hasNext()) {
                  VertexFormatElement element = (VertexFormatElement)var2.next();
                  switch(element.getUsage()) {
                  case COLOR:
                        if (null != this.COLOUR) {
                              buffer.color(this.COLOUR.R, this.COLOUR.G, this.COLOUR.B, this.COLOUR.A);
                        }
                        break;
                  case UV:
                        if (0 == element.getIndex()) {
                              if (null != this.UV) {
                                    buffer.tex(this.UV.U, this.UV.V);
                              }
                        } else if (null != this.LIGHT_MAP) {
                              buffer.lightmap(this.LIGHT_MAP.SKY_LIGHT, this.LIGHT_MAP.BLOCK_LIGHT);
                        }
                        break;
                  case NORMAL:
                        if (null != this.NORMAL) {
                              buffer.normal((float)this.NORMAL.X, (float)this.NORMAL.Y, (float)this.NORMAL.Z);
                        }
                        break;
                  case POSITION:
                        buffer.pos(this.POSITION.X, this.POSITION.Y, this.POSITION.Z);
                  }
            }

            buffer.endVertex();
      }
}
