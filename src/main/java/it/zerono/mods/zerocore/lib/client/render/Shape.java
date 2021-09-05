package it.zerono.mods.zerocore.lib.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Shape implements IVertexSource {
      private final List _vertices;

      public Shape() {
            this(4);
      }

      public Shape(int initialSize) {
            this._vertices = Lists.newArrayListWithCapacity(initialSize);
      }

      public void addVertex(@Nonnull Vertex vertex) {
            this._vertices.add(vertex);
      }

      @Nonnull
      public ImmutableList getVertices() {
            return ImmutableList.copyOf(this._vertices);
      }

      public int getVerticesCount() {
            return this._vertices.size();
      }

      public void uploadVertexData(@Nonnull BufferBuilder buffer) {
            Iterator var2 = this._vertices.iterator();

            while(var2.hasNext()) {
                  Vertex vertex = (Vertex)var2.next();
                  vertex.uploadVertexData(buffer);
            }

      }
}
