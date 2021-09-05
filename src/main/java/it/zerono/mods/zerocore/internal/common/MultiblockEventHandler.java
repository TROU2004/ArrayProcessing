package it.zerono.mods.zerocore.internal.common;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

final class MultiblockEventHandler {
      @SubscribeEvent(
            priority = EventPriority.NORMAL
      )
      public void onChunkLoad(Load loadEvent) {
            Chunk chunk = loadEvent.getChunk();
            MultiblockRegistry.INSTANCE.onChunkLoaded(loadEvent.getWorld(), chunk.x, chunk.z);
      }

      @SubscribeEvent(
            priority = EventPriority.NORMAL
      )
      public void onWorldUnload(Unload unloadWorldEvent) {
            MultiblockRegistry.INSTANCE.onWorldUnloaded(unloadWorldEvent.getWorld());
      }

      @SubscribeEvent
      public void onWorldTick(WorldTickEvent event) {
            if (Phase.START == event.phase) {
                  MultiblockRegistry.INSTANCE.tickStart(event.world);
            }

      }

      @SideOnly(Side.CLIENT)
      @SubscribeEvent
      public void onClientTick(ClientTickEvent event) {
            if (Phase.START == event.phase) {
                  MultiblockRegistry.INSTANCE.tickStart(Minecraft.getMinecraft().world);
            }

      }
}
