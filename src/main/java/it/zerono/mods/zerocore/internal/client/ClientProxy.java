package it.zerono.mods.zerocore.internal.client;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.ITextComponent;

public class ClientProxy extends CommonProxy {
      public void sendPlayerStatusMessage(@Nonnull EntityPlayer player, @Nonnull ITextComponent message) {
            if (player instanceof EntityPlayerSP) {
                  Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(message.getUnformattedText());
            }

      }

      public IThreadListener getClientThreadListener() {
            return Minecraft.getMinecraft();
      }

      public IThreadListener getServerThreadListener() {
            return Minecraft.getMinecraft().getIntegratedServer();
      }
}
