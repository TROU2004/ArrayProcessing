package it.zerono.mods.zerocore.internal.common.init;

import it.zerono.mods.zerocore.internal.common.item.ItemDebugTool;
import it.zerono.mods.zerocore.lib.init.GameObjectsHandler;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ObjectsHandler extends GameObjectsHandler {
      @ObjectHolder("zerocore:debugtool")
      public static final ItemDebugTool debugTool = null;
      private static final int DATA_VERSION = 1;

      public ObjectsHandler() {
            super(1);
      }

      protected void onRegisterItems(@Nonnull IForgeRegistry registry) {
            registry.register(new ItemDebugTool("debugtool"));
      }
}
