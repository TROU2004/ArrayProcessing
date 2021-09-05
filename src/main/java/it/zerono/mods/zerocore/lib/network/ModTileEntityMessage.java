package it.zerono.mods.zerocore.lib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class ModTileEntityMessage implements IMessage {
      private BlockPos _tilePosition;

      protected ModTileEntityMessage() {
            this._tilePosition = null;
      }

      protected ModTileEntityMessage(BlockPos position) {
            this._tilePosition = position;
      }

      protected ModTileEntityMessage(TileEntity tileEntity) {
            this._tilePosition = tileEntity.getPos();
      }

      public BlockPos getPos() {
            return this._tilePosition;
      }

      public void fromBytes(ByteBuf buffer) {
            int x = buffer.readInt();
            int y = buffer.readInt();
            int z = buffer.readInt();
            this._tilePosition = new BlockPos(x, y, z);
      }

      public void toBytes(ByteBuf buffer) {
            buffer.writeInt(this._tilePosition.getX());
            buffer.writeInt(this._tilePosition.getY());
            buffer.writeInt(this._tilePosition.getZ());
      }
}
