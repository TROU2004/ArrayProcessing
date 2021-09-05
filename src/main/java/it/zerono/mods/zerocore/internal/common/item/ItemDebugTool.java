package it.zerono.mods.zerocore.internal.common.item;

import it.zerono.mods.zerocore.lib.IDebugMessages;
import it.zerono.mods.zerocore.lib.IDebuggable;
import it.zerono.mods.zerocore.lib.crafting.RecipeHelper;
import it.zerono.mods.zerocore.lib.item.ModItem;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import it.zerono.mods.zerocore.util.CodeHelper;
import it.zerono.mods.zerocore.util.OreDictionaryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemDebugTool extends ModItem {
      public ItemDebugTool(String itemName) {
            super(itemName);
            this.setMaxStackSize(1);
            this.setCreativeTab(CreativeTabs.TOOLS);
      }

      public void onRegisterRecipes(@Nonnull IForgeRegistry registry) {
            RecipeHelper.addShapedRecipe(new ItemStack(this, 1, 0), "IDI", "CGX", "IRI", 'I', Items.IRON_INGOT, 'D', Items.GLOWSTONE_DUST, 'C', Items.COMPARATOR, 'G', Blocks.GLASS, 'X', Items.COMPASS, 'R', Items.REDSTONE);
      }

      @SideOnly(Side.CLIENT)
      public void onRegisterModels() {
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
      }

      @SideOnly(Side.CLIENT)
      public void addInformation(ItemStack stack, @Nullable World playerIn, List tooltip, ITooltipFlag advanced) {
            tooltip.add(I18n.format("zerocore:debugTool.block.tooltip1", new Object[0]));
            tooltip.add("");
            tooltip.add(I18n.format("zerocore:debugTool.block.tooltip2", new Object[]{TextFormatting.ITALIC.toString()}));
            tooltip.add(I18n.format("zerocore:debugTool.block.tooltip3", new Object[]{TextFormatting.GREEN, TextFormatting.GRAY.toString() + TextFormatting.ITALIC.toString()}));
      }

      public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
            if (player.isSneaking() != WorldHelper.calledByLogicalClient(world)) {
                  return EnumActionResult.PASS;
            } else {
                  TileEntity te = WorldHelper.getTile((IBlockAccess)world, (BlockPos)pos);
                  if (te instanceof IDebuggable) {
                        IDebuggable debugee = (IDebuggable)te;
                        ItemDebugTool.MessagesPool pool = new ItemDebugTool.MessagesPool();
                        debugee.getDebugMessages(pool);
                        if (pool.MESSAGES.size() > 0) {
                              this.sendMessages(player, new TextComponentTranslation("zerocore:debugTool.tile.header", new Object[]{WorldHelper.getWorldSideName(world), pos.getX(), pos.getY(), pos.getZ()}), pool);
                              return EnumActionResult.SUCCESS;
                        }
                  }

                  if (world.isAirBlock(pos)) {
                        return EnumActionResult.PASS;
                  } else {
                        IBlockState blockState = world.getBlockState(pos);
                        String[] names = OreDictionaryHelper.getOreNames(blockState);
                        ItemDebugTool.MessagesPool pool = new ItemDebugTool.MessagesPool();
                        ITextComponent header = new TextComponentTranslation("zerocore:debugTool.block.header", new Object[]{WorldHelper.getWorldSideName(world), pos.getX(), pos.getY(), pos.getZ()});
                        if (null != names && names.length > 0) {
                              pool.add("zerocore:debugTool.block.intro", names.length, blockState.getBlock().getTranslationKey());
                              String[] var14 = names;
                              int var15 = names.length;

                              for(int var16 = 0; var16 < var15; ++var16) {
                                    String name = var14[var16];
                                    pool.add("zerocore:debugTool.block.nameentry", name);
                              }
                        } else {
                              pool.add("zerocore:debugTool.block.notfound");
                        }

                        this.sendMessages(player, header, pool);
                        return EnumActionResult.SUCCESS;
                  }
            }
      }

      private void sendMessages(EntityPlayer player, ITextComponent header, ItemDebugTool.MessagesPool pool) {
            CodeHelper.sendChatMessage(player, header);
            Iterator var4 = pool.MESSAGES.iterator();

            while(var4.hasNext()) {
                  ITextComponent message = (ITextComponent)var4.next();
                  CodeHelper.sendChatMessage(player, message);
            }

      }

      public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
            return false;
      }

      private static final class MessagesPool implements IDebugMessages {
            final List MESSAGES = new ArrayList(2);

            public MessagesPool() {
            }

            public void add(ITextComponent message) {
                  this.MESSAGES.add(message);
            }

            public void add(String messageFormatStringResourceKey, Object... messageParameters) {
                  this.MESSAGES.add(new TextComponentTranslation(messageFormatStringResourceKey, messageParameters));
            }
      }
}
