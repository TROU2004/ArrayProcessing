package trou.array_processing.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import trou.array_processing.tile.TileArrayItemHatch;

import javax.annotation.Nullable;

public class BlockArrayInputHatch extends BlockStructure{
    public BlockArrayInputHatch() {
        super("input_hatch");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileArrayItemHatch(false);
    }

    @Override
    public boolean ActiveBlock(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        return false;
    }
}
