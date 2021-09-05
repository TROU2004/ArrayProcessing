package it.zerono.mods.zerocore.api.multiblock.rectangular;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public enum PartPosition implements IStringSerializable {
      Unknown((EnumFacing)null, PartPosition.Type.Unknown),
      Interior((EnumFacing)null, PartPosition.Type.Unknown),
      FrameCorner((EnumFacing)null, PartPosition.Type.Frame),
      FrameEastWest((EnumFacing)null, PartPosition.Type.Frame),
      FrameSouthNorth((EnumFacing)null, PartPosition.Type.Frame),
      FrameUpDown((EnumFacing)null, PartPosition.Type.Frame),
      TopFace(EnumFacing.UP, PartPosition.Type.Face),
      BottomFace(EnumFacing.DOWN, PartPosition.Type.Face),
      NorthFace(EnumFacing.NORTH, PartPosition.Type.Face),
      SouthFace(EnumFacing.SOUTH, PartPosition.Type.Face),
      EastFace(EnumFacing.EAST, PartPosition.Type.Face),
      WestFace(EnumFacing.WEST, PartPosition.Type.Face);

      private EnumFacing _facing;
      private PartPosition.Type _type;

      public boolean isFace() {
            return this._type == PartPosition.Type.Face;
      }

      public boolean isFrame() {
            return this._type == PartPosition.Type.Frame;
      }

      public EnumFacing getFacing() {
            return this._facing;
      }

      public PartPosition.Type getType() {
            return this._type;
      }

      public static PropertyEnum createProperty(String name) {
            return PropertyEnum.create(name, PartPosition.class);
      }

      public String getName() {
            return this.toString();
      }

      private PartPosition(EnumFacing facing, PartPosition.Type type) {
            this._facing = facing;
            this._type = type;
      }

      public static enum Type {
            Unknown,
            Interior,
            Frame,
            Face;
      }
}
