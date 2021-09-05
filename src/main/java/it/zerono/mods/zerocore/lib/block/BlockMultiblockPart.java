package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import it.zerono.mods.zerocore.util.CodeHelper;
import it.zerono.mods.zerocore.util.ItemHelper;
import javax.annotation.Nonnull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMultiblockPart extends ModBlock {
      protected final Enum _partType;

      public BlockMultiblockPart(@Nonnull Enum type, @Nonnull String blockName, @Nonnull Material material) {
            super(blockName, material);
            this._partType = type;
      }

      @Nonnull
      public Enum getPartType() {
            return this._partType;
      }

      public boolean hasTileEntity(IBlockState state) {
            return true;
      }

      public TileEntity createTileEntity(World world, IBlockState state) {
            return ((IMultiblockPartType)this.getPartType()).createTileEntity(world, state);
      }

      public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos position) {
            TileEntity te = WorldHelper.getTile(world, position);
            if (te instanceof MultiblockTileEntityBase) {
                  state = this.buildActualState(state, world, position, (MultiblockTileEntityBase)te);
            }

            return state;
      }

      public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos position, EnumFacing side) {
            IBlockState sideState = blockAccess.getBlockState(position.offset(side));
            return this != sideState.getBlock();
      }

      public boolean onBlockActivated(World world, BlockPos position, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
            if (this.hasTileEntity(state) && !player.isSneaking()) {
                  ItemStack heldItem = player.getHeldItem(hand);
                  TileEntity te = WorldHelper.getTile((IBlockAccess)world, (BlockPos)position);
                  if (te instanceof IMultiblockPart && WorldHelper.calledByLogicalServer(world) && ItemHelper.stackIsEmpty(heldItem) && hand == EnumHand.OFF_HAND) {
                        MultiblockControllerBase controller = ((IMultiblockPart)te).getMultiblockController();
                        ITextComponent message = null;
                        if (null != controller) {
                              ValidationError error = controller.getLastError();
                              if (null != error) {
                                    message = error.getChatMessage();
                              }
                        } else {
                              message = new TextComponentTranslation("multiblock.validation.block_not_connected", new Object[0]);
                        }

                        if (null != message) {
                              CodeHelper.sendStatusMessage(player, (ITextComponent)message);
                              return true;
                        }
                  }
            }

            return super.onBlockActivated(world, position, state, player, hand, side, hitX, hitY, hitZ);
      }

      @Nonnull
      protected IBlockState buildActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos position, @Nonnull MultiblockTileEntityBase part) {
            return state;
      }
}
