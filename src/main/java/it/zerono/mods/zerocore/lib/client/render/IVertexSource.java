package it.zerono.mods.zerocore.lib.client.render;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IVertexSource {
      void uploadVertexData(@Nonnull BufferBuilder var1);
}
