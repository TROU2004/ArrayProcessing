package it.zerono.mods.zerocore.api.multiblock;

import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MultiblockTileEntityBase extends ModTileEntity implements IMultiblockPart {
      private MultiblockControllerBase controller = null;
      private boolean visited = false;
      private boolean saveMultiblockData = false;
      private NBTTagCompound cachedMultiblockData = null;
      private boolean paused = false;
      private static final IMultiblockRegistry REGISTRY = ZeroCore.getProxy().initMultiblockRegistry();

      public Set attachToNeighbors() {
            Set controllers = null;
            MultiblockControllerBase bestController = null;
            IMultiblockPart[] partsToCheck = this.getNeighboringParts();
            IMultiblockPart[] var4 = partsToCheck;
            int var5 = partsToCheck.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                  IMultiblockPart neighborPart = var4[var6];
                  if (neighborPart.isConnected()) {
                        MultiblockControllerBase candidate = neighborPart.getMultiblockController();
                        if (candidate.getClass().equals(this.getMultiblockControllerType())) {
                              if (controllers == null) {
                                    controllers = new HashSet();
                                    bestController = candidate;
                              } else if (!controllers.contains(candidate) && candidate.shouldConsume(bestController)) {
                                    bestController = candidate;
                              }

                              controllers.add(candidate);
                        }
                  }
            }

            if (bestController != null) {
                  this.controller = bestController;
                  bestController.attachBlock(this);
            }

            return controllers;
      }

      public void assertDetached() {
            if (this.controller != null) {
                  BlockPos coord = this.getWorldPosition();
                  ZeroCore.getLogger().info("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual.", coord.getX(), coord.getY(), coord.getZ());
                  this.controller = null;
            }

      }

      protected void syncDataFrom(NBTTagCompound data, ModTileEntity.SyncReason syncReason) {
            if (ModTileEntity.SyncReason.FullSync == syncReason) {
                  if (data.hasKey("multiblockData")) {
                        this.cachedMultiblockData = data.getCompoundTag("multiblockData");
                  }
            } else if (data.hasKey("multiblockData")) {
                  NBTTagCompound tag = data.getCompoundTag("multiblockData");
                  if (this.isConnected()) {
                        this.getMultiblockController().syncDataFrom(tag, syncReason);
                  } else {
                        this.cachedMultiblockData = tag;
                  }
            }

      }

      protected void syncDataTo(NBTTagCompound data, ModTileEntity.SyncReason syncReason) {
            NBTTagCompound multiblockData;
            if (ModTileEntity.SyncReason.FullSync == syncReason) {
                  if (this.isMultiblockSaveDelegate() && this.isConnected()) {
                        multiblockData = new NBTTagCompound();
                        this.controller.syncDataTo(multiblockData, syncReason);
                        data.setTag("multiblockData", multiblockData);
                  }
            } else if (this.isMultiblockSaveDelegate() && this.isConnected()) {
                  multiblockData = new NBTTagCompound();
                  this.getMultiblockController().syncDataTo(multiblockData, syncReason);
                  data.setTag("multiblockData", multiblockData);
            }

      }

      public void invalidate() {
            super.invalidate();
            this.detachSelf(false);
      }

      public void onChunkUnload() {
            super.onChunkUnload();
            this.detachSelf(true);
      }

      public void validate() {
            super.validate();
            REGISTRY.onPartAdded(this.getWorld(), this);
      }

      public boolean hasMultiblockSaveData() {
            return this.cachedMultiblockData != null;
      }

      public NBTTagCompound getMultiblockSaveData() {
            return this.cachedMultiblockData;
      }

      public void onMultiblockDataAssimilated() {
            this.cachedMultiblockData = null;
      }

      /** @deprecated */
      @Deprecated
      public abstract void onMachineAssembled(MultiblockControllerBase var1);

      public abstract void onPreMachineAssembled(MultiblockControllerBase var1);

      public abstract void onPostMachineAssembled(MultiblockControllerBase var1);

      /** @deprecated */
      @Deprecated
      public abstract void onMachineBroken();

      public abstract void onPreMachineBroken();

      public abstract void onPostMachineBroken();

      public abstract void onMachineActivated();

      public abstract void onMachineDeactivated();

      public boolean isConnected() {
            return this.controller != null;
      }

      public boolean isMachineAssembled() {
            return null != this.controller && this.controller.isAssembled();
      }

      public boolean isMachineDisassembled() {
            return null != this.controller && this.controller.isDisassembled();
      }

      public boolean isMachinePaused() {
            return null != this.controller && this.controller.isPaused();
      }

      public MultiblockControllerBase getMultiblockController() {
            return this.controller;
      }

      public void becomeMultiblockSaveDelegate() {
            this.saveMultiblockData = true;
      }

      public void forfeitMultiblockSaveDelegate() {
            this.saveMultiblockData = false;
      }

      public boolean isMultiblockSaveDelegate() {
            return this.saveMultiblockData;
      }

      public void setUnvisited() {
            this.visited = false;
      }

      public void setVisited() {
            this.visited = true;
      }

      public boolean isVisited() {
            return this.visited;
      }

      public void onAssimilated(MultiblockControllerBase newController) {
            assert this.controller != newController;

            this.controller = newController;
      }

      public void onAttached(MultiblockControllerBase newController) {
            this.controller = newController;
      }

      public void onDetached(MultiblockControllerBase oldController) {
            this.controller = null;
      }

      public abstract MultiblockControllerBase createNewMultiblock();

      public IMultiblockPart[] getNeighboringParts() {
            List neighborParts = new ArrayList();
            BlockPos partPosition = this.getWorldPosition();
            World world = this.getWorld();
            EnumFacing[] var6 = EnumFacing.VALUES;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                  EnumFacing facing = var6[var8];
                  BlockPos neighborPosition = partPosition.offset(facing);
                  TileEntity te = WorldHelper.getTile((IBlockAccess)world, (BlockPos)neighborPosition);
                  if (te instanceof IMultiblockPart) {
                        neighborParts.add((IMultiblockPart)te);
                  }
            }

            return (IMultiblockPart[])neighborParts.toArray(new IMultiblockPart[neighborParts.size()]);
      }

      public void onOrphaned(MultiblockControllerBase controller, int oldSize, int newSize) {
            this.markDirty();
            this.getWorld().markChunkDirty(this.getWorldPosition(), this);
      }

      public BlockPos getWorldPosition() {
            return this.pos;
      }

      public boolean isPartInvalid() {
            return this.isInvalid();
      }

      protected void notifyNeighborsOfBlockChange() {
            WorldHelper.notifyNeighborsOfStateChange(this.getWorld(), this.getWorldPosition(), this.getBlockType());
      }

      /** @deprecated */
      @Deprecated
      protected void notifyNeighborsOfTileChange() {
      }

      protected void detachSelf(boolean chunkUnloading) {
            if (this.controller != null) {
                  this.controller.detachBlock(this, chunkUnloading);
                  this.controller = null;
            }

            REGISTRY.onPartRemovedFromWorld(this.getWorld(), this);
      }

      protected void markMultiblockForRenderUpdate() {
            MultiblockControllerBase controller = this.getMultiblockController();
            if (null != controller) {
                  controller.markMultiblockForRenderUpdate();
            }

      }
}
