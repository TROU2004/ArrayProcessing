package trou.array_processing.block;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import trou.array_processing.ArrayProcessing;
import trou.array_processing.multiblock.ProcessingArrayController;

import javax.annotation.Nullable;

public abstract class BlockStructure extends Block {
    Class<? extends TileEntity> tile = null;
    final String identity;
    public BlockStructure(String identity, Class<? extends TileEntity> tile) {
        this(identity);
        this.tile = tile;
    }

    public BlockStructure(String identity) {
        super(Material.ROCK);
        this.setRegistryName("array_" + identity);
        this.setTranslationKey(ArrayProcessing.MODID + ".array_" + identity);
        this.setCreativeTab(ArrayProcessing.TAB);
        this.identity = identity;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public void registerModelItem() {
        ModelLoader.setCustomModelResourceLocation(new ItemBlock(this), 0, new ModelResourceLocation(new ResourceLocation(ArrayProcessing.MODID, "array" + identity), "inventory"));
    }

    public void registerTileEntity() {
        if (tile != null) {
            GameRegistry.registerTileEntity(tile, new ResourceLocation(ArrayProcessing.MODID, "array_" + identity));
        }
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(World world, IBlockState state);

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND && playerIn.getHeldItemMainhand().isEmpty()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof IMultiblockPart) {
                ProcessingArrayController controller = (ProcessingArrayController) ((IMultiblockPart) te).getMultiblockController();
                if (controller != null && controller.getLastError() != null) {
                    playerIn.sendMessage(controller.getLastError().getChatMessage());
                } else {
                    return ActiveBlock(worldIn, pos, state, playerIn);
                }
            }
        }
        return false;
    }

    public abstract boolean ActiveBlock(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn);
}
