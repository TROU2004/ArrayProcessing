package trou.array_processing.tile;

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import trou.array_processing.block.BlockArrayWall;
import trou.array_processing.multiblock.ProcessingArrayController;

public class TileEntityBase extends RectangularMultiblockTileEntityBase {

    @Override
    public Class<ProcessingArrayController> getMultiblockControllerType() {
        return ProcessingArrayController.class;
    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }

    @Override
    public MultiblockControllerBase createNewMultiblock() {
        return new ProcessingArrayController(world);
    }

    @Override
    public boolean isGoodForFrame(IMultiblockValidator validator) {
        if (world.getBlockState(pos).getBlock() instanceof BlockArrayWall) return true;
        validator.setLastError(new ValidationError("array_processing.multiblock.validation.invalid_block_notwall", pos.getX(), pos.getY(), pos.getZ()));
        return true;
    }

    @Override
    public boolean isGoodForSides(IMultiblockValidator validator) {
        return true;
    }

    @Override
    public boolean isGoodForTop(IMultiblockValidator validator) {
        return true;
    }

    @Override
    public boolean isGoodForBottom(IMultiblockValidator validator) {
        return true;
    }

    @Override
    public boolean isGoodForInterior(IMultiblockValidator validator) {
        return false;
    }
}
