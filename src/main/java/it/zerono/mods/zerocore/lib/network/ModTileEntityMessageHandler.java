package it.zerono.mods.zerocore.lib.network;

import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class ModTileEntityMessageHandler extends ModMessageHandler {
      protected void processMessage(ModTileEntityMessage message, MessageContext ctx) {
            World world = this.getWorld(ctx);
            BlockPos position = message.getPos();
            if (null == world) {
                  ZeroCore.getLogger().error("Invalid world instance found while processing a ModTileEntityMessage: skipping message");
            } else if (null == position) {
                  ZeroCore.getLogger().error("Invalid tile entity position in a ModTileEntityMessage: skipping message");
            } else {
                  TileEntity tileEntity = WorldHelper.getTile((IBlockAccess)world, (BlockPos)position);
                  if (null != tileEntity) {
                        this.processTileEntityMessage(message, ctx, tileEntity);
                  } else {
                        ZeroCore.getLogger().error("Invalid tile entity found at %d, %d, %d while processing a ModTileEntityMessage: skipping message", position.getX(), position.getY(), position.getZ());
                  }
            }
      }

      protected abstract World getWorld(MessageContext var1);

      protected abstract void processTileEntityMessage(ModTileEntityMessage var1, MessageContext var2, TileEntity var3);
}
