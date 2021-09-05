package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class Colour {
      public static final Colour WHITE = new Colour(255, 255, 255, 255);
      public byte R;
      public byte G;
      public byte B;
      public byte A;

      public Colour(int r, int g, int b, int a) {
            this.set(r, g, b, a);
      }

      public Colour(double r, double g, double b, double a) {
            this.set(r, g, b, a);
      }

      public Colour(@Nonnull Colour other) {
            this.set(other);
      }

      @Nonnull
      public static Colour fromARGB(int packedARGB) {
            return new Colour(packedARGB >> 16 & 255, packedARGB >> 8 & 255, packedARGB & 255, packedARGB >> 24 & 255);
      }

      @Nonnull
      public static Colour fromRGBA(int packedRGBA) {
            return new Colour(packedRGBA >> 24 & 255, packedRGBA >> 16 & 255, packedRGBA >> 8 & 255, packedRGBA & 255);
      }

      public int toRGB() {
            return RGB(this.R, this.G, this.B);
      }

      public int toARGB() {
            return ARGB(this.R, this.G, this.B, this.A);
      }

      public int toRGBA() {
            return RGBA(this.R, this.G, this.B, this.A);
      }

      public static int RGB(int r, int g, int b) {
            return r << 16 | g << 8 | b;
      }

      public static int RGB(byte r, byte g, byte b) {
            return RGB(r & 255, g & 255, b & 255);
      }

      public static int RGB(double r, double g, double b) {
            return RGB((int)(r * 255.0D), (int)(g * 255.0D), (int)(b * 255.0D));
      }

      public static int RGBA(int r, int g, int b, int a) {
            return r << 24 | g << 16 | b << 8 | a;
      }

      public static int RGBA(byte r, byte g, byte b, byte a) {
            return RGBA(r & 255, g & 255, b & 255, a & 255);
      }

      public static int RGBA(double r, double g, double b, double a) {
            return RGBA((int)(r * 255.0D), (int)(g * 255.0D), (int)(b * 255.0D), (int)(a * 255.0D));
      }

      public static int ARGB(int r, int g, int b, int a) {
            return a << 24 | r << 16 | g << 8 | b;
      }

      public static int ARGB(byte r, byte g, byte b, byte a) {
            return ARGB(a & 255, r & 255, g & 255, b & 255);
      }

      public static int ARGB(double r, double g, double b, double a) {
            return ARGB((int)(a * 255.0D), (int)(r * 255.0D), (int)(g * 255.0D), (int)(b * 255.0D));
      }

      @Nonnull
      public Colour set(int r, int g, int b, int a) {
            this.R = (byte)r;
            this.G = (byte)g;
            this.B = (byte)b;
            this.A = (byte)a;
            return this;
      }

      @Nonnull
      public Colour set(double r, double g, double b, double a) {
            return this.set((int)(r * 255.0D), (int)(g * 255.0D), (int)(b * 255.0D), (int)(a * 255.0D));
      }

      @Nonnull
      public Colour set(@Nonnull Colour other) {
            return this.set(other.R, other.G, other.B, other.A);
      }

      public boolean equals(Object other) {
            if (!(other instanceof Colour)) {
                  return false;
            } else {
                  Colour c = (Colour)other;
                  return this.R == c.R && this.G == c.G && this.B == c.B && this.A == c.A;
            }
      }

      public String toString() {
            return String.format("Colour R 0x%02x, G 0x%02x, B 0x%02x, A 0x%02x", this.R, this.G, this.B, this.A);
      }

      private Colour() {
      }
}
