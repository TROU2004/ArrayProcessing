package it.zerono.mods.zerocore.lib.block.properties;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class Orientation {
      public static final PropertyDirection FACING = PropertyDirection.create("facing");
      public static final PropertyDirection HFACING;
      public static final PropertyDirection VFACING;

      public static EnumFacing suggestDefaultHorizontalFacing(World world, BlockPos position, EnumFacing currentFacing) {
            EnumFacing oppositeFacing = currentFacing.getOpposite();
            IBlockState facingState = world.getBlockState(position.offset(currentFacing));
            IBlockState oppositeState = world.getBlockState(position.offset(oppositeFacing));
            Block facingBlock = facingState.getBlock();
            Block oppositeBlock = oppositeState.getBlock();
            return facingBlock.isFullBlock(facingState) && !oppositeBlock.isFullBlock(oppositeState) ? oppositeFacing : currentFacing;
      }

      static {
            HFACING = PropertyDirection.create("hfacing", Plane.HORIZONTAL);
            VFACING = PropertyDirection.create("vfacing", Plane.VERTICAL);
      }
}
