package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IMultiblockPartType extends IPropertyValue {
      @Nullable
      TileEntity createTileEntity(@Nonnull World var1, @Nonnull IBlockState var2);
}
