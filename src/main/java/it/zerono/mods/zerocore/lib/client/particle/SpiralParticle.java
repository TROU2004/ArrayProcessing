package it.zerono.mods.zerocore.lib.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SpiralParticle extends Particle {
      protected float _angle = 0.0F;
      protected double _centerX;
      protected double _centerZ;
      protected double _radius;

      protected SpiralParticle(World world, double centerX, double centerY, double centerZ, double radius, int lifeInTicks) {
            super(world, centerX + radius * 1.0D, centerY, centerZ + radius * 0.0D);
            this._centerX = centerX;
            this._centerZ = centerZ;
            this._radius = radius;
            this.particleMaxAge = lifeInTicks;
      }

      public void onUpdate() {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (++this.particleAge >= this.particleMaxAge) {
                  this.setExpired();
            }

            if ((this._angle += 10.0F) >= 360.0F) {
                  this._angle = 0.0F;
            }

            float radiants = (float)((double)this._angle * 3.141592653589793D / 180.0D);
            double newX = this._centerX + this._radius * (double)MathHelper.cos(radiants);
            double newZ = this._centerZ + this._radius * (double)MathHelper.sin(radiants);
            this.motionX = newX - this.posX;
            this.motionZ = newZ - this.posZ;
            this.motionY = 0.01D;
            this.move(this.motionX, this.motionY, this.motionZ);
      }
}
