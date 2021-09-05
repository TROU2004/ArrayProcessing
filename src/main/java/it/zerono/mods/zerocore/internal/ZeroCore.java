package it.zerono.mods.zerocore.internal;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
      modid = "zerocore",
      name = "ZeroCore-AP",
      acceptedMinecraftVersions = "1.12.2",
      dependencies = "required-after:forge@[14.23.5.2847,15.0.0.0);after:cofhcore",
      version = "1.12.2-0.1.2.9"
)
public final class ZeroCore {
      @Instance
      private static ZeroCore s_instance;
      @SidedProxy(
            clientSide = "it.zerono.mods.zerocore.internal.client.ClientProxy",
            serverSide = "it.zerono.mods.zerocore.internal.common.CommonProxy"
      )
      private static CommonProxy s_proxy;
      private static Logger s_modLogger;

      public static CommonProxy getProxy() {
            return s_proxy;
      }

      public static Logger getLogger() {
            if (null == s_modLogger) {
                  s_modLogger = LogManager.getLogger("zerocore");
            }

            return s_modLogger;
      }
}
