package it.zerono.mods.zerocore.lib;

public enum MetalSize {
      Block("block"),
      Ingot("ingot"),
      Nugget("nugget"),
      Dust("dust");

      public final String oreDictionaryPrefix;

      private MetalSize(String orePrefix) {
            this.oreDictionaryPrefix = orePrefix;
      }
}
