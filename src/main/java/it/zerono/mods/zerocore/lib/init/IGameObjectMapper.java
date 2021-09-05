package it.zerono.mods.zerocore.lib.init;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nonnull;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;

public interface IGameObjectMapper {
      void linkObjectsMap(@Nonnull ImmutableMap var1);

      void remap(@Nonnull Mapping var1);
}
