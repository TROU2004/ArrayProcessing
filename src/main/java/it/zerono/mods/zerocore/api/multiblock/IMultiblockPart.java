package it.zerono.mods.zerocore.api.multiblock;

import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IMultiblockPart {
      boolean isConnected();

      boolean isMachineAssembled();

      boolean isMachineDisassembled();

      boolean isMachinePaused();

      MultiblockControllerBase getMultiblockController();

      BlockPos getWorldPosition();

      boolean isPartInvalid();

      void onAttached(MultiblockControllerBase var1);

      void onDetached(MultiblockControllerBase var1);

      void onOrphaned(MultiblockControllerBase var1, int var2, int var3);

      MultiblockControllerBase createNewMultiblock();

      Class getMultiblockControllerType();

      void onAssimilated(MultiblockControllerBase var1);

      void setVisited();

      void setUnvisited();

      boolean isVisited();

      void becomeMultiblockSaveDelegate();

      void forfeitMultiblockSaveDelegate();

      boolean isMultiblockSaveDelegate();

      IMultiblockPart[] getNeighboringParts();

      /** @deprecated */
      @Deprecated
      void onMachineAssembled(MultiblockControllerBase var1);

      void onPreMachineAssembled(MultiblockControllerBase var1);

      void onPostMachineAssembled(MultiblockControllerBase var1);

      /** @deprecated */
      @Deprecated
      void onMachineBroken();

      void onPreMachineBroken();

      void onPostMachineBroken();

      void onMachineActivated();

      void onMachineDeactivated();

      Set attachToNeighbors();

      void assertDetached();

      boolean hasMultiblockSaveData();

      NBTTagCompound getMultiblockSaveData();

      void onMultiblockDataAssimilated();
}
