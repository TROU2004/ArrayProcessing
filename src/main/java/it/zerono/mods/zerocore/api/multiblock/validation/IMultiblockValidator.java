package it.zerono.mods.zerocore.api.multiblock.validation;

public interface IMultiblockValidator {
      ValidationError getLastError();

      void setLastError(ValidationError var1);

      void setLastError(String var1, Object... var2);
}
