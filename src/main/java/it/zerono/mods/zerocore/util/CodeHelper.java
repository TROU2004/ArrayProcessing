package it.zerono.mods.zerocore.util;

import it.zerono.mods.zerocore.internal.ZeroCore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public final class CodeHelper {
      public static String getModIdFromActiveModContainer() {
            ModContainer mc = Loader.instance().activeModContainer();
            String modId = null != mc ? mc.getModId() : null;
            if (null != modId && !modId.isEmpty()) {
                  return modId;
            } else {
                  throw new RuntimeException("Cannot retrieve the MOD ID from FML");
            }
      }

      public static IThreadListener getClientThreadListener() {
            return ZeroCore.getProxy().getClientThreadListener();
      }

      public static IThreadListener getServerThreadListener() {
            return ZeroCore.getProxy().getServerThreadListener();
      }

      public static String i18nValue(boolean value) {
            return I18n.format(value ? "debug.zerocore.true" : "debug.zerocore.false", new Object[0]);
      }

      public static void sendChatMessage(@Nonnull ICommandSender sender, @Nonnull ITextComponent component) {
            if (sender instanceof EntityPlayer) {
                  ((EntityPlayer)sender).sendStatusMessage(component, false);
            } else {
                  sender.sendMessage(component);
            }

      }

      public static void sendStatusMessage(@Nonnull EntityPlayer player, @Nonnull ITextComponent message) {
            ZeroCore.getProxy().sendPlayerStatusMessage(player, message);
      }

      public static boolean runningInDevEnv() {
            return (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
      }

      @Nullable
      public static NBTTagCompound nbtLoadFrom(File file) {
            if (null == file) {
                  throw new IllegalArgumentException("The file to read from cannot be null");
            } else {
                  if (file.exists()) {
                        try {
                              FileInputStream stream = new FileInputStream(file);
                              Throwable var2 = null;

                              NBTTagCompound var3;
                              try {
                                    var3 = CompressedStreamTools.readCompressed(stream);
                              } catch (Throwable var13) {
                                    var2 = var13;
                                    throw var13;
                              } finally {
                                    if (stream != null) {
                                          if (var2 != null) {
                                                try {
                                                      stream.close();
                                                } catch (Throwable var12) {
                                                      var2.addSuppressed(var12);
                                                }
                                          } else {
                                                stream.close();
                                          }
                                    }

                              }

                              return var3;
                        } catch (Exception var15) {
                              var15.printStackTrace();
                        }
                  }

                  return null;
            }
      }

      @Nullable
      public static boolean nbtSaveTo(File file, NBTTagCompound data) {
            if (null == file) {
                  throw new IllegalArgumentException("The file to write to cannot be null");
            } else if (null == data) {
                  throw new IllegalArgumentException("The data to write cannot be null");
            } else {
                  try {
                        FileOutputStream stream = new FileOutputStream(file);
                        Throwable var3 = null;

                        boolean var4;
                        try {
                              CompressedStreamTools.writeCompressed(data, stream);
                              var4 = true;
                        } catch (Throwable var14) {
                              var3 = var14;
                              throw var14;
                        } finally {
                              if (stream != null) {
                                    if (var3 != null) {
                                          try {
                                                stream.close();
                                          } catch (Throwable var13) {
                                                var3.addSuppressed(var13);
                                          }
                                    } else {
                                          stream.close();
                                    }
                              }

                        }

                        return var4;
                  } catch (Exception var16) {
                        var16.printStackTrace();
                        return false;
                  }
            }
      }

      public static float mathLerp(float from, float to, float modifier) {
            modifier = Math.min(1.0F, Math.max(0.0F, modifier));
            return from + modifier * (to - from);
      }

      public static int mathVolume(BlockPos minimum, BlockPos maximum) {
            return minimum != null && maximum != null ? mathVolume(minimum.getX(), minimum.getY(), minimum.getZ(), maximum.getX(), maximum.getY(), maximum.getZ()) : 0;
      }

      public static int mathVolume(int x1, int y1, int z1, int x2, int y2, int z2) {
            int cx = Math.abs(x2 - x1) + 1;
            int cy = Math.abs(y2 - y1) + 1;
            int cz = Math.abs(z2 - z1) + 1;
            return cx * cy * cz;
      }

      public static int argb(float red, float green, float blue, float alpha) {
            return argb(MathHelper.floor(red * 255.0F), MathHelper.floor(green * 255.0F), MathHelper.floor(blue * 255.0F), MathHelper.floor(alpha * 255.0F));
      }

      public static int argb(int red, int green, int blue, int alpha) {
            int color = (alpha << 8) + red;
            color = (color << 8) + green;
            color = (color << 8) + blue;
            return color;
      }
}
