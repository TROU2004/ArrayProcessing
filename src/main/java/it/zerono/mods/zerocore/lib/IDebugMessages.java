package it.zerono.mods.zerocore.lib;

import net.minecraft.util.text.ITextComponent;

public interface IDebugMessages {
      void add(ITextComponent var1);

      void add(String var1, Object... var2);
}
