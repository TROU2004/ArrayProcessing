package it.zerono.mods.zerocore.lib.data.fixer;

import com.google.common.collect.Maps;
import it.zerono.mods.zerocore.internal.ZeroCore;
import java.util.Map;
import javax.annotation.Nonnull;
import joptsimple.internal.Strings;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;

public class TileEntityNameFixer implements IFixableData {
      private final int _dataVersion;
      private final String _filterPrefix;
      private final Map _remappings;

      public TileEntityNameFixer(int dataVersion, @Nonnull String filterPrefix) {
            this._dataVersion = dataVersion;
            this._filterPrefix = filterPrefix;
            this._remappings = Maps.newHashMap();
      }

      public void addReplacement(@Nonnull String oldName, @Nonnull ResourceLocation newId) {
            this._remappings.put(oldName, newId.toString());
      }

      public int getFixVersion() {
            return this._dataVersion;
      }

      public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
            String teName = compound.getString("id");
            if (teName.startsWith(this._filterPrefix)) {
                  String newName = (String)this._remappings.get(teName);
                  if (!Strings.isNullOrEmpty(newName)) {
                        compound.setString("id", newName);
                        ZeroCore.getLogger().debug("Remapped old TileEntity ID '{}' with '{}'", teName, newName);
                  } else {
                        ZeroCore.getLogger().debug("No remapping found for TileEntity ID '{}'", teName);
                  }
            }

            return compound;
      }
}
