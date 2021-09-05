package it.zerono.mods.zerocore.api.multiblock;

import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public abstract class MultiblockControllerBase implements IMultiblockValidator {
      public static final short DIMENSION_UNBOUNDED = -1;
      public final World WORLD;
      protected MultiblockControllerBase.AssemblyState assemblyState;
      protected HashSet<IMultiblockPart> connectedParts;
      private BlockPos referenceCoord;
      private BlockPos minimumCoord;
      private BlockPos maximumCoord;
      private boolean shouldCheckForDisconnections;
      private ValidationError lastValidationError;
      protected boolean debugMode;
      private static final IMultiblockRegistry REGISTRY = ZeroCore.getProxy().initMultiblockRegistry();

      protected MultiblockControllerBase(World world) {
            this.WORLD = world;
            this.connectedParts = new HashSet();
            this.referenceCoord = null;
            this.assemblyState = MultiblockControllerBase.AssemblyState.Disassembled;
            this.minimumCoord = null;
            this.maximumCoord = null;
            this.shouldCheckForDisconnections = true;
            this.lastValidationError = null;
            this.debugMode = false;
      }

      public void setDebugMode(boolean active) {
            this.debugMode = active;
      }

      public boolean isDebugMode() {
            return this.debugMode;
      }

      public abstract void onAttachedPartWithMultiblockData(IMultiblockPart var1, NBTTagCompound var2);

      public boolean hasBlock(BlockPos blockCoord) {
            return this.connectedParts.contains(blockCoord);
      }

      public void attachBlock(IMultiblockPart part) {
            BlockPos coord = part.getWorldPosition();
            if (!this.connectedParts.add(part)) {
                  ZeroCore.getLogger().warn("[%s] Controller %s is double-adding part %d @ %s. This is unusual. If you encounter odd behavior, please tear down the machine and rebuild it.", this.WORLD.isRemote ? "CLIENT" : "SERVER", this.hashCode(), part.hashCode(), coord);
            }

            part.onAttached(this);
            this.onBlockAdded(part);
            if (part.hasMultiblockSaveData()) {
                  NBTTagCompound savedData = part.getMultiblockSaveData();
                  this.onAttachedPartWithMultiblockData(part, savedData);
                  part.onMultiblockDataAssimilated();
            }

            if (this.referenceCoord == null) {
                  this.referenceCoord = coord;
                  part.becomeMultiblockSaveDelegate();
            } else if (coord.compareTo(this.referenceCoord) < 0) {
                  TileEntity te = this.getTile();
                  if (null != te) {
                        ((IMultiblockPart)te).forfeitMultiblockSaveDelegate();
                  }

                  this.referenceCoord = coord;
                  part.becomeMultiblockSaveDelegate();
            } else {
                  part.forfeitMultiblockSaveDelegate();
            }

            BlockPos partPos = part.getWorldPosition();
            int curX;
            int curY;
            int curZ;
            int newX;
            int newY;
            int newZ;
            int partCoord;
            if (this.minimumCoord != null) {
                  curX = this.minimumCoord.getX();
                  curY = this.minimumCoord.getY();
                  curZ = this.minimumCoord.getZ();
                  partCoord = partPos.getX();
                  newX = partCoord < curX ? partCoord : curX;
                  partCoord = partPos.getY();
                  newY = partCoord < curY ? partCoord : curY;
                  partCoord = partPos.getZ();
                  newZ = partCoord < curZ ? partCoord : curZ;
                  if (newX != curX || newY != curY || newZ != curZ) {
                        this.minimumCoord = new BlockPos(newX, newY, newZ);
                  }
            }

            if (this.maximumCoord != null) {
                  curX = this.maximumCoord.getX();
                  curY = this.maximumCoord.getY();
                  curZ = this.maximumCoord.getZ();
                  partCoord = partPos.getX();
                  newX = partCoord > curX ? partCoord : curX;
                  partCoord = partPos.getY();
                  newY = partCoord > curY ? partCoord : curY;
                  partCoord = partPos.getZ();
                  newZ = partCoord > curZ ? partCoord : curZ;
                  if (newX != curX || newY != curY || newZ != curZ) {
                        this.maximumCoord = new BlockPos(newX, newY, newZ);
                  }
            }

            REGISTRY.addDirtyController(this.WORLD, this);
      }

      protected abstract void onBlockAdded(IMultiblockPart var1);

      protected abstract void onBlockRemoved(IMultiblockPart var1);

      protected abstract void onMachineAssembled();

      protected abstract void onMachineRestored();

      protected abstract void onMachinePaused();

      protected abstract void onMachineDisassembled();

      private void onDetachBlock(IMultiblockPart part) {
            part.onDetached(this);
            this.onBlockRemoved(part);
            part.forfeitMultiblockSaveDelegate();
            this.minimumCoord = this.maximumCoord = null;
            if (this.referenceCoord != null && this.referenceCoord.equals(part.getWorldPosition())) {
                  this.referenceCoord = null;
            }

            this.shouldCheckForDisconnections = true;
      }

      public void detachBlock(IMultiblockPart part, boolean chunkUnloading) {
            if (chunkUnloading && this.assemblyState == MultiblockControllerBase.AssemblyState.Assembled) {
                  this.assemblyState = MultiblockControllerBase.AssemblyState.Paused;
                  this.onMachinePaused();
            }

            this.onDetachBlock(part);
            if (!this.connectedParts.remove(part)) {
                  BlockPos position = part.getWorldPosition();
                  ZeroCore.getLogger().warn("[%s] Double-removing part (%d) @ %d, %d, %d, this is unexpected and may cause problems. If you encounter anomalies, please tear down the reactor and rebuild it.", this.WORLD.isRemote ? "CLIENT" : "SERVER", part.hashCode(), position.getX(), position.getY(), position.getZ());
            }

            if (this.connectedParts.isEmpty()) {
                  REGISTRY.addDeadController(this.WORLD, this);
            } else {
                  REGISTRY.addDirtyController(this.WORLD, this);
                  if (this.referenceCoord == null) {
                        this.selectNewReferenceCoord();
                  }

            }
      }

      protected abstract int getMinimumNumberOfBlocksForAssembledMachine();

      protected abstract int getMaximumXSize();

      protected abstract int getMaximumZSize();

      protected abstract int getMaximumYSize();

      protected int getMinimumXSize() {
            return 1;
      }

      protected int getMinimumYSize() {
            return 1;
      }

      protected int getMinimumZSize() {
            return 1;
      }

      public ValidationError getLastError() {
            return this.lastValidationError;
      }

      public void setLastError(ValidationError error) {
            if (null == error) {
                  throw new IllegalArgumentException("The validation error can't be null");
            } else {
                  this.lastValidationError = error;
            }
      }

      public void setLastError(String messageFormatStringResourceKey, Object... messageParameters) {
            this.lastValidationError = new ValidationError(messageFormatStringResourceKey, messageParameters);
      }

      protected abstract boolean isMachineWhole(IMultiblockValidator var1);

      public void checkIfMachineIsWhole() {
            MultiblockControllerBase.AssemblyState oldState = this.assemblyState;
            this.lastValidationError = null;
            if (this.isMachineWhole(this)) {
                  this.assembleMachine(oldState);
            } else if (oldState == MultiblockControllerBase.AssemblyState.Assembled) {
                  this.disassembleMachine();
            }

      }

      private void assembleMachine(MultiblockControllerBase.AssemblyState oldState) {
            Iterator var2 = this.connectedParts.iterator();

            IMultiblockPart part;
            while(var2.hasNext()) {
                  part = (IMultiblockPart)var2.next();
                  part.onPreMachineAssembled(this);
                  part.onMachineAssembled(this);
            }

            this.assemblyState = MultiblockControllerBase.AssemblyState.Assembled;
            MultiblockControllerBase.AssemblyState var10001 = this.assemblyState;
            if (oldState == MultiblockControllerBase.AssemblyState.Paused) {
                  this.onMachineRestored();
            } else {
                  this.onMachineAssembled();
            }

            var2 = this.connectedParts.iterator();

            while(var2.hasNext()) {
                  part = (IMultiblockPart)var2.next();
                  part.onPostMachineAssembled(this);
            }

      }

      private void disassembleMachine() {
            Iterator var1 = this.connectedParts.iterator();

            IMultiblockPart part;
            while(var1.hasNext()) {
                  part = (IMultiblockPart)var1.next();
                  part.onPreMachineBroken();
                  part.onMachineBroken();
            }

            this.assemblyState = MultiblockControllerBase.AssemblyState.Disassembled;
            this.onMachineDisassembled();
            var1 = this.connectedParts.iterator();

            while(var1.hasNext()) {
                  part = (IMultiblockPart)var1.next();
                  part.onPostMachineBroken();
            }

      }

      public void assimilate(MultiblockControllerBase other) {
            BlockPos otherReferenceCoord = other.getReferenceCoord();
            if (otherReferenceCoord != null && this.getReferenceCoord().compareTo(otherReferenceCoord) >= 0) {
                  throw new IllegalArgumentException("The controller with the lowest minimum-coord value must consume the one with the higher coords");
            } else {
                  Set partsToAcquire = new HashSet(other.connectedParts);
                  other._onAssimilated(this);
                  Iterator var5 = partsToAcquire.iterator();

                  while(var5.hasNext()) {
                        IMultiblockPart acquiredPart = (IMultiblockPart)var5.next();
                        if (!acquiredPart.isPartInvalid()) {
                              this.connectedParts.add(acquiredPart);
                              acquiredPart.onAssimilated(this);
                              this.onBlockAdded(acquiredPart);
                        }
                  }

                  this.onAssimilate(other);
                  other.onAssimilated(this);
            }
      }

      private void _onAssimilated(MultiblockControllerBase otherController) {
            if (this.referenceCoord != null) {
                  if (this.WORLD.isBlockLoaded(this.referenceCoord)) {
                        TileEntity te = this.getTile();
                        if (te instanceof IMultiblockPart) {
                              ((IMultiblockPart)te).forfeitMultiblockSaveDelegate();
                        }
                  }

                  this.referenceCoord = null;
            }

            this.connectedParts.clear();
      }

      protected abstract void onAssimilate(MultiblockControllerBase var1);

      protected abstract void onAssimilated(MultiblockControllerBase var1);

      public final void updateMultiblockEntity() {
            if (this.connectedParts.isEmpty()) {
                  REGISTRY.addDeadController(this.WORLD, this);
            } else if (this.assemblyState == MultiblockControllerBase.AssemblyState.Assembled) {
                  if (this.WORLD.isRemote) {
                        this.updateClient();
                  } else if (this.updateServer() && this.minimumCoord != null && this.maximumCoord != null && this.WORLD.isAreaLoaded(this.minimumCoord, this.maximumCoord)) {
                        int minChunkX = WorldHelper.getChunkXFromBlock(this.minimumCoord);
                        int minChunkZ = WorldHelper.getChunkZFromBlock(this.minimumCoord);
                        int maxChunkX = WorldHelper.getChunkXFromBlock(this.maximumCoord);
                        int maxChunkZ = WorldHelper.getChunkZFromBlock(this.maximumCoord);

                        for(int x = minChunkX; x <= maxChunkX; ++x) {
                              for(int z = minChunkZ; z <= maxChunkZ; ++z) {
                                    Chunk chunkToSave = this.WORLD.getChunk(x, z);
                                    chunkToSave.markDirty();
                              }
                        }
                  }

            }
      }

      protected abstract boolean updateServer();

      protected abstract void updateClient();

      protected abstract boolean isBlockGoodForFrame(World var1, int var2, int var3, int var4, IMultiblockValidator var5);

      protected abstract boolean isBlockGoodForTop(World var1, int var2, int var3, int var4, IMultiblockValidator var5);

      protected abstract boolean isBlockGoodForBottom(World var1, int var2, int var3, int var4, IMultiblockValidator var5);

      protected abstract boolean isBlockGoodForSides(World var1, int var2, int var3, int var4, IMultiblockValidator var5);

      protected abstract boolean isBlockGoodForInterior(World var1, int var2, int var3, int var4, IMultiblockValidator var5);

      public BlockPos getReferenceCoord() {
            if (this.referenceCoord == null) {
                  this.selectNewReferenceCoord();
            }

            return this.referenceCoord;
      }

      public int getNumConnectedBlocks() {
            return this.connectedParts.size();
      }

      protected abstract void syncDataFrom(NBTTagCompound var1, ModTileEntity.SyncReason var2);

      protected abstract void syncDataTo(NBTTagCompound var1, ModTileEntity.SyncReason var2);

      public void recalculateMinMaxCoords() {
            int minZ = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int minX = Integer.MAX_VALUE;
            int maxZ = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int maxX = Integer.MIN_VALUE;
            Iterator var9 = this.connectedParts.iterator();

            while(var9.hasNext()) {
                  IMultiblockPart part = (IMultiblockPart)var9.next();
                  BlockPos partPos = part.getWorldPosition();
                  int partCoord = partPos.getX();
                  if (partCoord < minX) {
                        minX = partCoord;
                  }

                  if (partCoord > maxX) {
                        maxX = partCoord;
                  }

                  partCoord = partPos.getY();
                  if (partCoord < minY) {
                        minY = partCoord;
                  }

                  if (partCoord > maxY) {
                        maxY = partCoord;
                  }

                  partCoord = partPos.getZ();
                  if (partCoord < minZ) {
                        minZ = partCoord;
                  }

                  if (partCoord > maxZ) {
                        maxZ = partCoord;
                  }
            }

            this.minimumCoord = new BlockPos(minX, minY, minZ);
            this.maximumCoord = new BlockPos(maxX, maxY, maxZ);
      }

      public BlockPos getMinimumCoord() {
            if (this.minimumCoord == null) {
                  this.recalculateMinMaxCoords();
            }

            return this.minimumCoord;
      }

      public BlockPos getMaximumCoord() {
            if (this.maximumCoord == null) {
                  this.recalculateMinMaxCoords();
            }

            return this.maximumCoord;
      }

      public boolean isEmpty() {
            return this.connectedParts.isEmpty();
      }

      public boolean shouldConsume(MultiblockControllerBase otherController) {
            if (!otherController.getClass().equals(this.getClass())) {
                  throw new IllegalArgumentException("Attempting to merge two multiblocks with different master classes - this should never happen!");
            } else if (otherController == this) {
                  return false;
            } else {
                  int res = this._shouldConsume(otherController);
                  if (res < 0) {
                        return true;
                  } else if (res > 0) {
                        return false;
                  } else {
                        ZeroCore.getLogger().warn("[%s] Encountered two controllers with the same reference coordinate. Auditing connected parts and retrying.", this.WORLD.isRemote ? "CLIENT" : "SERVER");
                        this.auditParts();
                        otherController.auditParts();
                        res = this._shouldConsume(otherController);
                        if (res < 0) {
                              return true;
                        } else if (res > 0) {
                              return false;
                        } else {
                              ZeroCore.getLogger().error("My Controller (%d): size (%d), parts: %s", this.hashCode(), this.connectedParts.size(), this.getPartsListString());
                              ZeroCore.getLogger().error("Other Controller (%d): size (%d), coords: %s", otherController.hashCode(), otherController.connectedParts.size(), otherController.getPartsListString());
                              throw new IllegalArgumentException("[" + (this.WORLD.isRemote ? "CLIENT" : "SERVER") + "] Two controllers with the same reference coord that somehow both have valid parts - this should never happen!");
                        }
                  }
            }
      }

      private int _shouldConsume(MultiblockControllerBase otherController) {
            BlockPos myCoord = this.getReferenceCoord();
            BlockPos theirCoord = otherController.getReferenceCoord();
            return theirCoord == null ? -1 : myCoord.compareTo(theirCoord);
      }

      private String getPartsListString() {
            StringBuilder sb = new StringBuilder();
            boolean first = true;

            for(Iterator var4 = this.connectedParts.iterator(); var4.hasNext(); first = false) {
                  IMultiblockPart part = (IMultiblockPart)var4.next();
                  if (!first) {
                        sb.append(", ");
                  }

                  BlockPos partPos = part.getWorldPosition();
                  sb.append(String.format("(%d: %d, %d, %d)", part.hashCode(), partPos.getX(), partPos.getY(), partPos.getZ()));
            }

            return sb.toString();
      }

      private void auditParts() {
            HashSet deadParts = new HashSet();
            Iterator var2 = this.connectedParts.iterator();

            while(true) {
                  IMultiblockPart part;
                  do {
                        if (!var2.hasNext()) {
                              this.connectedParts.removeAll(deadParts);
                              ZeroCore.getLogger().warn("[%s] Controller found %d dead parts during an audit, %d parts remain attached", this.WORLD.isRemote ? "CLIENT" : "SERVER", deadParts.size(), this.connectedParts.size());
                              return;
                        }

                        part = (IMultiblockPart)var2.next();
                  } while(!part.isPartInvalid() && part == this.getTile(part.getWorldPosition()));

                  this.onDetachBlock(part);
                  deadParts.add(part);
            }
      }

      public Set checkForDisconnections() {
            if (!this.shouldCheckForDisconnections) {
                  return null;
            } else if (this.isEmpty()) {
                  REGISTRY.addDeadController(this.WORLD, this);
                  return null;
            } else {
                  IChunkProvider chunkProvider = this.WORLD.getChunkProvider();
                  this.referenceCoord = null;
                  Set deadParts = new HashSet();
                  IMultiblockPart referencePart = null;
                  int originalSize = this.connectedParts.size();
                  Iterator var7 = this.connectedParts.iterator();

                  while(true) {
                        while(var7.hasNext()) {
                              IMultiblockPart part = (IMultiblockPart)var7.next();
                              BlockPos position = part.getWorldPosition();
                              if (this.WORLD.isBlockLoaded(position) && !part.isPartInvalid()) {
                                    if (part != this.getTile(position)) {
                                          deadParts.add(part);
                                          this.onDetachBlock(part);
                                    } else {
                                          part.setUnvisited();
                                          part.forfeitMultiblockSaveDelegate();
                                          if (this.referenceCoord == null) {
                                                this.referenceCoord = position;
                                                referencePart = part;
                                          } else if (position.compareTo(this.referenceCoord) < 0) {
                                                this.referenceCoord = position;
                                                referencePart = part;
                                          }
                                    }
                              } else {
                                    deadParts.add(part);
                                    this.onDetachBlock(part);
                              }
                        }

                        this.connectedParts.removeAll(deadParts);
                        deadParts.clear();
                        if (referencePart != null && !this.isEmpty()) {
                              referencePart.becomeMultiblockSaveDelegate();
                              LinkedList partsToCheck = new LinkedList();
                              IMultiblockPart[] nearbyParts = null;
                              int visitedParts = 0;
                              partsToCheck.add(referencePart);

                              while(!partsToCheck.isEmpty()) {
                                    IMultiblockPart part = (IMultiblockPart)partsToCheck.removeFirst();
                                    part.setVisited();
                                    ++visitedParts;
                                    nearbyParts = part.getNeighboringParts();
                                    IMultiblockPart[] var11 = nearbyParts;
                                    int var12 = nearbyParts.length;

                                    for(int var13 = 0; var13 < var12; ++var13) {
                                          IMultiblockPart nearbyPart = var11[var13];
                                          if (nearbyPart.getMultiblockController() == this && !nearbyPart.isVisited()) {
                                                nearbyPart.setVisited();
                                                partsToCheck.add(nearbyPart);
                                          }
                                    }
                              }

                              Set removedParts = new HashSet();
                              Iterator var18 = this.connectedParts.iterator();

                              while(var18.hasNext()) {
                                    IMultiblockPart orphanCandidate = (IMultiblockPart)var18.next();
                                    if (!orphanCandidate.isVisited()) {
                                          deadParts.add(orphanCandidate);
                                          orphanCandidate.onOrphaned(this, originalSize, visitedParts);
                                          this.onDetachBlock(orphanCandidate);
                                          removedParts.add(orphanCandidate);
                                    }
                              }

                              this.connectedParts.removeAll(deadParts);
                              deadParts.clear();
                              if (this.referenceCoord == null) {
                                    this.selectNewReferenceCoord();
                              }

                              this.shouldCheckForDisconnections = false;
                              return removedParts;
                        }

                        this.shouldCheckForDisconnections = false;
                        REGISTRY.addDeadController(this.WORLD, this);
                        return null;
                  }
            }
      }

      public Set detachAllBlocks() {
            if (this.WORLD == null) {
                  return new HashSet();
            } else {
                  IChunkProvider chunkProvider = this.WORLD.getChunkProvider();
                  Iterator var2 = this.connectedParts.iterator();

                  while(var2.hasNext()) {
                        IMultiblockPart part = (IMultiblockPart)var2.next();
                        if (this.WORLD.isBlockLoaded(part.getWorldPosition())) {
                              this.onDetachBlock(part);
                        }
                  }

                  Set detachedParts = this.connectedParts;
                  this.connectedParts = new HashSet();
                  return detachedParts;
            }
      }

      public boolean isAssembled() {
            return this.assemblyState == MultiblockControllerBase.AssemblyState.Assembled;
      }

      public boolean isDisassembled() {
            return this.assemblyState == MultiblockControllerBase.AssemblyState.Disassembled;
      }

      public boolean isPaused() {
            return this.assemblyState == MultiblockControllerBase.AssemblyState.Paused;
      }

      private void selectNewReferenceCoord() {
            IChunkProvider chunkProvider = this.WORLD.getChunkProvider();
            IMultiblockPart theChosenOne = null;
            this.referenceCoord = null;
            Iterator var4 = this.connectedParts.iterator();

            while(true) {
                  BlockPos position;
                  IMultiblockPart part;
                  do {
                        do {
                              do {
                                    if (!var4.hasNext()) {
                                          if (theChosenOne != null) {
                                                theChosenOne.becomeMultiblockSaveDelegate();
                                          }

                                          return;
                                    }

                                    part = (IMultiblockPart)var4.next();
                                    position = part.getWorldPosition();
                              } while(part.isPartInvalid());
                        } while(!this.WORLD.isBlockLoaded(position));
                  } while(this.referenceCoord != null && this.referenceCoord.compareTo(position) <= 0);

                  this.referenceCoord = position;
                  theChosenOne = part;
            }
      }

      protected void markReferenceCoordForUpdate() {
            BlockPos rc = this.getReferenceCoord();
            if (this.WORLD != null && rc != null) {
                  WorldHelper.notifyBlockUpdate(this.WORLD, rc, (IBlockState)null, (IBlockState)null);
            }

      }

      protected void markReferenceCoordDirty() {
            if (this.WORLD != null && !this.WORLD.isRemote) {
                  BlockPos referenceCoord = this.getReferenceCoord();
                  if (referenceCoord != null) {
                        TileEntity saveTe = this.getTile(referenceCoord);
                        this.WORLD.markChunkDirty(referenceCoord, saveTe);
                  }
            }
      }

      protected void markMultiblockForRenderUpdate() {
            this.WORLD.markBlockRangeForRenderUpdate(this.getMinimumCoord(), this.getMaximumCoord());
      }

      protected boolean doesPartBelong(IMultiblockPart part) {
            return this == part.getMultiblockController() || this.connectedParts.contains(part);
      }

      public void forceStructureUpdate(World world) {
      }

      @Nullable
      protected TileEntity getTile() {
            return WorldHelper.getTile((IBlockAccess)this.WORLD, (BlockPos)this.referenceCoord);
      }

      @Nullable
      protected TileEntity getTile(@Nonnull BlockPos position) {
            return WorldHelper.getTile((IBlockAccess)this.WORLD, (BlockPos)position);
      }

      protected static enum AssemblyState {
            Disassembled,
            Assembled,
            Paused;
      }
}
