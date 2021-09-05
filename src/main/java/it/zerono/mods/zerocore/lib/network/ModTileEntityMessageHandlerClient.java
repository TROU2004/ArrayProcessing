package it.zerono.mods.zerocore.lib.network;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ModTileEntityMessageHandlerClient extends ModTileEntityMessageHandler {
      @SideOnly(Side.CLIENT)
      protected IThreadListener getThreadListener(MessageContext ctx) {
            return FMLClientHandler.instance().getClient();
      }

      @SideOnly(Side.CLIENT)
      protected World getWorld(MessageContext ctx) {
            return FMLClientHandler.instance().getClient().world;
      }
}
