package it.zerono.mods.zerocore.api.multiblock.rectangular;

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.lib.BlockFacings;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RectangularMultiblockTileEntityBase extends MultiblockTileEntityBase {
      private PartPosition position;
      private BlockFacings outwardFacings;

      public RectangularMultiblockTileEntityBase() {
            this.position = PartPosition.Unknown;
            this.outwardFacings = BlockFacings.NONE;
      }

      @Nonnull
      public BlockFacings getOutwardsDir() {
            return this.outwardFacings;
      }

      @Nonnull
      public PartPosition getPartPosition() {
            return this.position;
      }

      @Nullable
      public EnumFacing getOutwardFacing() {
            EnumFacing facing = null != this.position ? this.position.getFacing() : null;
            if (null == facing) {
                  BlockFacings out = this.getOutwardsDir();
                  if (!out.none() && 1 == out.countFacesIf(true)) {
                        facing = out.firstIf(true);
                  }
            }

            return facing;
      }

      @Nullable
      public EnumFacing getOutwardFacingFromWorldPosition() {
            BlockFacings facings = null;
            MultiblockControllerBase controller = this.getMultiblockController();
            if (null != controller) {
                  BlockPos position = this.getWorldPosition();
                  BlockPos min = controller.getMinimumCoord();
                  BlockPos max = controller.getMaximumCoord();
                  int x = position.getX();
                  int y = position.getY();
                  int z = position.getZ();
                  facings = BlockFacings.from(min.getY() == y, max.getY() == y, min.getZ() == z, max.getZ() == z, min.getX() == x, max.getX() == x);
            }

            return null != facings && !facings.none() && 1 == facings.countFacesIf(true) ? facings.firstIf(true) : null;
      }

      public void notifyOutwardNeighborsOfStateChange() {
            Block blockType = this.getBlockType();
            BlockFacings facings = this.getOutwardsDir();
            BlockPos position = this.getWorldPosition();
            World world = this.getWorld();
            EnumFacing[] var5 = EnumFacing.VALUES;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                  EnumFacing facing = var5[var7];
                  if (facings.isSet(facing)) {
                        WorldHelper.notifyNeighborsOfStateChange(world, position.offset(facing), blockType);
                  }
            }

      }

      public void onAttached(MultiblockControllerBase newController) {
            super.onAttached(newController);
            this.recalculateOutwardsDirection(newController.getMinimumCoord(), newController.getMaximumCoord());
      }

      /** @deprecated */
      @Deprecated
      public void onMachineAssembled(MultiblockControllerBase controller) {
      }

      public void onPreMachineAssembled(MultiblockControllerBase controller) {
            this.recalculateOutwardsDirection(controller.getMinimumCoord(), controller.getMaximumCoord());
      }

      public void onPostMachineAssembled(MultiblockControllerBase controller) {
      }

      /** @deprecated */
      @Deprecated
      public void onMachineBroken() {
      }

      public void onPreMachineBroken() {
      }

      public void onPostMachineBroken() {
            this.position = PartPosition.Unknown;
            this.outwardFacings = BlockFacings.NONE;
      }

      public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord) {
            BlockPos myPosition = this.getPos();
            int myX = myPosition.getX();
            int myY = myPosition.getY();
            int myZ = myPosition.getZ();
            int facesMatching = 0;
            boolean downFacing = myY == minCoord.getY();
            boolean upFacing = myY == maxCoord.getY();
            boolean northFacing = myZ == minCoord.getZ();
            boolean southFacing = myZ == maxCoord.getZ();
            boolean westFacing = myX == minCoord.getX();
            boolean eastFacing = myX == maxCoord.getX();
            this.outwardFacings = BlockFacings.from(downFacing, upFacing, northFacing, southFacing, westFacing, eastFacing);
            if (eastFacing || westFacing) {
                  ++facesMatching;
            }

            if (upFacing || downFacing) {
                  ++facesMatching;
            }

            if (southFacing || northFacing) {
                  ++facesMatching;
            }

            if (facesMatching <= 0) {
                  this.position = PartPosition.Interior;
            } else if (facesMatching >= 3) {
                  this.position = PartPosition.FrameCorner;
            } else if (facesMatching == 2) {
                  if (!eastFacing && !westFacing) {
                        this.position = PartPosition.FrameEastWest;
                  } else if (!southFacing && !northFacing) {
                        this.position = PartPosition.FrameSouthNorth;
                  } else {
                        this.position = PartPosition.FrameUpDown;
                  }
            } else if (eastFacing) {
                  this.position = PartPosition.EastFace;
            } else if (westFacing) {
                  this.position = PartPosition.WestFace;
            } else if (southFacing) {
                  this.position = PartPosition.SouthFace;
            } else if (northFacing) {
                  this.position = PartPosition.NorthFace;
            } else if (upFacing) {
                  this.position = PartPosition.TopFace;
            } else {
                  this.position = PartPosition.BottomFace;
            }

      }

      public abstract boolean isGoodForFrame(IMultiblockValidator var1);

      public abstract boolean isGoodForSides(IMultiblockValidator var1);

      public abstract boolean isGoodForTop(IMultiblockValidator var1);

      public abstract boolean isGoodForBottom(IMultiblockValidator var1);

      public abstract boolean isGoodForInterior(IMultiblockValidator var1);
}
