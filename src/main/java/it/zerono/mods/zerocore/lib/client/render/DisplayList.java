package it.zerono.mods.zerocore.lib.client.render;

import net.minecraft.client.renderer.GlStateManager;

public final class DisplayList {
      private final int _id = GlStateManager.glGenLists(1);

      public void finalize() {
            GlStateManager.glDeleteLists(this._id, 1);
      }

      public void beginList() {
            GlStateManager.glNewList(this._id, 4864);
      }

      public void endList() {
            GlStateManager.glEndList();
      }

      public void play() {
            GlStateManager.callList(this._id);
      }
}
