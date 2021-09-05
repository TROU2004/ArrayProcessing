package it.zerono.mods.zerocore.lib.gui;

import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ModGuiHandler implements IGuiHandler {
      public ModGuiHandler(Object modInstance) {
            NetworkRegistry.INSTANCE.registerGuiHandler(modInstance, this);
      }

      public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity te = WorldHelper.getTile((IBlockAccess)world, (BlockPos)(new BlockPos(x, y, z)));
            return te instanceof ModTileEntity ? ((ModTileEntity)te).getServerGuiElement(guiId, player) : null;
      }

      public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity te = WorldHelper.getTile((IBlockAccess)world, (BlockPos)(new BlockPos(x, y, z)));
            return te instanceof ModTileEntity ? ((ModTileEntity)te).getClientGuiElement(guiId, player) : null;
      }
}
