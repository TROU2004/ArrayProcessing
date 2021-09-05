package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.world.WorldHelper;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class ModTileEntity extends TileEntity {
      public boolean isUseableByPlayer(EntityPlayer entityplayer) {
            BlockPos position = this.getPos();
            if (this != this.getTile(position)) {
                  return false;
            } else {
                  return entityplayer.getDistanceSq((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D) <= 64.0D;
            }
      }

      public boolean canOpenGui(World world, BlockPos posistion, IBlockState state) {
            return false;
      }

      public boolean openGui(Object mod, EntityPlayer player, int guiId) {
            player.openGui(mod, guiId, this.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ());
            return true;
      }

      public Object getServerGuiElement(int guiId, EntityPlayer player) {
            return null;
      }

      public Object getClientGuiElement(int guiId, EntityPlayer player) {
            return null;
      }

      public void readFromNBT(NBTTagCompound data) {
            super.readFromNBT(data);
            this.syncDataFrom(data, ModTileEntity.SyncReason.FullSync);
      }

      public NBTTagCompound writeToNBT(NBTTagCompound data) {
            this.syncDataTo(super.writeToNBT(data), ModTileEntity.SyncReason.FullSync);
            return data;
      }

      public void handleUpdateTag(NBTTagCompound data) {
            super.readFromNBT(data);
            this.syncDataFrom(data, ModTileEntity.SyncReason.NetworkUpdate);
      }

      public NBTTagCompound getUpdateTag() {
            NBTTagCompound data = super.getUpdateTag();
            this.syncDataTo(data, ModTileEntity.SyncReason.NetworkUpdate);
            return data;
      }

      public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
            this.syncDataFrom(packet.getNbtCompound(), ModTileEntity.SyncReason.NetworkUpdate);
      }

      @Nullable
      public SPacketUpdateTileEntity getUpdatePacket() {
            NBTTagCompound data = new NBTTagCompound();
            this.syncDataTo(data, ModTileEntity.SyncReason.NetworkUpdate);
            return new SPacketUpdateTileEntity(this.getPos(), 0, data);
      }

      protected abstract void syncDataFrom(NBTTagCompound var1, ModTileEntity.SyncReason var2);

      protected abstract void syncDataTo(NBTTagCompound var1, ModTileEntity.SyncReason var2);

      public void markChunkDirty() {
            this.getWorld().markChunkDirty(this.getPos(), this);
      }

      public void callNeighborBlockChange() {
            WorldHelper.notifyNeighborsOfStateChange(this.getWorld(), this.getPos(), this.getBlockType());
      }

      /** @deprecated */
      @Deprecated
      public void callNeighborTileChange() {
      }

      public void notifyBlockUpdate() {
            WorldHelper.notifyBlockUpdate(this.getWorld(), this.getPos(), (IBlockState)null, (IBlockState)null);
      }

      public void notifyBlockUpdate(IBlockState oldState, IBlockState newState) {
            WorldHelper.notifyBlockUpdate(this.getWorld(), this.getPos(), oldState, newState);
      }

      public void nofityTileEntityUpdate() {
            this.markDirty();
            WorldHelper.notifyBlockUpdate(this.getWorld(), this.getPos(), (IBlockState)null, (IBlockState)null);
      }

      @Nullable
      protected TileEntity getTile(@Nonnull BlockPos position) {
            return WorldHelper.getTile((IBlockAccess)this.getWorld(), (BlockPos)position);
      }

      @Nullable
      protected TileEntity getTile(@Nonnull EnumFacing facing) {
            return WorldHelper.getTile((IBlockAccess)this.getWorld(), (BlockPos)this.getPos().offset(facing));
      }

      public static enum SyncReason {
            FullSync,
            NetworkUpdate;
      }
}
