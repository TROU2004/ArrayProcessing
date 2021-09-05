package it.zerono.mods.zerocore.lib.network;

import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class ModMessageHandler implements IMessageHandler {
      public IMessage onMessage(final IMessage message, final MessageContext ctx) {
            IThreadListener listener = this.getThreadListener(ctx);
            listener.addScheduledTask(new Runnable() {
                  public void run() {
                        ModMessageHandler.this.processMessage(message, ctx);
                  }
            });
            return null;
      }

      protected abstract IThreadListener getThreadListener(MessageContext var1);

      protected abstract void processMessage(IMessage var1, MessageContext var2);
}
