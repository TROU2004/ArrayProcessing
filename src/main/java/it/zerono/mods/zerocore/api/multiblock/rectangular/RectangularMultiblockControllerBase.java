package it.zerono.mods.zerocore.api.multiblock.rectangular;

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RectangularMultiblockControllerBase extends MultiblockControllerBase {
      protected RectangularMultiblockControllerBase(World world) {
            super(world);
      }

      protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
            if (this.connectedParts.size() < this.getMinimumNumberOfBlocksForAssembledMachine()) {
                  validatorCallback.setLastError(ValidationError.VALIDATION_ERROR_TOO_FEW_PARTS);
                  return false;
            } else {
                  BlockPos maximumCoord = this.getMaximumCoord();
                  BlockPos minimumCoord = this.getMinimumCoord();
                  int minX = minimumCoord.getX();
                  int minY = minimumCoord.getY();
                  int minZ = minimumCoord.getZ();
                  int maxX = maximumCoord.getX();
                  int maxY = maximumCoord.getY();
                  int maxZ = maximumCoord.getZ();
                  int deltaX = maxX - minX + 1;
                  int deltaY = maxY - minY + 1;
                  int deltaZ = maxZ - minZ + 1;
                  int maxXSize = this.getMaximumXSize();
                  int maxYSize = this.getMaximumYSize();
                  int maxZSize = this.getMaximumZSize();
                  int minXSize = this.getMinimumXSize();
                  int minYSize = this.getMinimumYSize();
                  int minZSize = this.getMinimumZSize();
                  if (maxXSize > 0 && deltaX > maxXSize) {
                        validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", maxXSize, "X");
                        return false;
                  } else if (maxYSize > 0 && deltaY > maxYSize) {
                        validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", maxYSize, "Y");
                        return false;
                  } else if (maxZSize > 0 && deltaZ > maxZSize) {
                        validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", maxZSize, "Z");
                        return false;
                  } else if (deltaX < minXSize) {
                        validatorCallback.setLastError("zerocore:zerocore:api.multiblock.validation.machine_too_small", minXSize, "X");
                        return false;
                  } else if (deltaY < minYSize) {
                        validatorCallback.setLastError("zerocore:zerocore:api.multiblock.validation.machine_too_small", minYSize, "Y");
                        return false;
                  } else if (deltaZ < minZSize) {
                        validatorCallback.setLastError("zerocore:zerocore:api.multiblock.validation.machine_too_small", minZSize, "Z");
                        return false;
                  } else {
                        Class myClass = this.getClass();

                        for(int x = minX; x <= maxX; ++x) {
                              for(int y = minY; y <= maxY; ++y) {
                                    for(int z = minZ; z <= maxZ; ++z) {
                                          TileEntity te = this.getTile(new BlockPos(x, y, z));
                                          RectangularMultiblockTileEntityBase part;
                                          if (te instanceof RectangularMultiblockTileEntityBase) {
                                                part = (RectangularMultiblockTileEntityBase)te;
                                                if (!myClass.equals(part.getMultiblockControllerType())) {
                                                      validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part", x, y, z);
                                                      return false;
                                                }

                                                if (!this.doesPartBelong(part)) {
                                                      validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_foreign_part", x, y, z);
                                                      return false;
                                                }
                                          } else {
                                                part = null;
                                          }

                                          int extremes = 0;
                                          if (x == minX) {
                                                ++extremes;
                                          }

                                          if (y == minY) {
                                                ++extremes;
                                          }

                                          if (z == minZ) {
                                                ++extremes;
                                          }

                                          if (x == maxX) {
                                                ++extremes;
                                          }

                                          if (y == maxY) {
                                                ++extremes;
                                          }

                                          if (z == maxZ) {
                                                ++extremes;
                                          }

                                          boolean isPartValid;
                                          if (extremes >= 2) {
                                                isPartValid = part != null ? part.isGoodForFrame(validatorCallback) : this.isBlockGoodForFrame(this.WORLD, x, y, z, validatorCallback);
                                                if (!isPartValid) {
                                                      if (null == validatorCallback.getLastError()) {
                                                            validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_frame", x, y, z);
                                                      }

                                                      return false;
                                                }
                                          } else if (extremes == 1) {
                                                if (y == maxY) {
                                                      isPartValid = part != null ? part.isGoodForTop(validatorCallback) : this.isBlockGoodForTop(this.WORLD, x, y, z, validatorCallback);
                                                      if (!isPartValid) {
                                                            if (null == validatorCallback.getLastError()) {
                                                                  validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_top", x, y, z);
                                                            }

                                                            return false;
                                                      }
                                                } else if (y == minY) {
                                                      isPartValid = part != null ? part.isGoodForBottom(validatorCallback) : this.isBlockGoodForBottom(this.WORLD, x, y, z, validatorCallback);
                                                      if (!isPartValid) {
                                                            if (null == validatorCallback.getLastError()) {
                                                                  validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_bottom", x, y, z);
                                                            }

                                                            return false;
                                                      }
                                                } else {
                                                      isPartValid = part != null ? part.isGoodForSides(validatorCallback) : this.isBlockGoodForSides(this.WORLD, x, y, z, validatorCallback);
                                                      if (!isPartValid) {
                                                            if (null == validatorCallback.getLastError()) {
                                                                  validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_sides", x, y, z);
                                                            }

                                                            return false;
                                                      }
                                                }
                                          } else {
                                                isPartValid = part != null ? part.isGoodForInterior(validatorCallback) : this.isBlockGoodForInterior(this.WORLD, x, y, z, validatorCallback);
                                                if (!isPartValid) {
                                                      if (null == validatorCallback.getLastError()) {
                                                            validatorCallback.setLastError("zerocore:api.multiblock.validation.reactor.invalid_part_for_interior", x, y, z);
                                                      }

                                                      return false;
                                                }
                                          }
                                    }
                              }
                        }

                        return true;
                  }
            }
      }

      public void forceStructureUpdate(World world) {
            BlockPos minCoord = this.getMinimumCoord();
            BlockPos maxCoord = this.getMaximumCoord();
            int minX = minCoord.getX();
            int minY = minCoord.getY();
            int minZ = minCoord.getZ();
            int maxX = maxCoord.getX();
            int maxY = maxCoord.getY();
            int maxZ = maxCoord.getZ();

            for(int x = minX; x <= maxX; ++x) {
                  for(int y = minY; y <= maxY; ++y) {
                        for(int z = minZ; z <= maxZ; ++z) {
                              BlockPos pos = new BlockPos(x, y, z);
                              IBlockState state = world.getBlockState(pos);
                              world.notifyBlockUpdate(pos, state, state, 3);
                        }
                  }
            }

      }
}
