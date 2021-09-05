package it.zerono.mods.zerocore.lib.client.render;

import it.zerono.mods.zerocore.lib.BlockFacings;
import it.zerono.mods.zerocore.lib.client.render.builder.CuboidBuilder;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.Cuboid;
import it.zerono.mods.zerocore.lib.math.LightMap;
import java.util.Iterator;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ModRenderHelper {
      private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

      public static void bindTexture(@Nonnull ResourceLocation textureLocation) {
            MINECRAFT.renderEngine.bindTexture(textureLocation);
      }

      public static void bindBlocksTexture() {
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      }

      public static TextureAtlasSprite getTextureSprite(ResourceLocation location) {
            return MINECRAFT.getTextureMapBlocks().getAtlasSprite(location.toString());
      }

      public static TextureAtlasSprite getFluidStillSprite(Fluid fluid) {
            return getTextureSprite(fluid.getStill());
      }

      public static TextureAtlasSprite getFluidStillSprite(FluidStack fluid) {
            return getTextureSprite(fluid.getFluid().getStill(fluid));
      }

      public static TextureAtlasSprite getFluidFlowingSprite(Fluid fluid) {
            return getTextureSprite(fluid.getFlowing());
      }

      public static TextureAtlasSprite getFluidFlowingSprite(FluidStack fluid) {
            return getTextureSprite(fluid.getFluid().getFlowing(fluid));
      }

      /** @deprecated */
      @Deprecated
      public static void glSetColor(int rgbColor) {
            float r = (float)(rgbColor >> 16 & 255) / 255.0F;
            float g = (float)(rgbColor >> 8 & 255) / 255.0F;
            float b = (float)(rgbColor >> 0 & 255) / 255.0F;
            GlStateManager.color(r, g, b);
      }

      /** @deprecated */
      @Deprecated
      public static void glSetColor(int rgbColor, float alpha) {
            float r = (float)(rgbColor >> 16 & 255) / 255.0F;
            float g = (float)(rgbColor >> 8 & 255) / 255.0F;
            float b = (float)(rgbColor >> 0 & 255) / 255.0F;
            GlStateManager.color(r, g, b, alpha);
      }

      public static void bufferFluidCube(@Nonnull BufferBuilder vertexBuffer, @Nonnull Cuboid cuboid, @Nonnull BlockFacings visibleFaces, @Nonnull Colour colour, @Nonnull LightMap lightMap, @Nonnull Fluid fluid) {
            CuboidBuilder builder = CuboidBuilder.getDefaultBuilder();
            TextureAtlasSprite still = getFluidStillSprite(fluid);
            TextureAtlasSprite flowing = getFluidFlowingSprite(fluid);
            builder.setCuboid(cuboid);
            builder.setColour(colour);
            builder.setLightMap(lightMap);
            builder.setVisibleFaces(visibleFaces);
            Iterator var9 = Plane.HORIZONTAL.iterator();

            EnumFacing facing;
            while(var9.hasNext()) {
                  facing = (EnumFacing)var9.next();
                  if (visibleFaces.isSet(facing)) {
                        builder.setTexture(facing, flowing);
                  }
            }

            var9 = Plane.VERTICAL.iterator();

            while(var9.hasNext()) {
                  facing = (EnumFacing)var9.next();
                  if (visibleFaces.isSet(facing)) {
                        builder.setTexture(facing, still);
                  }
            }

            builder.build().uploadVertexData(vertexBuffer);
      }

      public static void paintFluidCube(@Nonnull Cuboid cuboid, @Nonnull BlockFacings visibleFaces, @Nonnull Colour colour, @Nonnull LightMap lightMap, @Nonnull Fluid fluid) {
            BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
            vertexBuffer.begin(7, DefaultVertexFormats.BLOCK);
            bufferFluidCube(vertexBuffer, cuboid, visibleFaces, colour, lightMap, fluid);
            RenderHelper.disableStandardItemLighting();
            Tessellator.getInstance().draw();
            RenderHelper.enableStandardItemLighting();
      }

      /** @deprecated */
      @Deprecated
      public static void renderFluidCube(Fluid fluid, BlockFacings facesToDraw, double offsetX, double offsetY, double offsetZ, double x1, double y1, double z1, double x2, double y2, double z2, int color, int brightness) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            MINECRAFT.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            TextureAtlasSprite still = getTextureSprite(fluid.getStill());
            TextureAtlasSprite flowing = getTextureSprite(fluid.getFlowing());
            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.translate(offsetX, offsetY, offsetZ);
            EnumFacing[] H = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.EAST};
            EnumFacing[] V = new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP};
            ModRenderHelper.TexturedQuadData quadData = new ModRenderHelper.TexturedQuadData(buffer);
            double width = x2 - x1;
            double height = y2 - y1;
            double depth = z2 - z1;
            quadData.setColor(color);
            quadData.setBrightness(brightness);
            quadData.setCoordinates(x1, y1, z1, width, height, depth, true);

            EnumFacing face;
            int i;
            for(i = 0; i < 4; ++i) {
                  face = H[i];
                  if (facesToDraw.isSet(face)) {
                        quadData.setFace(face, flowing, true);
                        createTexturedQuad2(quadData);
                  }
            }

            quadData.setCoordinates(x1, y1, z1, width, height, depth, false);

            for(i = 0; i < 2; ++i) {
                  face = V[i];
                  if (facesToDraw.isSet(face)) {
                        quadData.setFace(face, still, false);
                        createTexturedQuad2(quadData);
                  }
            }

            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
      }

      /** @deprecated */
      @Deprecated
      public static void createTexturedQuad2(ModRenderHelper.TexturedQuadData data) {
            BufferBuilder vertexes = data.vertexes;
            if (null != vertexes) {
                  int alpha = data.alpha;
                  int red = data.red;
                  int green = data.green;
                  int blue = data.blue;
                  int light1 = data.light1;
                  int light2 = data.light2;
                  switch(data.face) {
                  case DOWN:
                        vertexes.pos(data.x1, data.y1, data.z1).color(red, green, blue, alpha).tex(data.minU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y1, data.z1).color(red, green, blue, alpha).tex(data.maxU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y1, data.z2).color(red, green, blue, alpha).tex(data.maxU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y1, data.z2).color(red, green, blue, alpha).tex(data.minU, data.maxV).lightmap(light1, light2).endVertex();
                        break;
                  case UP:
                        vertexes.pos(data.x1, data.y2, data.z1).color(red, green, blue, alpha).tex(data.minU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y2, data.z2).color(red, green, blue, alpha).tex(data.minU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y2, data.z2).color(red, green, blue, alpha).tex(data.maxU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y2, data.z1).color(red, green, blue, alpha).tex(data.maxU, data.minV).lightmap(light1, light2).endVertex();
                        break;
                  case NORTH:
                        vertexes.pos(data.x1, data.y1, data.z1).color(red, green, blue, alpha).tex(data.minU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y2, data.z1).color(red, green, blue, alpha).tex(data.minU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y2, data.z1).color(red, green, blue, alpha).tex(data.maxU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y1, data.z1).color(red, green, blue, alpha).tex(data.maxU, data.maxV).lightmap(light1, light2).endVertex();
                        break;
                  case SOUTH:
                        vertexes.pos(data.x1, data.y1, data.z2).color(red, green, blue, alpha).tex(data.maxU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y1, data.z2).color(red, green, blue, alpha).tex(data.minU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y2, data.z2).color(red, green, blue, alpha).tex(data.minU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y2, data.z2).color(red, green, blue, alpha).tex(data.maxU, data.minV).lightmap(light1, light2).endVertex();
                        break;
                  case WEST:
                        vertexes.pos(data.x1, data.y1, data.z1).color(red, green, blue, alpha).tex(data.maxU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y1, data.z2).color(red, green, blue, alpha).tex(data.minU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y2, data.z2).color(red, green, blue, alpha).tex(data.minU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x1, data.y2, data.z1).color(red, green, blue, alpha).tex(data.maxU, data.minV).lightmap(light1, light2).endVertex();
                        break;
                  case EAST:
                        vertexes.pos(data.x2, data.y1, data.z1).color(red, green, blue, alpha).tex(data.minU, data.maxV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y2, data.z1).color(red, green, blue, alpha).tex(data.minU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y2, data.z2).color(red, green, blue, alpha).tex(data.maxU, data.minV).lightmap(light1, light2).endVertex();
                        vertexes.pos(data.x2, data.y1, data.z2).color(red, green, blue, alpha).tex(data.maxU, data.maxV).lightmap(light1, light2).endVertex();
                  }

            }
      }

      private ModRenderHelper() {
      }

      /** @deprecated */
      @Deprecated
      public static class TexturedQuadData {
            public final BufferBuilder vertexes;
            public int alpha;
            public int red;
            public int green;
            public int blue;
            protected double x1;
            protected double y1;
            protected double z1;
            protected double x2;
            protected double y2;
            protected double z2;
            protected double xText1;
            protected double yText1;
            protected double zText1;
            protected double xText2;
            protected double yText2;
            protected double zText2;
            protected double minU;
            protected double maxU;
            protected double minV;
            protected double maxV;
            protected int light1;
            protected int light2;
            protected EnumFacing face;

            public TexturedQuadData(BufferBuilder buffer) {
                  this.vertexes = buffer;
                  this.alpha = this.red = this.green = this.blue = 255;
            }

            public void setBrightness(int brightness) {
                  this.light1 = brightness >> 16 & '\uffff';
                  this.light2 = brightness & '\uffff';
            }

            public void setColor(int color) {
                  this.alpha = color >> 24 & 255;
                  this.red = color >> 16 & 255;
                  this.green = color >> 8 & 255;
                  this.blue = color & 255;
            }

            public void setCoordinates(double x, double y, double z, double width, double height, double depth, boolean renderAsFlowingFluid) {
                  this.x1 = x;
                  this.x2 = x + width;
                  this.y1 = y;
                  this.y2 = y + height;
                  this.z1 = z;
                  this.z2 = z + depth;
                  this.xText1 = this.x1 % 1.0D;
                  this.xText2 = this.xText1 + width;
                  this.yText1 = this.y1 % 1.0D;
                  this.yText2 = this.yText1 + height;
                  this.zText1 = this.z1 % 1.0D;

                  for(this.zText2 = this.zText1 + depth; this.xText2 > 1.0D; --this.xText2) {
                  }

                  while(this.yText2 > 1.0D) {
                        --this.yText2;
                  }

                  while(this.zText2 > 1.0D) {
                        --this.zText2;
                  }

                  if (renderAsFlowingFluid) {
                        double swap = 1.0D - this.yText1;
                        this.yText1 = 1.0D - this.yText2;
                        this.yText2 = swap;
                  }

            }

            public void setFace(EnumFacing face, TextureAtlasSprite sprite, boolean renderAsFlowingFluid) {
                  double textSize = renderAsFlowingFluid ? 16.0D : 8.0D;
                  if (null == sprite) {
                        throw new RuntimeException("No sprite set while trying to create a texture quad!");
                  } else {
                        switch(face) {
                        case DOWN:
                        case UP:
                              this.minU = (double)sprite.getInterpolatedU(this.xText1 * textSize);
                              this.maxU = (double)sprite.getInterpolatedU(this.xText2 * textSize);
                              this.minV = (double)sprite.getInterpolatedV(this.zText1 * textSize);
                              this.maxV = (double)sprite.getInterpolatedV(this.zText2 * textSize);
                              break;
                        case NORTH:
                        case SOUTH:
                              this.minU = (double)sprite.getInterpolatedU(this.xText2 * textSize);
                              this.maxU = (double)sprite.getInterpolatedU(this.xText1 * textSize);
                              this.minV = (double)sprite.getInterpolatedV(this.yText1 * textSize);
                              this.maxV = (double)sprite.getInterpolatedV(this.yText2 * textSize);
                              break;
                        case WEST:
                        case EAST:
                              this.minU = (double)sprite.getInterpolatedU(this.zText2 * textSize);
                              this.maxU = (double)sprite.getInterpolatedU(this.zText1 * textSize);
                              this.minV = (double)sprite.getInterpolatedV(this.yText1 * textSize);
                              this.maxV = (double)sprite.getInterpolatedV(this.yText2 * textSize);
                              break;
                        default:
                              this.minU = (double)sprite.getMinU();
                              this.maxU = (double)sprite.getMaxU();
                              this.minV = (double)sprite.getMinV();
                              this.maxV = (double)sprite.getMaxV();
                        }

                        this.face = face;
                  }
            }
      }
}
