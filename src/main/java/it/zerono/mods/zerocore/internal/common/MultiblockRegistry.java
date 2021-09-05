package it.zerono.mods.zerocore.internal.common;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.IMultiblockRegistry;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.internal.ZeroCore;
import java.util.HashMap;
import net.minecraft.world.World;

final class MultiblockRegistry implements IMultiblockRegistry {
      private HashMap _registries = new HashMap(2);
      protected static final MultiblockRegistry INSTANCE = new MultiblockRegistry();

      public void onPartAdded(World world, IMultiblockPart part) {
            MultiblockWorldRegistry registry;
            if (this._registries.containsKey(world)) {
                  registry = (MultiblockWorldRegistry)this._registries.get(world);
            } else {
                  registry = new MultiblockWorldRegistry(world);
                  this._registries.put(world, registry);
            }

            registry.onPartAdded(part);
      }

      public void onPartRemovedFromWorld(World world, IMultiblockPart part) {
            if (this._registries.containsKey(world)) {
                  ((MultiblockWorldRegistry)this._registries.get(world)).onPartRemovedFromWorld(part);
            }

      }

      public void addDeadController(World world, MultiblockControllerBase controller) {
            if (this._registries.containsKey(world)) {
                  ((MultiblockWorldRegistry)this._registries.get(world)).addDeadController(controller);
            } else {
                  ZeroCore.getLogger().info("Controller %d in world %s marked as dead, but that world is not tracked! Controller is being ignored.", controller.hashCode(), world);
            }

      }

      public void addDirtyController(World world, MultiblockControllerBase controller) {
            if (this._registries.containsKey(world)) {
                  ((MultiblockWorldRegistry)this._registries.get(world)).addDirtyController(controller);
            } else {
                  ZeroCore.getLogger().error("Adding a dirty controller to a world that has no registered controllers! [ID=%d]", null != world ? world.provider.getDimension() : Integer.MIN_VALUE);
            }

      }

      void tickStart(World world) {
            if (this._registries.containsKey(world)) {
                  MultiblockWorldRegistry registry = (MultiblockWorldRegistry)this._registries.get(world);
                  registry.processMultiblockChanges();
                  registry.tickStart();
            }

      }

      void onChunkLoaded(World world, int chunkX, int chunkZ) {
            if (this._registries.containsKey(world)) {
                  ((MultiblockWorldRegistry)this._registries.get(world)).onChunkLoaded(chunkX, chunkZ);
            }

      }

      void onWorldUnloaded(World world) {
            if (this._registries.containsKey(world)) {
                  ((MultiblockWorldRegistry)this._registries.get(world)).onWorldUnloaded();
                  this._registries.remove(world);
            }

      }

      private MultiblockRegistry() {
      }
}
