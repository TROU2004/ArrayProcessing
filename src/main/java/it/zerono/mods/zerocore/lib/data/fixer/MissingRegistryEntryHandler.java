package it.zerono.mods.zerocore.lib.data.fixer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Action;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.registries.IForgeRegistryEntry;

class MissingRegistryEntryHandler {
      private final ResourceLocation _key;
      private final Action _remapAction;
      private final IForgeRegistryEntry _replacement;
      private int _oldId;

      static MissingRegistryEntryHandler remap(@Nonnull ResourceLocation key, @Nonnull IForgeRegistryEntry replacement) {
            return new MissingRegistryEntryHandler(key, Action.REMAP, replacement);
      }

      static MissingRegistryEntryHandler ignore(@Nonnull ResourceLocation key) {
            return new MissingRegistryEntryHandler(key, Action.IGNORE, (IForgeRegistryEntry)null);
      }

      static MissingRegistryEntryHandler warn(@Nonnull ResourceLocation key) {
            return new MissingRegistryEntryHandler(key, Action.WARN, (IForgeRegistryEntry)null);
      }

      static MissingRegistryEntryHandler fail(@Nonnull ResourceLocation key) {
            return new MissingRegistryEntryHandler(key, Action.FAIL, (IForgeRegistryEntry)null);
      }

      @Nonnull
      ResourceLocation getKey() {
            return this._key;
      }

      @Nonnull
      Action getRemapAction() {
            return this._remapAction;
      }

      @Nonnull
      IForgeRegistryEntry getReplacement() {
            return this._replacement;
      }

      int getOldId() {
            return this._oldId;
      }

      void remap(@Nonnull Mapping mapping) {
            if (this.getKey().equals(mapping.key)) {
                  this._oldId = mapping.id;
                  switch(this.getRemapAction()) {
                  case FAIL:
                        mapping.fail();
                        break;
                  case IGNORE:
                        mapping.ignore();
                        break;
                  case REMAP:
                        mapping.remap(this.getReplacement());
                        break;
                  case WARN:
                        mapping.warn();
                  }
            }

      }

      protected MissingRegistryEntryHandler(@Nonnull ResourceLocation key, @Nonnull Action action, @Nullable IForgeRegistryEntry replacement) {
            this._key = key;
            this._remapAction = action;
            this._replacement = replacement;
            this._oldId = -1;
      }
}
