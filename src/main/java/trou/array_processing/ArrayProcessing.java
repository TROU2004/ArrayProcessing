package trou.array_processing;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArrayProcessing.MODID, name = ArrayProcessing.NAME, version = ArrayProcessing.VERSION, useMetadata = true)
@Mod.EventBusSubscriber(modid = ArrayProcessing.MODID)
public class ArrayProcessing {
    public static final String MODID = "array_processing";
    public static final String NAME = "Array Processing";
    public static final String VERSION = "1.0.0";
    private Logger logger;

    @Mod.Instance
    public static ArrayProcessing INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @SubscribeEvent
    public void registerBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new Block(Material.ROCK).setRegistryName("structure_block"),
                new Block(Material.ROCK).setRegistryName("structure_glass"),
                new Block(Material.ROCK).setRegistryName("structure_controller"),
                new Block(Material.ROCK).setRegistryName("structure_input_bus"),
                new Block(Material.ROCK).setRegistryName("structure_output_bus"),
                new Block(Material.ROCK).setRegistryName("structure_energy_bus")
        );
    }

    public static void info(String str) {
        INSTANCE.logger.info(str);
    }
}
