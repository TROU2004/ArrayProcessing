package it.zerono.mods.zerocore.lib.fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHandlerForwarder implements IFluidHandler {
      private IFluidHandler _handler;

      public FluidHandlerForwarder(@Nonnull IFluidHandler handler) {
            this.setHandler(handler);
      }

      @Nonnull
      public IFluidHandler getHandler() {
            return this._handler;
      }

      public void setHandler(@Nonnull IFluidHandler handler) {
            this._handler = handler;
      }

      public IFluidTankProperties[] getTankProperties() {
            return this._handler.getTankProperties();
      }

      public int fill(FluidStack resource, boolean doFill) {
            return this._handler.fill(resource, doFill);
      }

      @Nullable
      public FluidStack drain(FluidStack resource, boolean doDrain) {
            return this._handler.drain(resource, doDrain);
      }

      @Nullable
      public FluidStack drain(int maxDrain, boolean doDrain) {
            return this._handler.drain(maxDrain, doDrain);
      }
}
