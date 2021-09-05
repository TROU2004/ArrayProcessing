package it.zerono.mods.zerocore.internal.common;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

final class MultiblockWorldRegistry {
      private World worldObj;
      private final Set controllers;
      private final Set dirtyControllers;
      private final Set deadControllers;
      private Set orphanedParts;
      private final Set detachedParts;
      private final HashMap partsAwaitingChunkLoad;
      private final Object partsAwaitingChunkLoadMutex;
      private final Object orphanedPartsMutex;

      MultiblockWorldRegistry(World world) {
            this.worldObj = world;
            this.controllers = new HashSet();
            this.deadControllers = new HashSet();
            this.dirtyControllers = new HashSet();
            this.detachedParts = new HashSet();
            this.orphanedParts = new HashSet();
            this.partsAwaitingChunkLoad = new HashMap();
            this.partsAwaitingChunkLoadMutex = new Object();
            this.orphanedPartsMutex = new Object();
      }

      void tickStart() {
            this.worldObj.profiler.startSection("Zero CORE|Multiblock|Tick");
            if (this.controllers.size() > 0) {
                  Iterator var1 = this.controllers.iterator();

                  while(var1.hasNext()) {
                        MultiblockControllerBase controller = (MultiblockControllerBase)var1.next();
                        if (controller.WORLD == this.worldObj && controller.WORLD.isRemote == this.worldObj.isRemote) {
                              if (controller.isEmpty()) {
                                    this.deadControllers.add(controller);
                              } else {
                                    controller.updateMultiblockEntity();
                              }
                        }
                  }
            }

            this.worldObj.profiler.endSection();
      }

      void processMultiblockChanges() {
            this.worldObj.profiler.startSection("Zero CORE|Multiblock|Merge");
            List mergePools = null;
            Set newlyDetachedParts;
            Iterator var6;
            if (this.orphanedParts.size() > 0) {
                  newlyDetachedParts = null;
                  synchronized(this.orphanedPartsMutex) {
                        if (this.orphanedParts.size() > 0) {
                              newlyDetachedParts = this.orphanedParts;
                              this.orphanedParts = new HashSet();
                        }
                  }

                  if (newlyDetachedParts != null && newlyDetachedParts.size() > 0) {
                        IChunkProvider chunkProvider = this.worldObj.getChunkProvider();
                        var6 = newlyDetachedParts.iterator();

                        label182:
                        while(true) {
                              while(true) {
                                    BlockPos coord;
                                    IMultiblockPart orphan;
                                    do {
                                          do {
                                                do {
                                                      if (!var6.hasNext()) {
                                                            break label182;
                                                      }

                                                      orphan = (IMultiblockPart)var6.next();
                                                      coord = orphan.getWorldPosition();
                                                } while(!this.worldObj.isBlockLoaded(coord));
                                          } while(orphan.isPartInvalid());
                                    } while(this.getMultiblockPartFromWorld(this.worldObj, coord) != orphan);

                                    Set compatibleControllers = orphan.attachToNeighbors();
                                    if (compatibleControllers == null) {
                                          MultiblockControllerBase newController = orphan.createNewMultiblock();
                                          newController.attachBlock(orphan);
                                          this.controllers.add(newController);
                                    } else if (compatibleControllers.size() > 1) {
                                          if (mergePools == null) {
                                                mergePools = new ArrayList();
                                          }

                                          boolean hasAddedToPool = false;
                                          List candidatePools = new ArrayList();
                                          Iterator var10 = mergePools.iterator();

                                          Set consumedPool;
                                          while(var10.hasNext()) {
                                                consumedPool = (Set)var10.next();
                                                if (!Collections.disjoint(consumedPool, compatibleControllers)) {
                                                      candidatePools.add(consumedPool);
                                                }
                                          }

                                          if (candidatePools.size() <= 0) {
                                                mergePools.add(compatibleControllers);
                                          } else if (candidatePools.size() == 1) {
                                                ((Set)candidatePools.get(0)).addAll(compatibleControllers);
                                          } else {
                                                Set masterPool = (Set)candidatePools.get(0);

                                                for(int i = 1; i < candidatePools.size(); ++i) {
                                                      consumedPool = (Set)candidatePools.get(i);
                                                      masterPool.addAll(consumedPool);
                                                      mergePools.remove(consumedPool);
                                                }

                                                masterPool.addAll(compatibleControllers);
                                          }
                                    }
                              }
                        }
                  }
            }

            Iterator var14;
            MultiblockControllerBase newMaster;
            if (mergePools != null && mergePools.size() > 0) {
                  var14 = mergePools.iterator();

                  label139:
                  while(true) {
                        label137:
                        while(true) {
                              if (!var14.hasNext()) {
                                    break label139;
                              }

                              Set mergePool = (Set)var14.next();
                              newMaster = null;
                              var6 = mergePool.iterator();

                              while(true) {
                                    MultiblockControllerBase controller;
                                    do {
                                          if (!var6.hasNext()) {
                                                if (newMaster == null) {
                                                      ZeroCore.getLogger().error("Multiblock system checked a merge pool of size %d, found no master candidates. This should never happen.", mergePool.size());
                                                      continue label137;
                                                }

                                                this.addDirtyController(newMaster);
                                                var6 = mergePool.iterator();

                                                while(var6.hasNext()) {
                                                      controller = (MultiblockControllerBase)var6.next();
                                                      if (controller != newMaster) {
                                                            newMaster.assimilate(controller);
                                                            this.addDeadController(controller);
                                                            this.addDirtyController(newMaster);
                                                      }
                                                }
                                                continue label137;
                                          }

                                          controller = (MultiblockControllerBase)var6.next();
                                    } while(newMaster != null && !controller.shouldConsume(newMaster));

                                    newMaster = controller;
                              }
                        }
                  }
            }

            this.worldObj.profiler.endStartSection("Zero CORE|Multiblock|Split&Assembly");
            if (this.dirtyControllers.size() > 0) {
                  newlyDetachedParts = null;
                  Iterator var16 = this.dirtyControllers.iterator();

                  while(var16.hasNext()) {
                        newMaster = (MultiblockControllerBase)var16.next();
                        newlyDetachedParts = newMaster.checkForDisconnections();
                        if (!newMaster.isEmpty()) {
                              newMaster.recalculateMinMaxCoords();
                              newMaster.checkIfMachineIsWhole();
                        } else {
                              this.addDeadController(newMaster);
                        }

                        if (newlyDetachedParts != null && newlyDetachedParts.size() > 0) {
                              this.detachedParts.addAll(newlyDetachedParts);
                        }
                  }

                  this.dirtyControllers.clear();
            }

            this.worldObj.profiler.endStartSection("Zero CORE|Multiblock|DeadControllers");
            if (this.deadControllers.size() > 0) {
                  MultiblockControllerBase controller;
                  for(var14 = this.deadControllers.iterator(); var14.hasNext(); this.controllers.remove(controller)) {
                        controller = (MultiblockControllerBase)var14.next();
                        if (!controller.isEmpty()) {
                              ZeroCore.getLogger().error("Found a non-empty controller. Forcing it to shed its blocks and die. This should never happen!");
                              this.detachedParts.addAll(controller.detachAllBlocks());
                        }
                  }

                  this.deadControllers.clear();
            }

            this.worldObj.profiler.endStartSection("Zero CORE|Multiblock|DetachedParts");
            var14 = this.detachedParts.iterator();

            while(var14.hasNext()) {
                  IMultiblockPart part = (IMultiblockPart)var14.next();
                  part.assertDetached();
            }

            this.addAllOrphanedPartsThreadsafe(this.detachedParts);
            this.detachedParts.clear();
            this.worldObj.profiler.endSection();
      }

      void onPartAdded(IMultiblockPart part) {
            BlockPos worldLocation = part.getWorldPosition();
            if (!this.worldObj.isBlockLoaded(worldLocation)) {
                  this.worldObj.profiler.startSection("Zero CORE|Multiblock|PartAdded");
                  long chunkHash = WorldHelper.getChunkXZHashFromBlock(worldLocation);
                  synchronized(this.partsAwaitingChunkLoadMutex) {
                        Object partSet;
                        if (!this.partsAwaitingChunkLoad.containsKey(chunkHash)) {
                              partSet = new HashSet();
                              this.partsAwaitingChunkLoad.put(chunkHash, partSet);
                        } else {
                              partSet = (Set)this.partsAwaitingChunkLoad.get(chunkHash);
                        }

                        ((Set)partSet).add(part);
                  }

                  this.worldObj.profiler.endSection();
            } else {
                  this.addOrphanedPartThreadsafe(part);
            }

      }

      void onPartRemovedFromWorld(IMultiblockPart part) {
            this.worldObj.profiler.startSection("Zero CORE|Multiblock|PartRemoved");
            BlockPos coord = part.getWorldPosition();
            if (coord != null) {
                  long hash = WorldHelper.getChunkXZHashFromBlock(coord);
                  if (this.partsAwaitingChunkLoad.containsKey(hash)) {
                        synchronized(this.partsAwaitingChunkLoadMutex) {
                              if (this.partsAwaitingChunkLoad.containsKey(hash)) {
                                    ((Set)this.partsAwaitingChunkLoad.get(hash)).remove(part);
                                    if (((Set)this.partsAwaitingChunkLoad.get(hash)).size() <= 0) {
                                          this.partsAwaitingChunkLoad.remove(hash);
                                    }
                              }
                        }
                  }
            }

            this.detachedParts.remove(part);
            if (this.orphanedParts.contains(part)) {
                  synchronized(this.orphanedPartsMutex) {
                        this.orphanedParts.remove(part);
                  }
            }

            part.assertDetached();
            this.worldObj.profiler.endSection();
      }

      void onWorldUnloaded() {
            this.controllers.clear();
            this.deadControllers.clear();
            this.dirtyControllers.clear();
            this.detachedParts.clear();
            synchronized(this.partsAwaitingChunkLoadMutex) {
                  this.partsAwaitingChunkLoad.clear();
            }

            synchronized(this.orphanedPartsMutex) {
                  this.orphanedParts.clear();
            }

            this.worldObj = null;
      }

      void onChunkLoaded(int chunkX, int chunkZ) {
            long chunkHash = ChunkPos.asLong(chunkX, chunkZ);
            if (this.partsAwaitingChunkLoad.containsKey(chunkHash)) {
                  synchronized(this.partsAwaitingChunkLoadMutex) {
                        if (this.partsAwaitingChunkLoad.containsKey(chunkHash)) {
                              this.addAllOrphanedPartsThreadsafe((Collection)this.partsAwaitingChunkLoad.get(chunkHash));
                              this.partsAwaitingChunkLoad.remove(chunkHash);
                        }
                  }
            }

      }

      void addDeadController(MultiblockControllerBase deadController) {
            this.deadControllers.add(deadController);
      }

      void addDirtyController(MultiblockControllerBase dirtyController) {
            this.dirtyControllers.add(dirtyController);
      }

      public Set getControllers() {
            return Collections.unmodifiableSet(this.controllers);
      }

      private IMultiblockPart getMultiblockPartFromWorld(World world, BlockPos position) {
            TileEntity te = WorldHelper.getTile((IBlockAccess)world, (BlockPos)position);
            return te instanceof IMultiblockPart ? (IMultiblockPart)te : null;
      }

      private void addOrphanedPartThreadsafe(IMultiblockPart part) {
            synchronized(this.orphanedPartsMutex) {
                  this.orphanedParts.add(part);
            }
      }

      private void addAllOrphanedPartsThreadsafe(Collection parts) {
            synchronized(this.orphanedPartsMutex) {
                  this.orphanedParts.addAll(parts);
            }
      }
}
