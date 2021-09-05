package trou.array_processing.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trou.array_processing.tile.TileEntityBase;

import javax.annotation.Nullable;

public class BlockArrayGlass extends BlockStructure {
    public BlockArrayGlass() {
        super("glass");
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBase();
    }

    @Override
    public boolean ActiveBlock(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        return false;
    }
}
