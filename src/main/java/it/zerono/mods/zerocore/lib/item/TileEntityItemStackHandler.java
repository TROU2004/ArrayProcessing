package it.zerono.mods.zerocore.lib.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityItemStackHandler extends ItemStackHandler {
      private TileEntity _linkedTE;

      public TileEntityItemStackHandler(TileEntity linkedTileEntity) {
            super(1);
            this._linkedTE = linkedTileEntity;
      }

      public TileEntityItemStackHandler(TileEntity linkedTileEntity, int size) {
            super(size);
            this._linkedTE = linkedTileEntity;
      }

      public TileEntityItemStackHandler(TileEntity linkedTileEntity, NonNullList stacks) {
            super(stacks);
            this._linkedTE = linkedTileEntity;
      }

      protected void onContentsChanged(int slot) {
            this._linkedTE.markDirty();
      }
}
