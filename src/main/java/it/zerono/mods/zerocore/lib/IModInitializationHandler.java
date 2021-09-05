package it.zerono.mods.zerocore.lib;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IModInitializationHandler {
      void onPreInit(FMLPreInitializationEvent var1);

      void onInit(FMLInitializationEvent var1);

      void onPostInit(FMLPostInitializationEvent var1);
}
